package dev.lepauley.luigi;

//New comment just to test local and remote will sync up
/*
 * Responsible for starting game
 */

public class Launcher {

	public static void main(String[] args) {
		//Starts new game with underlying display built in 
		Game game = new Game("I'm uh Luigi! I'm uh gonna ween!", 450, 450);
		
		//Start game! Run, initialize, game loop
		game.start();
	}
}
