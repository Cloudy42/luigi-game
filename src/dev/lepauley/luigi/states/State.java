package dev.lepauley.luigi.states;

import java.awt.Graphics;

import dev.lepauley.luigi.Game;

/*
 * Base class to handle all game states
 * For example game (main game play), menu, pause? etc.
 */
public abstract class State {
	
	//We want an instance of our game class in all our state classes
	protected Game game;
	
	public State(Game game) {
		this.game = game;
	}
	public abstract void tick();
	public abstract void render(Graphics g);
	
}
