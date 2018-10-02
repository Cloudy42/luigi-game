package dev.lepauley.luigi.states;

import java.awt.Graphics;

import dev.lepauley.luigi.general.Game;

/*
 * Base class to handle all game states
 * For example game (main game play), menu, etc.
 */
public abstract class State {
	
	//We want an instance of our game class in all our state classes
	protected Game game;
	
	//Keeps Track of state's name so we can do equalities
	protected String currentStateName;
	
	//Constructor
	public State(Game game, String currentStateName) {
		
		//Sets current game
		this.game = game;
		
		//Sets current state name
		this.currentStateName = currentStateName;
	}

	//Unused atm
	public abstract void tick();
	public abstract void render(Graphics g);
	
}
