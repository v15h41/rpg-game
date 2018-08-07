import org.newdawn.slick.SlickException;

/**
 * Represents the object of the aggressive monster and handles its movement when
 * it is near a player
 * 
 */

public class AggressiveMonster extends Monster {

	/** Speed of the monster */
	public static final float AGGRESSIVE_MONSTER_SPEED = 0.25f;
	/** Required range of monster to player in order for it to walk */
	public static final int WALK_RANGE = 150;
	/** Required range of monster to player in order for it to attack it */
	public static final int ATTACK_RANGE = 50;

	/**
	 * Initializes the aggressive monster object with necessary attributes
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
	public AggressiveMonster(double posX, double posY, String sprite, int maxHP, int damage, int cooldown, String name)
			throws SlickException {
		this.setSprite(sprite);
		this.setMaxHP(maxHP);
		this.setCooldown(cooldown);
		this.setPosX(posX);
		this.setSpeed(AGGRESSIVE_MONSTER_SPEED);
		this.setPosY(posY);
		this.setName(name);
		this.setHP(maxHP);
		this.setDamage(damage);
		this.setCooldownTime(cooldown);
	}

	@Override
	public void update(World world, double dirX, double dirY, int delta) {
		// Updates the movement of the monster when it is close to a player
		double movement[] = chasePlayer(world);

		super.update(world, movement[0], movement[1], delta);
	}

	/**
	 * Calculates the movement direction required in order to chase the player
	 * when it is close by and attack it when it is close enough
	 * 
	 * @param world
	 *            Dependency on the world object
	 * @return An array with the dirX and dirY of the monster
	 */
	private double[] chasePlayer(World world) {
		double movement[] = { 0, 0 };

		Player player = world.getPlayer();
		double distance = world.getDistance(this.getPosX(), this.getPosY(), player.getPosX(), player.getPosY());

		// If the player is in walking range, the monster will chase it. If the
		// player is in attack range, the monster will attack it
		if (distance <= WALK_RANGE && distance > ATTACK_RANGE) {
			movement = AI(player.getPosX(), player.getPosY(), distance, AGGRESSIVE_AI);
		} else if (distance <= ATTACK_RANGE) {
			player.onAttack(world, this.attack());
		}

		return movement;
	}

}
