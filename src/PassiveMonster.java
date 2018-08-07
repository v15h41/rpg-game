import java.util.Random;

import org.newdawn.slick.SlickException;

/**
 * Represents the object of the passive monster and handles its movement and
 * reaction to danger
 * 
 */

public class PassiveMonster extends Monster {

	/** Speed of the monster */
	public static final float PASSIVE_MONSTER_SPEED = 0.2f;
	/** Constant required for random movement */
	public static final int RANDOM_MOVEMENT_CONSTANT = 3;

	/** Time allocated before the monster changes its direction of wandering */
	public static final int WANDER_TIME = 3000;
	/** Time allocated before the monster feels safe again */
	public static final int SAFE_TIME = 5000;

	/** Current time elapsed wandering */
	private int wanderTime;
	/** Current time elapsed wandering */
	private int safeTime;
	/** Direction of movement in the x-axis when wandering */
	private int wanderX;
	/** Direction of movement in the y-axis when wandering */
	private int wanderY;

	/**
	 * Initializes the passive monster object with necessary attributes
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
	public PassiveMonster(double posX, double posY, String sprite, int maxHP, int damage, int cooldown, String name)
			throws SlickException {
		this.setSprite(sprite);
		this.setMaxHP(maxHP);
		this.setCooldown(cooldown);
		this.setPosX(posX);
		this.setPosY(posY);
		this.setSpeed(PASSIVE_MONSTER_SPEED);
		this.setName(name);
		this.setHP(maxHP);
		this.setCooldownTime(cooldown);
		this.wanderTime = WANDER_TIME;
		this.safeTime = SAFE_TIME;
	}

	@Override
	public void update(World world, double dirX, double dirY, int delta) {
		double[] movement;

		// If the monster is not safe, it will run away otherwise it will wander
		if (this.safeTime < SAFE_TIME) {
			movement = runAway(world);
			this.safeTime = this.safeTime + delta;
		} else if (this.safeTime != SAFE_TIME) {
			this.safeTime = SAFE_TIME;
			movement = wander(delta);
		} else {
			movement = wander(delta);
		}

		super.update(world, movement[0], movement[1], delta);
	}

	/**
	 * Calculates the movement direction required in order to run away from the
	 * attacking player
	 * 
	 * @param world
	 *            Dependency on the world object
	 * @return An array with the dirX and dirY of the monster
	 * 
	 */
	private double[] runAway(World world) {
		Player player = world.getPlayer();
		double distance = world.getDistance(this.getPosX(), this.getPosY(), player.getPosX(), player.getPosY());

		double[] movement = AI(player.getPosX(), player.getPosY(), distance, PASSIVE_AI);

		return movement;
	}

	/**
	 * Decides the random movement of the passive monster as it roams around the
	 * world
	 * 
	 * @param delta
	 *            Time in milliseconds between frames
	 * @return An array with the dirX and dirY of the monster
	 */
	private double[] wander(int delta) {
		double movement[] = { 0, 0 };
		Random ran = new Random();

		// Wander in a different direction after a specifed time interval
		if (this.wanderTime < WANDER_TIME) {
			movement[0] = wanderX;
			movement[1] = wanderY;

			this.wanderTime = this.wanderTime + delta;
		} else {
			this.wanderTime = 0;

			movement[0] = ran.nextInt(RANDOM_MOVEMENT_CONSTANT) - 1;
			movement[1] = ran.nextInt(RANDOM_MOVEMENT_CONSTANT) - 1;

			wanderX = (int) movement[0];
			wanderY = (int) movement[1];
		}

		return movement;

	}

	@Override
	public void onAttack(World world, int damage) {
		// On attack, the monster will reset its safety timer
		this.safeTime = 0;
		super.onAttack(world, damage);
	}
}
