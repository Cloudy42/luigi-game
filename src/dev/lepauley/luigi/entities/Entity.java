package dev.lepauley.luigi.entities;

import java.awt.Graphics;

/*
 * The base shell for all Entities in game
 */
public abstract class Entity {

	//Float = smoother movement using decimals for calculations
	protected float x, y;
	
	public Entity(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g);
	
}
