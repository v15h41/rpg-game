import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.SlickException;

/**
 * Represents a player object.
 * 
 */

public class Player extends Unit {

	/** Speed of the player */
	public static final float PLAYER_SPEED = 0.25f;

	/** Item not found */
	public static final int ITEM_NOT_FOUND = -1;
	
	/** Interaction range */
	public static final int INTERACT_RANGE = 50;

	// Respawn position of player in case of death
	public static final double RESPAWN_POS_X = 738.0;
	public static final double RESPAWN_POS_Y = 549.0;

	/** ArrayList of item objects that represents the player's inventory */
	private List<Item> inventory;

	/**
	 * Initializes the player object with necessary attributes
	 * 
	 * @param posX
	 *            Initial position of entity in the x-axis
	 * @param posY
	 *            Initial position of entity in the y-axis
	 * @param sprite
	 *            File path of entity sprite
	 * @param maxHP
	 *            Max HP of unit
	 * @param damage
	 *            Max damage of unit
	 * @param cooldown
	 *            Cooldown rate of unit
	 * @param name
	 *            Name of unit
	 * @throws SlickException
	 */
	public Player(double posX, double posY, String sprite, int maxHP, int damage, int cooldown, String name)
			throws SlickException {
		this.setSprite(sprite);
		this.setMaxHP(maxHP);
		this.setHP(maxHP);
		this.setCooldown(cooldown);
		this.setCooldownTime(cooldown);
		this.setPosX(posX);
		this.setPosY(posY);
		this.setDamage(damage);
		this.setSpeed(PLAYER_SPEED);

		// Initializes the inventory list
		inventory = new ArrayList<Item>();
	}

	@Override
	public void update(World world, double dirX, double dirY, int delta) {
		super.update(world, dirX, dirY, delta);
		checkForItem(world);

	}

	/**
	 * Checks surroundings for nearby items
	 * 
	 * @param world
	 *            Dependency on the world object
	 */
	private void checkForItem(World world) {
		List<Entity> nearBy = world.getNearByEntity(INTERACT_RANGE, this.getPosX(), this.getPosY());

		// If an item is found, remove it from the world and add it to the
		// inventory
		for (Entity currentEntity : nearBy) {
			if (currentEntity instanceof Item) {
				((Item) currentEntity).itemCollected(this);
				world.remove(currentEntity);
			}
		}
	}

	/**
	 * Attacks nearby units
	 * 
	 * @param world
	 *            Dependency on the world object
	 */
	public void attackUnit(World world) {
		List<Entity> nearBy = world.getNearByEntity(INTERACT_RANGE, this.getPosX(), this.getPosY());

		// If there is a monster nearby, attack it
		for (Entity currentEntity : nearBy) {
			if (currentEntity instanceof Monster) {
				((Monster) currentEntity).onAttack(world, this.attack());
			}
		}
	}

	/**
	 * Talks to nearby villages
	 * 
	 * @param world
	 *            Dependency on the world object
	 */
	public void interactVillager(World world) {
		List<Entity> nearBy = world.getNearByEntity(INTERACT_RANGE, this.getPosX(), this.getPosY());

		// If there is a villager nearby, talk to it
		for (Entity currentEntity : nearBy) {
			if (currentEntity instanceof Villager) {
				((Villager) currentEntity).talk(world, this);
			}
		}
	}

	/**
	 * Gets the inventory list of player
	 * 
	 * @return Inventory list
	 */
	public List<Item> getInventory() {
		return this.inventory;
	}

	@Override
	public void onAttack(World world, int damage) {
		takeDamage(damage);

		// If the player dies, respawn him/her instead of removing him
		if (this.getHP() < 1) {
			this.setPosX(RESPAWN_POS_X);
			this.setPosY(RESPAWN_POS_Y);

			this.setHP(this.getMaxHP());
		}
	}

	/**
	 * Returns the index of an item in the player's inventory
	 * 
	 * @param ID
	 *            Unique identifier of the item
	 * @return Index of the item in the inventory ArrayList
	 */
	public int findItem(int ID) {
		for (Item currentItem : inventory) {
			if (currentItem.getID() == ID) {
				return currentItem.getIndex();
			}
		}

		return ITEM_NOT_FOUND;
	}

	/**
	 * Remove item from the player's inventory
	 * 
	 * @param ID
	 *            Unique identifier of the item
	 */
	public void removeItem(int ID) {
		int itemIndex = findItem(ID);

		if (itemIndex != ITEM_NOT_FOUND) {
			inventory.remove(itemIndex);
		}
	}

}
