import org.newdawn.slick.SlickException;

/**
 * Represents all items in the game and stores their ID and their effect on the
 * player.
 *
 */

public class Item extends Entity {

	// Item ID constants
	public static final int AMULET_ID = 0;
	public static final int SWORD_ID = 1;
	public static final int TOME_ID = 2;
	public static final int ELIXIR_ID = 3;
	
	/** Amulet HP buff */
	public static final int AMULET_BUFF = 80;
	/** Sword damage buff */
	public static final int SWORD_BUFF = 30;
	/** Tome cooldown buff */
	public static final int TOME_BUFF = 300;

	/** Unique identifier for each item in the game */
	private int ID;

	/**
	 * Initializes the item object with necessary attributes
	 * 
	 * @param posX
	 *            Position of the item on the world's x-axis
	 * @param posY
	 *            Position of the item on the world's y-axis
	 * @param sprite
	 *            File path of item sprite
	 * @param ID
	 *            Unique identifier for given item
	 * @throws SlickException
	 */
	public Item(int posX, int posY, String sprite, int ID) throws SlickException {
		this.setPosX(posX);
		this.setPosY(posY);
		this.setSprite(sprite);
		this.ID = ID;
	}

	/**
	 * Gets unique identifier for the item
	 * 
	 * @return Unique identifier
	 */
	public int getID() {
		return this.ID;
	}

	/**
	 * Adds collected item to the inventory of the player
	 * 
	 * @param player
	 *            Dependency on the player object
	 */
	public void itemCollected(Player player) {
		player.getInventory().add(this);

		// Based on the ID of the item, there will be a different effect
		switch (this.ID) {
		case AMULET_ID:
			player.setMaxHP(player.getMaxHP() + AMULET_BUFF);
			player.setHP(player.getHP() + AMULET_BUFF);
			break;
		case SWORD_ID:
			player.setDamage(player.getDamage() + SWORD_BUFF);
			break;
		case TOME_ID:
			player.setCooldown(player.getCooldown() - TOME_BUFF);
			break;
		case ELIXIR_ID:
			break;
		}
	}

}
