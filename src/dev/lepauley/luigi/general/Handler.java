package dev.lepauley.luigi.general;

import dev.lepauley.luigi.gfx.GameCamera;
import dev.lepauley.luigi.input.KeyManager;
import dev.lepauley.luigi.levels.Level;
import dev.lepauley.luigi.states.GameState;

/*
 * Use to pass around a bunch of different game objects for use in other classes
 * 		But does so just by passing this handler object, which then has access to everything
 * Predominantly a class of getters (and some setters)
 */

public class Handler {

	private Game game;
	
	//Level Array, which holds multiple individual levels
	private Level[] level;
	
	public Handler(Game game) {
		this.game = game;
	}
	
	/*************** GETTERS and SETTERS ***************/
	
	public GameCamera getGameCamera() {
		return game.getGameCamera();
	}
	
	public KeyManager getKeyManager() {
		return game.getKeyManager();
	}
	public int getWidth() {
		return game.getWidth();
	}
	
	public int getHeight() {
		return game.getHeight();
	}

	public Game getGame() {
		return game;
	}

	public Level[] getLevel() {
		return level;
	}
	
	//Return specifically chosen level
	public Level getSpecificLevel() {
		return ((GameState) Game.getGameState()).getLevel();
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void setLevel(Level[] level) {
		this.level = level;
	}
	
	
}
