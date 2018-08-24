package dev.lepauley.luigi;

import dev.lepauley.luigi.display.Display;

/*
 * Main class for game - holds all base code: 
 *     1.) Starts Game
 *     2.) Runs Game
 *     3.) Closes Game
 */

public class Game {

	private Display display;
	
	public int width, height;
	
	public Game(String title, int width, int height) {
		this.width = width;
		this.height = height;
		
		//Sets display for Game instance
		display = new Display(title, width, height);
	}
	
}
