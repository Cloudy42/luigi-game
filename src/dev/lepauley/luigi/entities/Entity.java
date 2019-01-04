package dev.lepauley.luigi.entities;

import java.awt.Graphics;
import java.awt.Rectangle;

import dev.lepauley.luigi.general.GVar;
import dev.lepauley.luigi.general.Handler;

/*
 * The base shell for all Entities in game
 */
public abstract class Entity {

	//Main Handler object (can reference game or anything from here)
	protected Handler handler;
	
	//Float = smoother movement using decimals for calculations
	//X and Y coordinates of entity
	protected float x, y;
	
	//Size of entity
	protected int width, height;
	
	//Boundary box for collision detection
	protected Rectangle bounds, collisionBoundsUp, collisionBoundsDown, collisionBoundsLeft, collisionBoundsRight;
	
	//Collision bounds size
	protected static final int DEFAULT_COLLISION_BOUNDS_SIZE = 3;
	
	//Constructor to set Defaults
	public Entity(Handler handler, float x, float y, int width, int height) {
		this.handler = handler;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		//By default, set boundary box to be exact size of entity
		//So 0,0 to start top left of entity, then same width/height
		bounds = new Rectangle(0, 0, width, height);
		collisionBoundsUp = new Rectangle(0, 0, width, height);
		collisionBoundsDown = new Rectangle(0, 0, width, height);
		collisionBoundsLeft = new Rectangle(0, 0, width, height);
		collisionBoundsRight = new Rectangle(0, 0, width, height);
		
	}
	
	//Updates Entity
	public abstract void tick();
	
	//Draws Entity
	public abstract void render(Graphics g);

	/*************** GETTERS and SETTERS ***************/

	//Gets x Position
	public float getX() {
		return x;
	}

	//Sets x Position
	public void setX(float x) {
		this.x = x;
	}

	//Gets y Position
	public float getY() {
		return y;
	}

	//Sets y Position
	public void setY(float y) {
		this.y = y;
	}

	//Gets entity width
	public int getWidth() {
		return width;
	}

	//Sets entity width
	public void setWidth(int width) {
		this.width = width;
	}

	//Gets entity height
	public int getHeight() {
		return height;
	}

	//Sets entity height
	public void setHeight(int height) {
		this.height = height;
	}
	
	//Gets collision bounds
	public Rectangle getBounds() {
		return bounds;
	}

	//Sets collision bounds
	public Rectangle setBounds(int x, int y, int width, int height) {
		return refreshBounds(new Rectangle(x, y, width, height));
	}
	
	//Refresh collision bounds
	public Rectangle refreshBounds(Rectangle bounds) {
		return new Rectangle(bounds.x * GVar.getMultiplier(), bounds.y * GVar.getMultiplier(),
				             bounds.width * GVar.getMultiplier(), bounds.height * GVar.getMultiplier());
	}
		

}
