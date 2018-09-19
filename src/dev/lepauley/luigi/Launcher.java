package dev.lepauley.luigi;

/*
 * Responsible for starting game
 */

public class Launcher {

	public static void main(String[] args) {
		//Starts new game with underlying display built in 
		Game game = new Game("2018.09.19 - Waruigi, I'mma gonna ween!!", GVar.GAME_WIDTH, GVar.GAME_HEIGHT);
		
		//Start game! Run, initialize, game loop
		game.start();
	}
}
