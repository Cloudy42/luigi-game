package dev.lepauley.luigi;

import java.awt.Font;

import dev.lepauley.luigi.utilities.EnumSFX;

/*
 * Class of global variables to use throughout code:
 * 	- multiplier
 * 		Adjust size of all assets (e.g. tiles, entities, act like a zoom essentially)
 * 	- <placeholder variable name>
 * 		<PH description>
 */

/*
 * Locations that multiplier variable is used:
 * 1. 
 * 2. 
 */
public class GVar {

	//Manages how small and large entites can scale to
	public static final int MIN_SCALE = 1;
	public static final int MAX_SCALE = 5;
	
	//Manages game screen size
	public static final int GAME_WIDTH = 1050; //525
	public static final int GAME_HEIGHT = 470; //235
	
	//Font size shadow (we may want to move this later or redefine if we have more fonts, but currently this is where I'm putting it)
	//Defaut font
	public static final Font FONT_20 = new Font ("Lucida Sans Unicode", 1, 20);
	public static final Font FONT_30 = new Font ("Lucida Sans Unicode", 1, 30);
	public static final Font FONT_45 = new Font ("Lucida Sans Unicode", 1, 45);
	public static final Font FONT_70 = new Font ("Lucida Sans Unicode", 1, 70);
	public static final int FONT_20_SHADOW = 2;
	public static final int FONT_30_SHADOW = 3;
	public static final int FONT_45_SHADOW = 4;
	public static final int FONT_70_SHADOW = 7;
	
	//# of Players Selected
	private static int playerSelectCount = 1;
	
	//Multiplier for speed/scale/etc.
	private static int multiplier = 1;

	//Denotes whether debug mode is active or not
	private static boolean debugToggle = true;
	
	//Denotes whether game is paused or not
	private static boolean pauseToggle = false;

	//Denotes whether key manual is active or not
	private static boolean keyManualToggle = false;
	public static final int KEY_MANUAL_POSITION_X = 200;
	public static final int KEY_MANUAL_OFFSET_Y = 30;

	/*************** GETTERS and SETTERS ***************/
	
	public static int getMultiplier() {
		return multiplier;
	}

	public static void setMultiplier(int multiplier) {
		GVar.multiplier = multiplier;
	}
	
	public static void incrementMultiplier(int x) {
		multiplier += x;
	}

	public static int getPlayerSelectCount() {
		return playerSelectCount;
	}
	public static void setPlayerSelectCount(int no) {
		playerSelectCount = no;
		Game.gameAudio.pauseAudio("sfx");
		Game.gameAudio.playAudio("sfx", EnumSFX.Bump.toString());
	}
	
	public static boolean getDebug() {
		return debugToggle;
	}
	public static void toggleDebug() {
		if(debugToggle) 
			debugToggle = false;
		else
			debugToggle = true;
	}

	public static boolean getPause() {
		return pauseToggle;
	}
	public static void togglePause() {
		if(pauseToggle) { 
			pauseToggle = false;
			Game.gameAudio.pauseAudio("all");
			Game.gameAudio.playAudio("sfx", EnumSFX.Pause.toString());
			Game.gameAudio.resumeAudio("music");
		}
		else {
			pauseToggle = true;
			Game.gameAudio.pauseAudio("all");
			Game.gameAudio.playAudio("sfx", EnumSFX.Pause.toString());
		}
	}	
	
	public static boolean getKeyManual() {
		return keyManualToggle;
	}
	public static void toggleKeyManual() {
		if(keyManualToggle) 
			keyManualToggle = false;
		else
			keyManualToggle = true;
	}

}
