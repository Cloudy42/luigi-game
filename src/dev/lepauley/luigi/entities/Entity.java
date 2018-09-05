package dev.lepauley.luigi.entities;

import java.awt.Graphics;

import dev.lepauley.luigi.GlobalVariables;

/*
 * The base shell for all Entities in game
 */
public abstract class Entity {

	protected static int multiplier = GlobalVariables.multiplier;
	
	//Float = smoother movement using decimals for calculations
	//Coordinates of entity
	protected float x, y;
	
	public Entity(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g);
	
}
