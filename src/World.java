
/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Author: Vishal Egbert
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 * Represents the entire game world. (Designed to be instantiated just once for
 * the whole game).
 */
public class World {
	// Constants for tile blocking
	public static final String TILE_PROPERTY_BLOCKED = "block";
	public static final String TILE_PROPERTY_BLOCKED_FALSE = "0";
	public static final String TILE_PROPERTY_BLOCKED_TRUE = "1";

	// Constants for column positions in TSV files
	public static final int COL_ID = 0;
	public static final int COL_TYPE = 1;
	public static final int COL_HP = 2;
	public static final int COL_DAMAGE = 3;
	public static final int COL_COOLDOWN = 4;
	public static final int XPOS = 1;
	public static final int YPOS = 2;

	// File path constants for sprites and other assets
	public static final String ASSETS = "assets/";
	public static final String MAP = "map.tmx";
	public static final String UNITS = "units/";
	public static final String ITEMS = "items/";
	public static final String ITEM_POSITIONS = "items.txt";
	public static final String ATTRIBUTES = "attributes.txt";
	public static final String UNIT_POSITIONS = "positions.txt";
	public static final String PANEL = "panel.png";

	public static final int LAYER_ID = 0;

	// Type IDs for units
	public static final int PLAYER_ID = 0;
	public static final int NPC_ID = 1;
	public static final int PASSIVE_ID = 2;
	public static final int AGGRESSIVE_ID = 3;

	// Constants related to rendering of boxes
	public static final int BAR_WIDTH = 70;
	public static final int BAR_HEIGHT = 20;
	public static final int BAR_ELEVATION = 50;
	public static final int BAR_EXTRA = 6;

	// Num attributes, players and items
	public static final int NUM_UNIT_ATTRIBUTES = 6;
	public static final int NUM_ITEM_ATTRIBUTES = 2;
	public static final int NUM_CHARACTERS = 9;
	public static final int NUM_ITEMS = 4;

	/** TiledMap object that represents the map of the world. */
	private TiledMap map;
	/** Player object that represents the player. */
	private Player player;
	/** Camera object that represents the game's viewport. */
	private Camera camera;
	/** ArrayList that holds all entities present in the game. */
	private List<Entity> entity;

	/** Creates a new World object and initialize other objects. */
	public World() throws SlickException {
		map = new TiledMap(ASSETS + MAP, ASSETS);
		entity = new ArrayList<Entity>();
		init();
		camera = new Camera(player);
	}

	/**
	 * Reads data from tab separated value files that store attributes and
	 * positions of units
	 * 
	 * @param attributeTable
	 *            Table of attributes, sprites and names of units
	 * @param characterSprites
	 *            Array of filenames of unit sprites
	 * @param itemSprites
	 *            Array of filenames of item sprites
	 * @param itemPositions
	 *            Table of item positions
	 * @param characterNames
	 *            Array of unit names
	 */
	private void readData(int[][] attributeTable, String[] characterSprites, String[] itemSprites,
			int[][] itemPositions, String[] characterNames) {
		Scanner reader;
		File file;

		try {
			file = new File(ASSETS + UNITS + ATTRIBUTES);
			reader = new Scanner(file);

			// Read attributes into attribute table, read character sprites and
			// names into respective arrays
			for (int i = 0; i < NUM_CHARACTERS; i++) {
				String line = reader.nextLine();
				String data[] = line.split("\t");

				// Sprites were stroed in the second last column
				characterSprites[i] = data[NUM_UNIT_ATTRIBUTES - 1];
				// Names were stored in the last column
				characterNames[i] = data[NUM_UNIT_ATTRIBUTES];

				@SuppressWarnings("resource")
				Scanner readLine = new Scanner(line);
				for (int j = 0; j < NUM_UNIT_ATTRIBUTES - 1; j++) {
					attributeTable[i][j] = readLine.nextInt();
				}
			}

			// Read item positions into item position table and read item
			// sprites into its array
			file = new File(ASSETS + ITEMS + ITEM_POSITIONS);
			reader = new Scanner(file);

			for (int i = 0; i < NUM_ITEMS; i++) {
				String line = reader.nextLine();
				String data[] = line.split("\t");

				itemSprites[i] = data[NUM_ITEM_ATTRIBUTES];

				@SuppressWarnings("resource")
				Scanner readLine = new Scanner(line);
				for (int j = 0; j < NUM_ITEM_ATTRIBUTES; j++) {
					itemPositions[i][j] = readLine.nextInt();
				}

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Get the entity list of the world
	 * 
	 * @return Entity list
	 */
	public List<Entity> getEntityList() {
		return entity;
	}

	/**
	 * Initialize all entities with starting attributes, positions and sprites
	 * 
	 * @throws SlickException
	 */
	private void init() throws SlickException {
		int[][] attributeTable = new int[NUM_CHARACTERS][NUM_UNIT_ATTRIBUTES];
		String[] characterSprites = new String[NUM_CHARACTERS];
		String[] characterNames = new String[NUM_CHARACTERS];
		String[] itemSprites = new String[NUM_ITEMS];
		int[][] itemPositions = new int[NUM_ITEMS][NUM_ITEM_ATTRIBUTES];

		Scanner reader;
		File file;

		readData(attributeTable, characterSprites, itemSprites, itemPositions, characterNames);

		// Initialize all items in the world, gives them an ID and adds them to
		// the entity ArrayList
		for (int i = 0; i < NUM_ITEMS; i++) {
			Item item = new Item(itemPositions[i][0], itemPositions[i][1], ASSETS + ITEMS + itemSprites[i], i);
			entity.add(item);
		}

		try {
			file = new File(ASSETS + UNITS + UNIT_POSITIONS);
			reader = new Scanner(file);

			// Based on positions from unit position tsv, intialize all unit
			// objects based on their stats
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				@SuppressWarnings("resource")
				Scanner readLine = new Scanner(line);

				int data[] = { readLine.nextInt(), readLine.nextInt(), readLine.nextInt() };

				// Switch based on unit types retrieved from the TSV
				switch (attributeTable[data[0]][COL_TYPE]) {
				case PLAYER_ID:
					player = new Player((double) data[XPOS], (double) data[YPOS], ASSETS + UNITS + characterSprites[0],
							attributeTable[0][COL_HP], attributeTable[0][COL_DAMAGE], attributeTable[0][COL_COOLDOWN],
							characterNames[attributeTable[data[0]][COL_ID]]);
					break;
				case NPC_ID:
					Villager villager = new Villager((double) data[XPOS], (double) data[YPOS],
							ASSETS + UNITS + characterSprites[attributeTable[data[0]][COL_ID]],
							attributeTable[0][COL_HP], attributeTable[0][COL_DAMAGE], attributeTable[0][COL_COOLDOWN],
							characterNames[attributeTable[data[0]][COL_ID]]);

					entity.add(villager);
					break;
				case PASSIVE_ID:
					PassiveMonster passive = new PassiveMonster((double) data[XPOS], (double) data[YPOS],
							ASSETS + UNITS + characterSprites[attributeTable[data[0]][COL_ID]],
							attributeTable[0][COL_HP], attributeTable[0][COL_DAMAGE], attributeTable[0][COL_COOLDOWN],
							characterNames[attributeTable[data[0]][COL_ID]]);
					entity.add(passive);
					break;
				case AGGRESSIVE_ID:
					AggressiveMonster aggressive = new AggressiveMonster((double) data[XPOS], (double) data[YPOS],
							ASSETS + UNITS + characterSprites[attributeTable[data[0]][COL_ID]],
							attributeTable[0][COL_HP], attributeTable[0][COL_DAMAGE], attributeTable[0][COL_COOLDOWN],
							characterNames[attributeTable[data[0]][COL_ID]]);
					entity.add(aggressive);
				}

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		refreshEntityIndex();
	}

	/**
	 * Update the game state for a frame.
	 * 
	 * @param dirX
	 *            The player's movement in the x axis (-1, 0 or 1).
	 * @param dirY
	 *            The player's movement in the y axis (-1, 0 or 1).
	 * @param attack
	 *            One if attack key is pressed
	 * @param talk
	 *            One if talk key is pressed
	 * @param delta
	 *            Time passed since last frame (milliseconds).
	 */
	public void update(double dirX, double dirY, double attack, double talk, int delta) throws SlickException {
		player.update(this, dirX, dirY, delta);
		camera.update();

		if (attack == 1)
			player.attackUnit(this);

		if (talk == 1)
			player.interactVillager(this);

		for (Entity currentEntity : entity) {
			if (currentEntity instanceof Unit) {
				((Unit) currentEntity).update(this, 0, 0, delta);
			}
		}
	}

	/**
	 * Render the entire screen, so it reflects the current game state.
	 * 
	 * @param g
	 *            The Slick graphics object, used for drawing.
	 */
	public void render(Graphics g) throws SlickException {
		map.render(camera.getOffsetXPos(), camera.getOffsetYPos(), camera.getXTile(), camera.getYTile(), 13, 10);

		// Render entities and their health bars and dialogue boxes if
		// applicable
		for (Entity currentEntity : entity) {
			currentEntity.renderEntity(g, camera.getRelativeX(currentEntity.getPosX()),
					camera.getRelativeY(currentEntity.getPosY()));

			if (currentEntity instanceof Unit)
				renderHealthBar(g, (Unit) currentEntity);

			if (currentEntity instanceof Villager)
				renderDialogueBox(g, (Villager) currentEntity);

		}

		player.renderEntity(g, camera.getRelativeX(player.getPosX()), camera.getRelativeY(player.getPosY()));

		renderPanel(g);
	}

	/**
	 * Checks to see if a position lies on an illegal tile of the map
	 * 
	 * @param x
	 *            Player's x-coordinate
	 * @param y
	 *            Player's y-coordinate
	 * @return boolean value indicating whether position is illegal or not
	 */
	public boolean isBlocked(double x, double y) {
		// Calculates tile position the player is trying to walk into
		double xTile = x / Camera.TILE_DIMENSION;
		double yTile = y / Camera.TILE_DIMENSION;

		int tileID = map.getTileId((int) xTile, (int) yTile, LAYER_ID);

		String property = map.getTileProperty(tileID, TILE_PROPERTY_BLOCKED, TILE_PROPERTY_BLOCKED_FALSE);

		// If the tile is blocked, return true
		if (property.equals(TILE_PROPERTY_BLOCKED_TRUE))
			return true;

		return false;
	}

	/**
	 * Renders the player's status panel.
	 * 
	 * @param g
	 *            The current Slick graphics context.
	 * @throws SlickException
	 */
	private void renderPanel(Graphics g) throws SlickException {
		// Panel colours
		Color LABEL = new Color(0.9f, 0.9f, 0.4f); // Gold
		Color VALUE = new Color(1.0f, 1.0f, 1.0f); // White
		Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f); // Black, transp
		Color BAR = new Color(0.8f, 0.0f, 0.0f, 0.8f); // Red, transp

		// Variables for layout
		String text; // Text to display
		int text_x, text_y; // Coordinates to draw text
		int bar_x, bar_y; // Coordinates to draw rectangles
		int bar_width, bar_height; // Size of rectangle to draw
		int hp_bar_width; // Size of red (HP) rectangle
		int inv_x, inv_y; // Coordinates to draw inventory item

		float health_percent; // Player's health, as a percentage

		// Panel background image
		Image panel = new Image(ASSETS + PANEL);
		panel.draw(0, RPG.SCREEN_HEIGHT - RPG.PANEL_HEIGHT);

		// Display the player's health
		text_x = 15;
		text_y = RPG.SCREEN_HEIGHT - RPG.PANEL_HEIGHT + 25;
		g.setColor(LABEL);
		g.drawString("Health:", text_x, text_y);
		text = player.getHP() + "/" + player.getMaxHP();

		bar_x = 90;
		bar_y = RPG.SCREEN_HEIGHT - RPG.PANEL_HEIGHT + 20;
		bar_width = 90;
		bar_height = 30;
		health_percent = (float) player.getHP() / (float) player.getMaxHP();

		hp_bar_width = (int) (bar_width * health_percent);
		text_x = bar_x + (bar_width - g.getFont().getWidth(text)) / 2;
		g.setColor(BAR_BG);
		g.fillRect(bar_x, bar_y, bar_width, bar_height);
		g.setColor(BAR);
		g.fillRect(bar_x, bar_y, hp_bar_width, bar_height);
		g.setColor(VALUE);
		g.drawString(text, text_x, text_y);

		// Display the player's damage and cooldown
		text_x = 200;
		g.setColor(LABEL);
		g.drawString("Damage:", text_x, text_y);
		text_x += 80;
		text = player.getDamage() + ""; // TODO: Damage
		g.setColor(VALUE);
		g.drawString(text, text_x, text_y);
		text_x += 40;
		g.setColor(LABEL);
		g.drawString("Rate:", text_x, text_y);
		text_x += 55;
		text = player.getCooldown() + ""; // TODO: Cooldown
		g.setColor(VALUE);
		g.drawString(text, text_x, text_y);

		// Display the player's inventory
		g.setColor(LABEL);
		g.drawString("Items:", 420, text_y);
		bar_x = 490;
		bar_y = RPG.SCREEN_HEIGHT - RPG.PANEL_HEIGHT + 10;
		bar_width = 288;
		bar_height = bar_height + 20;
		g.setColor(BAR_BG);
		g.fillRect(bar_x, bar_y, bar_width, bar_height);

		inv_x = 490;
		inv_y = RPG.SCREEN_HEIGHT - RPG.PANEL_HEIGHT + ((RPG.PANEL_HEIGHT - 72) / 2);
		List<Item> inventory = player.getInventory();
		for (Item item : inventory) {
			item.getSprite().draw(inv_x, inv_y);

			inv_x += 72;
		}
	}

	/**
	 * Gets player object
	 * 
	 * @return Player object
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Render health bar on top of each unit
	 * 
	 * @param g
	 *            Graphics class
	 * @param unit
	 *            Unit object
	 */
	private void renderHealthBar(Graphics g, Unit unit) {
		Color VALUE = new Color(1.0f, 1.0f, 1.0f); // White
		Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f); // Black, transp
		Color BAR = new Color(0.8f, 0.0f, 0.0f, 0.8f); // Red, transp

		Font font = g.getFont();

		int bar_width = BAR_WIDTH;
		int bar_height = BAR_HEIGHT;

		if (font.getWidth(unit.getName()) > BAR_WIDTH) {
			bar_width = font.getWidth(unit.getName()) + BAR_EXTRA;
		}

		int text_x = (int) unit.getPosX() - font.getWidth(unit.getName()) / 2;
		int text_y = (int) unit.getPosY() - BAR_ELEVATION;

		int bar_x = (int) unit.getPosX() - bar_width / 2;
		int bar_y = (int) unit.getPosY() - BAR_ELEVATION;

		g.setColor(BAR_BG);
		g.fillRect(camera.getRelativeX(bar_x), camera.getRelativeY(bar_y), bar_width, bar_height);

		float percentage = (float) unit.getHP() / (float) unit.getMaxHP();
		float health_width = percentage * bar_width;

		g.setColor(BAR);
		g.fillRect(camera.getRelativeX(bar_x), camera.getRelativeY(bar_y), health_width, bar_height);

		g.setColor(VALUE);
		g.drawString(unit.getName(), camera.getRelativeX(text_x), camera.getRelativeY(text_y));

	}

	/**
	 * Render the dialogue box for any villager that is speaking
	 * 
	 * @param g
	 *            Graphics class
	 * @param villager
	 *            Villager object
	 */
	private void renderDialogueBox(Graphics g, Villager villager) {
		Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);
		Color VALUE = new Color(1.0f, 1.0f, 1.0f);

		String dialogue = villager.getDialogue();
		Font font = g.getFont();
		int width = font.getWidth(dialogue);
		int bar_width = width + BAR_EXTRA;
		int bar_height = BAR_HEIGHT;

		// If any villager is speaking, render the dialogue box above the health
		// bar
		if (width != 0) {
			int text_x = (int) villager.getPosX() - (width / 2);
			int text_y = (int) villager.getPosY() - BAR_HEIGHT - BAR_ELEVATION;

			int bar_x = (int) villager.getPosX() - (bar_width / 2);

			g.setColor(BAR_BG);
			g.fillRect(camera.getRelativeX(bar_x), camera.getRelativeY(text_y), bar_width, bar_height);

			g.setColor(VALUE);
			g.drawString(dialogue, camera.getRelativeX(text_x), camera.getRelativeY(text_y));
		}
	}

	/**
	 * Update the index values of each entity and item in the entity ArrayList
	 * and inventory ArrayList
	 */
	private void refreshEntityIndex() {
		int index = 0;

		for (Entity currentEntity : entity) {
			currentEntity.setIndex(index);
			index++;
		}

		int index2 = 0;
		
		for (Item currentItem : player.getInventory()) {
			currentItem.setIndex(index2);
			index2++;
		}
	}

	/**
	 * Remove entity from the entity array list
	 * 
	 * @param unit
	 *            Unit to be removed
	 */
	public void remove(Entity unit) {
		entity.remove(unit.getIndex());
		refreshEntityIndex();
	}

	/**
	 * Calculate the distance between two points on the world
	 * 
	 * @param posX1
	 * @param posY1
	 * @param posX2
	 * @param posY2
	 * @return Distance between two points
	 */
	public double getDistance(double posX1, double posY1, double posX2, double posY2) {
		double a = posX1 - posX2;
		double b = posY1 - posY2;

		double c = Math.pow(a, 2.0) + Math.pow(b, 2.0);

		return Math.sqrt(c);
	}

	/**
	 * Get nearby entities within a certain pixel range
	 * 
	 * @param pixels
	 *            Pixel range of discovery
	 * @param posX
	 *            Position in the x-axis
	 * @param posY
	 *            Position in the y-axis
	 * @return An ArrayList of all nearby entities
	 */
	public List<Entity> getNearByEntity(int pixels, double posX, double posY) {
		List<Entity> nearByEntities = new ArrayList<Entity>();

		for (Entity currentEntity : entity) {
			if (getDistance(currentEntity.getPosX(), currentEntity.getPosY(), posX, posY) < pixels) {
				nearByEntities.add(currentEntity);
			}
		}

		return nearByEntities;
	}

}
