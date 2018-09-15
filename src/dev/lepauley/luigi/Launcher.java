package dev.lepauley.luigi;

/*
 * Responsible for starting game
 */

public class Launcher {

	public static void main(String[] args) {
		//Starts new game with underlying display built in 
		Game game = new Game("2018.09.15 - Waruigi, I'mma gonna ween!!", 1050, 470);
		
		//Start game! Run, initialize, game loop
		game.start();
	}
}
