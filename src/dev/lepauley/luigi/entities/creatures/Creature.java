package dev.lepauley.luigi.entities.creatures;

import dev.lepauley.luigi.entities.Entity;

/*
 * The base shell for all Creatures in game
 */
public abstract class Creature extends Entity {

	protected int health;
	
	public Creature(float x, float y) {
		super(x,y);
		health = 10;
	}
	
}
