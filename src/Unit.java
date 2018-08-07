import java.util.Random;

import org.newdawn.slick.Graphics;

/**
 * Represents a unit object from which the player, monsters and NPCs inherit
 * from
 *
 */

public abstract class Unit extends Entity {

	/** Boolean value when the player is facing left. */
	public static final boolean DIR_LEFT = true;
	/** Boolean value when the player is facing right. */
	public static final boolean DIR_RIGHT = false;

	/** Default unit direction */
	private boolean unitFacing = DIR_RIGHT;

	/** Stores the unit's cooldown timer. */
	private int cooldown;
	/** Stores the unit's max HP. */
	private int maxHP;
	/** Stores the unit's base HP. */
	private int HP;
	/** Stores the unit's speed. */
	private float speed;
	/** Stores the unit's damage. */
	private int damage;
	/** Stores the unit's name. */
	private String name;
	/** Stores the unit's current cooldown timer. */
	private int cooldownTime;

	/** Reduces the unit's HP by a certain amount of damage points. */
	public void takeDamage(int damage) {
		this.HP = this.HP - damage;
	}

	/**
	 * Sets the unit's name
	 * 
	 * @param name
	 *            Name of the unit
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the unit's name
	 * 
	 * @return Name of the unit
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the speed of the unit
	 * 
	 * @return The speed of the unit
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * Sets the speed of the unit
	 * 
	 * @param speed
	 *            The speed of the unit
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * Gets the cooldown of the unit
	 * 
	 * @return The cooldown of the unit
	 */
	public int getCooldown() {
		return cooldown;
	}

	/**
	 * Sets the cooldown of the unit
	 * 
	 * @param cooldown
	 *            The cooldown of the unit
	 */
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	/**
	 * Gets the max HP of the unit
	 * 
	 * @return The max HP of the unit
	 */
	public int getMaxHP() {
		return maxHP;
	}

	/**
	 * Sets the max HP of the unit
	 * 
	 * @param maxHP
	 *            The max HP of the unit
	 */
	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	/**
	 * Gets the HP of the unit
	 * 
	 * @return HP The HP of the unit
	 */
	public int getHP() {
		return HP;
	}

	/**
	 * Sets the HP of the unit
	 * 
	 * @param HP
	 *            The HP of the unit
	 */
	public void setHP(int HP) {
		this.HP = HP;
	}

	/**
	 * Sets the damage of the unit
	 * 
	 * @param damage
	 *            The damage of the unit
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * Gets the damage of the unit
	 * 
	 * @return The damage of the unit
	 */
	public int getDamage() {
		return this.damage;
	}

	/**
	 * Updates the unit position every time it is called
	 * 
	 * @param world
	 *            An instance of world that allows the Unit to detect obstacles
	 * 
	 * @param dirX
	 *            Flag to indicate positive or negative movement in the x-axis
	 * 
	 * @param dirY
	 *            Flag to indicate positive or negative movement in the y-axis
	 * 
	 * @param delta
	 *            Number of milliseconds passed between frames
	 */
	public void update(World world, double dirX, double dirY, int delta) {
		move(world, dirX, dirY, delta);
		flipUnit(dirX);
		checkCooldown(delta);
	}

	/**
	 * Determines the next position of the given unit such that it does not walk
	 * into any obstacles
	 * 
	 * @param world
	 *            Dependency on the world object
	 * @param dirX
	 *            Movement in the x-axis
	 * @param dirY
	 *            Movement in the y-axis
	 * @param delta
	 *            Time between frames
	 */
	private void move(World world, double dirX, double dirY, int delta) {
		// Calculates the unit's potential position
		double tempX = newUnitLocationX(this.getPosX(), dirX, delta);
		double tempY = newUnitLocationY(this.getPosY(), dirY, delta);

		// Checks for obstacle in potential direction of movement
		if (world.isBlocked(tempX, this.getPosY())) {
			dirX = 0;
		}

		if (world.isBlocked(this.getPosX(), tempY)) {
			dirY = 0;
		}

		// Sets unit's new position
		this.setPosX(newUnitLocationX(this.getPosX(), dirX, delta));
		this.setPosY(newUnitLocationY(this.getPosY(), dirY, delta));
	}

	/**
	 * Flips unit's sprite based on its movement in the x-axis
	 * 
	 * @param dirX
	 *            Movement in the x-axis
	 */
	private void flipUnit(double dirX) {
		if (dirX < 0)
			this.unitFacing = DIR_LEFT;
		else
			this.unitFacing = DIR_RIGHT;

	}

	/**
	 * Updarts cooldown of unit
	 * 
	 * @param delta
	 *            Time between frames
	 */
	private void checkCooldown(int delta) {
		if (this.cooldownTime < this.cooldown) {
			this.cooldownTime = this.cooldownTime + delta;
		} else if (this.cooldownTime != this.cooldown) {
			this.cooldownTime = this.cooldown;
		}
	}

	/**
	 * Calculates unit's new location in the world on the x-axis.
	 * 
	 * @param unitPosX
	 *            Unit's current x-coordinate
	 * @param dirX
	 *            Direction of unit's movement
	 * @param delta
	 *            Time in milliseconds between frames
	 * @return unit's anticipated location on the x-axis
	 */
	private double newUnitLocationX(double unitPosX, double dirX, int delta) {
		return unitPosX + dirX * delta * this.speed;
	}

	/**
	 * Calculates unit's new location in the world on the y-axis.
	 * 
	 * @param unitPosY
	 *            Unit's current y-coordinate
	 * @param dirY
	 *            Direction of unit's movement
	 * @param delta
	 *            Time in milliseconds between frames
	 * @return unit's anticipated location on the y-axis
	 */
	private double newUnitLocationY(double unitPosY, double dirY, int delta) {
		return unitPosY + dirY * delta * this.speed;
	}

	@Override
	public void renderEntity(Graphics g, int x, int y) {
		// Flips the sprite if it is a unit
		this.getSprite().getFlippedCopy(unitFacing, false).drawCentered(x, y);
	}

	/**
	 * Takes damage if the unit is attacked
	 * 
	 * @param world
	 *            Dependency on the world object
	 * @param damage
	 *            Amount of damage dealt
	 */
	public void onAttack(World world, int damage) {
		takeDamage(damage);

		// Remove the object from the world if it is dead
		if (this.getHP() < 1) {
			world.remove(this);
		}
	}

	/**
	 * Deals a random amount of damage to a unit
	 * 
	 * @return Amount of damage dealt
	 */
	public int attack() {
		Random ran = new Random();

		// Attacks only if cooldown time is elapsed
		if (this.cooldownTime == this.cooldown) {
			this.cooldownTime = 0;
			return ran.nextInt(this.getDamage() + 1);
		}

		return 0;
	}

	/**
	 * Get cooldown time of the unit
	 * 
	 * @return Cooldown time
	 */
	public int getCooldownTime() {
		return this.cooldownTime;
	}

	/**
	 * Sets cooldown time of the unit
	 * 
	 * @param cooldownTime
	 *            Cooldown time
	 */
	public void setCooldownTime(int cooldownTime) {
		this.cooldownTime = cooldownTime;
	}
}
