package dev.lepauley.luigi.entities;

import java.awt.Graphics;

import dev.lepauley.luigi.GVar;

/*
 * The base shell for all Entities in game
 */
public abstract class Entity {

	//Float = smoother movement using decimals for calculations
	//Coordinates of entity
	protected float x, y;
	
	//Size of entity
	protected int width, height;
	
	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g);

	/*************** GETTERS and SETTERS ***************/
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
