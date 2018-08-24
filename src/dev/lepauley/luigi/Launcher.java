package dev.lepauley.luigi;

import dev.lepauley.luigi.display.Display;

/*
 * Responsible for starting game
 */

public class Launcher {

	public static void main(String[] args) {
		//Starts new game with underlying display built in 
		new Game("I'm uh Luigi! I'm uh gonna ween!", 450, 450);
	}
}
