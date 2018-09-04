package dev.lepauley.luigi.states;

import java.awt.Graphics;

/*
 * Base class to handle all game states
 * For example game (main game play), menu, pause? etc.
 */
public abstract class State {
	
	public abstract void tick();
	public abstract void render(Graphics g);
	
}
