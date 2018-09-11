package dev.lepauley.luigi;

/*
 * Responsible for starting game
 */

public class Launcher {

	public static void main(String[] args) {
		//Starts new game with underlying display built in 
		Game game = new Game("Business is coming along! Get pumped!", 650, 450);
		
		//Start game! Run, initialize, game loop
		game.start();
	}
}
