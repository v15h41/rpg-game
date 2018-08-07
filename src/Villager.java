import java.util.List;

import org.newdawn.slick.SlickException;

/**
 * Represents the villager object, handles dialogue and effects on player
 *
 */
public class Villager extends Unit {

	// Dialogue constants
	public static final String ELVIRA = "Elvira";
	public static final String ELVIRA_HP_FULL = "Return to me if you ever need healing.";
	public static final String ELVIRA_HP_HEALED = "You're looking much healthier now.";

	public static final String PRINCE = "Prince Aldric";
	public static final String PRINCE_HAS_ELIXIR = "The elixir! My father is cured! Thankyou!";
	public static final String PRINCE_NO_ELIXIR = "Please seek out the Elixir of Life to cure the king.";

	public static final String GARTH = "Garth";
	public static final String GARTH_AMULET = "Find the Amulet of Vitality, across the river to the west.";
	public static final String GARTH_SWORD = "Find the Sword of Strength - cross the river and back, on the east side";
	public static final String GARTH_TOME = "Find the Tome of Agility, in the Land of Shadows.";
	public static final String GARTH_ALL = "You have found all the treasure I know of.";

	// Item constants
	public static final int AMULET_ID = 0;
	public static final int SWORD_ID = 1;
	public static final int TOME_ID = 2;
	public static final int ELIXIR_ID = 3;
	
	/** Item not found */
	public static final int ITEM_NOT_FOUND = -1;

	/** Allocated time spent displaying a dialogue box */
	public static final int TALK_TIME = 4000;
	
	/** Current string being spoken by the villager */
	private String currentDialogue = "";
	/** Timer that counts talking time elapsed */
	private int talkTimer;

	/**
	 * Initializes the villager object with necessary attributes
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
	public Villager(double posX, double posY, String sprite, int maxHP, int damage, int cooldown, String name)
			throws SlickException {
		this.setSprite(sprite);
		this.setMaxHP(maxHP);
		this.setHP(maxHP);
		this.setCooldown(cooldown);
		this.setPosX(posX);
		this.setPosY(posY);
		this.setName(name);
		this.talkTimer = TALK_TIME;
	}

	@Override
	public void update(World world, double dirX, double dirY, int delta) {
		super.update(world, dirX, dirY, delta);
		dialogueTimer(delta);
	}

	/**
	 * Counts elapsed time on display of dialogue box
	 * 
	 * @param delta
	 *            Time between friends
	 */
	private void dialogueTimer(int delta) {
		// Displays the dialogue through currentDialogue for an allocated amount
		// of time
		if (this.talkTimer < TALK_TIME) {
			this.talkTimer = this.talkTimer + delta;
		} else if (this.talkTimer != TALK_TIME) {
			this.talkTimer = TALK_TIME;
			currentDialogue = "";
		} else {
			currentDialogue = "";
		}
	}

	/**
	 * Dialogue of various villagers based on their abilities
	 * 
	 * @param world
	 *            Dependency on world object
	 * @param player
	 *            Dependency on player object
	 */
	public void talk(World world, Player player) {

		// Checks to see if village is still talking
		if (this.talkTimer == TALK_TIME) {
			// Switches based on name of villager being spoken to

			if (this.getName().equals(ELVIRA)) {
				if (player.getHP() == player.getMaxHP()) {
					currentDialogue = ELVIRA_HP_FULL;
				} else {
					player.setHP(player.getMaxHP());
					currentDialogue = ELVIRA_HP_HEALED;
				}
			}

			if (this.getName().equals(GARTH)) {
				player.getInventory();

				if (player.findItem(AMULET_ID) == ITEM_NOT_FOUND) {
					currentDialogue = GARTH_AMULET;
				} else if (player.findItem(SWORD_ID) == ITEM_NOT_FOUND) {
					currentDialogue = GARTH_SWORD;
				} else if (player.findItem(TOME_ID) == ITEM_NOT_FOUND) {
					currentDialogue = GARTH_TOME;
				} else {
					currentDialogue = GARTH_ALL;
				}
			}

			if (this.getName().equals(PRINCE) && this.talkTimer == TALK_TIME) {
				List<Entity> entity = world.getEntityList();
				boolean found = false;

				// Checks if elixir exists in the world
				for (Entity currentEntity : entity) {
					if (currentEntity instanceof Item) {
						if (((Item) currentEntity).getID() == ELIXIR_ID) {
							found = true;
						}
					}
				}

				// Checks to see if player has the elixer, takes it if he/she
				// does
				if (player.findItem(ELIXIR_ID) != ITEM_NOT_FOUND) {
					currentDialogue = PRINCE_HAS_ELIXIR;
					player.removeItem(ELIXIR_ID);
				} else if (!found) {
					currentDialogue = PRINCE_HAS_ELIXIR;
				} else {
					currentDialogue = PRINCE_NO_ELIXIR;
				}
			}

			talkTimer = 0;
		}
	}

	/**
	 * Gets dialogue of villager
	 * 
	 * @return Dialogue of villager
	 */
	public String getDialogue() {
		return this.currentDialogue;
	}

}
