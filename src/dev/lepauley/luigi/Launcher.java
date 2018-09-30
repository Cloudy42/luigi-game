package dev.lepauley.luigi;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/*
 * Responsible for starting game
 */

public class Launcher {

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		//Starts new game with underlying display built in 
		Game game = new Game("2018.09.29 - Waruigi, I'mma gonna ween!!", GVar.GAME_WIDTH, GVar.GAME_HEIGHT);
		//Start game! Run, initialize, game loop
		game.start();
	}
}
