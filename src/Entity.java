import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Represents an entity object from which both units and items will inherit
 * from.
 *
 */

public abstract class Entity {

	/** Stores the unit's location on the world's x-axis. */
	private double posX;
	/** Stores the unit's location on the world's y-axis. */
	private double posY;

	/** Stores the entitiy's index number in the ArrayList. */
	private int index;

	/** Stores the unit's sprite. */
	private Image sprite;

	/**
	 * Gets entity's x-coordinate in the world.
	 * 
	 * @return entity's x-coordinate
	 */
	public double getPosX() {
		return posX;
	}

	/**
	 * Gets entity's y-coordinate in the world.
	 * 
	 * @return entity's y-coordinate
	 */
	public double getPosY() {
		return posY;
	}

	/**
	 * Sets entity's x-coordinate in the world.
	 * 
	 */
	public void setPosX(double posX) {
		this.posX = posX;
	}

	/**
	 * Sets entity's y-coordinate in the world.
	 * 
	 */
	public void setPosY(double posY) {
		this.posY = posY;
	}

	/**
	 * Sets the entity's index in the ArrayList that it is stored in.
	 * 
	 * @param index
	 *            Index of entity
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return this.index;
	}

	/**
	 * Sets entity's sprite.
	 * 
	 * @param filePath
	 *            The file path that leads to the sprite asset
	 * @throws SlickException
	 */
	public void setSprite(String sprite) throws SlickException {
		this.sprite = new Image(sprite);
	}

	/**
	 * Gets entity's sprite.
	 * 
	 * @return Returns entity's sprite in the form of an Image
	 */
	public Image getSprite() {
		return this.sprite;
	}

	/**
	 * Renders entity on the game screen.
	 * 
	 * @param g
	 *            Standard graphics variable
	 * @param x
	 *            x-coordinate of entity on game screen
	 * @param y
	 *            x-coordinate of entity on game screen
	 */
	public void renderEntity(Graphics g, int x, int y) {
		sprite.drawCentered(x, y);
	}

}
