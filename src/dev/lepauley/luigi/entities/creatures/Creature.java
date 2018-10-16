package dev.lepauley.luigi.entities.creatures;

import dev.lepauley.luigi.entities.Entity;
import dev.lepauley.luigi.general.Game;

/*
 * The base shell for all Creatures in game
 */
public abstract class Creature extends Entity {

	//Default Creature Values
	public static final int DEFAULT_HEALTH = 10;
	public static final float DEFAULT_SPEED = 3.0f;	
	public static final int DEFAULT_CREATURE_WIDTH = 32, DEFAULT_CREATURE_HEIGHT_BIG = 64, DEFAULT_CREATURE_HEIGHT_SMALL = 32;
	
	//Tracks how much HP and speed creature has
	protected int health;
	protected float speed;

	//Helper for moving creatures on x and y plane
	protected float xMove, yMove;
	
	//Creature Constructor. Establishes some defaults
	public Creature(Game game, float x, float y, int width, int height) {
		super(game, x,y, width, height);
		health = DEFAULT_HEALTH;
		speed = DEFAULT_SPEED;
		xMove = 0;
		yMove = 0;
	}

	//Moves creature using helpers
	public void move() {
		x += xMove;
		y += yMove;
	}
	
	/*************** GETTERS and SETTERS ***************/

	//Gets creature hp
	public int getHealth() {
		return health;
	}

	//Sets creature hp
	public void setHealth(int health) {
		this.health = health;
	}

	//Gets creature speed
	public float getSpeed() {
		return speed;
	}

	//Sets creature speed
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	//Gets creature xMovement
	public float getxMove() {
		return xMove;
	}

	//Sets creature xMovement
	public void setxMove(float xMove) {
		this.xMove = xMove;
	}

	//Gets creature yMovement
	public float getyMove() {
		return yMove;
	}

	//Sets creature yMovement
	public void setyMove(float yMove) {
		this.yMove = yMove;
	}
		
}
