
/**
 * Represents the abstract form of a monster object and handles the AI that is
 * common to both types of monsters
 *
 */

public abstract class Monster extends Unit {

	// Constants that affect the movement of the AI
	public static final int PASSIVE_AI = -1;
	public static final int AGGRESSIVE_AI = 1;

	/**
	 * Chooses suitable x and y movement based on the current position of the
	 * player relative to the monster
	 * 
	 * @param posX
	 *            Position of the player in the x axis
	 * @param posY
	 *            Position of the player in the y axis
	 * @param distance
	 *            Distance between the player and the monster
	 * @param aggressive
	 *            1 if aggressive and -1 if passive
	 * @return An array with the dirX and dirY of the monster
	 */
	public double[] AI(double posX, double posY, double distance, int aggressive) {
		double a = posX - this.getPosX();
		double b = posY - this.getPosY();

		double[] movement = { aggressive * a / distance, aggressive * b / distance };
		return movement;
	}

}