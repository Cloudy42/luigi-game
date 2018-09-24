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
	
	//Manages keyManual positioning:
	public static final int KEY_MANUAL_POSITION_X = 200;
	public static final int KEY_MANUAL_OFFSET_Y = 30;

	//Default font
	public static final String fontA = "Lucida Sans Unicode";
	
	//Message To Player (Pause, Game Over, etc.)
	private static String playerMsg; 
	private static int playerMsgLen;	

	//# of Players Selected
	private static int playerSelectCount;
	
	//Multiplier for speed/scale/etc.
	private static int multiplier;

	//Denotes whether debug mode is active or not
	private static boolean debugToggle;
	
	//Denotes whether game is paused or not
	private static boolean pauseToggle;

	//Denotes whether key manual is active or not
	private static boolean keyManualToggle;

	public static void resetGVarDefaults() {
		playerSelectCount = 1;
		multiplier = 1;
		debugToggle = true;
		pauseToggle = false;		
		keyManualToggle = false;
		playerMsg = "PAUSED";
		playerMsgLen = playerMsg.length();
	}
	
	public static int getShadowFont(int currentFontSize) {
		return currentFontSize / 10;
	}
	
	/*************** GETTERS and SETTERS ***************/
	
	public static Font setFont(String font, int size) {
		return new Font(font, 1, size);
	}
	
	public static int getMultiplier() {
		return multiplier;
	}

	public static void setMultiplier(int multiplier) {
		GVar.multiplier = multiplier;
	}
	
	public static void incrementMultiplier(int x) {
		multiplier += x;
	}

	public static String getPauseMsg() {
		return playerMsg;
	}
	public static void setPauseMsg(String newMsg) {
		playerMsg = newMsg;
		playerMsgLen = playerMsg.length();
	}
	public static int getPauseMsgLen() {
		return playerMsgLen;
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
		if(pauseToggle && !Game.gameHeader.getDead()) { 
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
