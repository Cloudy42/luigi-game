package dev.lepauley.luigi.entities;

import java.awt.Graphics;

/*
 * The base shell for all Entities in game
 */
public abstract class Entity {

	//Float = smoother movement using decimals for calculations
	//X and Y coordinates of entity
	protected float x, y;
	
	//Size of entity
	protected int width, height;
	
	//Constructor to set Defaults
	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
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

}
