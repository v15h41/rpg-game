
/* SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: Vishal Egbert
 */

import org.newdawn.slick.SlickException;

/**
 * Represents the camera that controls our viewpoint.
 */
public class Camera {

	public static final int TILE_DIMENSION = 72;

	/** The unit this camera is following */
	private Unit unitFollow;

	/** The camera's x-coordinate in the world */
	private int xPos;
	/** The camera's y-coordinate in the world */
	private int yPos;

	/** The camera's x-coordinate in the world, in terms of tile position. */
	private int xTile;
	/** The camera's y-coordinate in the world, in terms of tile position. */
	private int yTile;

	/**
	 * Calculate offset between tile and absolute pixel position of camera for
	 * the x-axis.
	 * 
	 * @return offset value between tile and absolute pixel position
	 */
	public int getOffsetXPos() {
		return (xTile * TILE_DIMENSION) - this.xPos;
	}

	/**
	 * Calculate offset between tile and absolute pixel position of camera for
	 * the y-axis.
	 * 
	 * @return offset value between tile and absolute pixel position
	 */
	public int getOffsetYPos() {
		return (yTile * TILE_DIMENSION) - this.yPos;
	}

	/**
	 * Gets current camera position in the x-axis in terms of tile position.
	 * 
	 * @return current camera position in the x-axis in terms of tile position
	 */
	public int getXTile() {
		return this.xTile;
	}

	/**
	 * Gets current camera position in the y-axis in terms of tile position.
	 * 
	 * @return current camera position in the y-axis in terms of tile position
	 */
	public int getYTile() {
		return this.yTile;
	}

	/**
	 * Gets current camera position in the x-axis.
	 * 
	 * @return current camera position in the x-axis
	 */
	public int getXPos() {
		return this.xPos;
	}

	/**
	 * Gets current camera position in the y-axis.
	 * 
	 * @return current camera position in the y-axis
	 */
	public int getYPos() {
		return this.yPos;
	}

	/**
	 * Creates a new Camera object and makes it follow the player by default.
	 */
	public Camera(Unit player) {
		this.followUnit(player);
	}

	/**
	 * Update the game camera to re-center its viewpoint around the player.
	 */
	public void update() throws SlickException {
		// Calculates absolute pixel position and tile position of camera.
		this.xPos = (int) getCenterViewX();
		this.yPos = (int) getCenterViewY();

		this.xTile = calculateXTile(xPos);
		this.yTile = calculateYTile(yPos);
	}

	/**
	 * Calculates tile x-coordinate.
	 */
	private int calculateXTile(int xPos) {
		return xPos / TILE_DIMENSION;
	}

	/**
	 * Calculates tile y-coordinate.
	 */
	private int calculateYTile(int yPos) {
		return yPos / TILE_DIMENSION;
	}

	/**
	 * Calculates absolute camera x-coordinate relative to player such that the
	 * camera is centered.
	 * 
	 * @return camera's absolute x-coordinate on the map
	 */
	private double getCenterViewX() {
		double x = unitFollow.getPosX();

		return x - (RPG.SCREEN_WIDTH / 2);
	}

	/**
	 * Calculates absolute camera x-coordinate relative to player such that the
	 * camera is centered.
	 * 
	 * @return camera's absolute x-coordinate on the map
	 */
	private double getCenterViewY() {
		double y = unitFollow.getPosY();

		return y - ((RPG.SCREEN_HEIGHT - RPG.PANEL_HEIGHT) / 2);
	}

	/**
	 * Assigns follow unit attribute to the unit that the camera should be
	 * following.
	 */
	public void followUnit(Object playerUnit) {
		if (playerUnit instanceof Player) {
			this.unitFollow = (Player) playerUnit;
		}
	}

	/**
	 * Get unit's x-coordinate relative to the screen
	 * 
	 * @return unit's x-coordinate
	 */
	public int getRelativeX(double x) {
		return (int) x - this.xPos;
	}

	/**
	 * Get unit's y-coordinate relative to the screen
	 * 
	 * @return unit's y-coordinate
	 */
	public int getRelativeY(double y) {
		return (int) y - this.yPos;
	}
}