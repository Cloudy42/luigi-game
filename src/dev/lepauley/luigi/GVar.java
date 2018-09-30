package dev.lepauley.luigi;

import java.awt.Font;

import dev.lepauley.luigi.states.StateManager;
import dev.lepauley.luigi.utilities.EnumPause;
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

	//Target FPS
	public static final int FPS_MIN = 10;
	public static final int FPS_MAX = 150;
	public static final int FPS_DEFAULT = 60;
	public static int FPS = FPS_DEFAULT;
	
	//Default font
	public static final String fontA = "Lucida Sans Unicode";
	
	//Message To Player (Pause, Game Over, etc.)
	private static String pauseMsg; 
	private static int pauseMsgLen;	

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
		FPS = FPS_DEFAULT;
		debugToggle = true;
		pauseToggle = false;		
		keyManualToggle = false;
		pauseMsg = "PAUSED";
		pauseMsgLen = pauseMsg.length();
	}
	
	public static int getShadowFont(int currentFontSize) {
		return currentFontSize / 10;
	}
	
	/*************** GETTERS and SETTERS ***************/
	
	public static Font setFont(String font, int size) {
		return new Font(font, 1, size);
	}

	//Note, I didn't set 0 FPS since it will lock game, and I didn't do below 10 since that makes input very laggy and such.
	//10 felt like a solid number to work with.
	public static void setFPS(int i) {
		FPS = i;
		
		//Only Print this is greater than the minimum or is less than the maximum
		if(FPS >= FPS_MIN && FPS <= FPS_MAX)
			System.out.println("FPS: " + FPS);
		
		//If Pause Message = "STOP" and THEN you increase speed, it will resume
		if(pauseMsg.equals(EnumPause.STOP.toString()) && FPS > FPS_MIN){
			togglePause(EnumPause.RESUME.toString());
		} 
		//If you decrease FPS BELOW the min, it will "STOP" the game
		if(FPS < FPS_MIN) {
			FPS = FPS_MIN;
			//This check makes it so it will only print STOPPED to console once
			if(!getPause())
				System.out.println("FPS: STOPPED!");
			togglePause(EnumPause.STOP.toString());
		//If increase FPS ABOVE the max, it will lock at the max
		} else if(FPS > FPS_MAX) {
			FPS = FPS_MAX;	
		}
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
		return pauseMsg;
	}
	public static void setPauseMsg(String newMsg) {
		pauseMsg = newMsg;
		pauseMsgLen = pauseMsg.length();
	}
	public static int getPauseMsgLen() {
		return pauseMsgLen;
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
	public static void togglePause(String msg) {
		setPauseMsg(msg);
		//if Pause Message == STOP, stop time (if not currently paused)
		if(msg.equals(EnumPause.STOP.toString())){
			if(!pauseToggle && StateManager.getCurrentStateName() == "GameState") {
				setPauseMsg("");
				pauseToggle = true;
			}
		//If Pause Message == RESUME, then unpause game
		} else if(msg.equals(EnumPause.RESUME.toString())){
			if(pauseToggle && StateManager.getCurrentStateName() == "GameState") {
				setPauseMsg("");
				pauseToggle = false;
			}
		//Else do normal pause rules:
		} else {
			//if Game is paused and you're not dead, unpause and resume audio
			if(pauseToggle && !Game.gameHeader.getDead()) { 
				pauseToggle = false;
				Game.gameAudio.pauseAudio("all");
				Game.gameAudio.playAudio("sfx", EnumSFX.Pause.toString());
				Game.gameAudio.resumeAudio("music");
			}
			//Else pause game (Note: Game should already be paused if dead, so that check doesn't matter)
			else {
				pauseToggle = true;
				Game.gameAudio.pauseAudio("all");
				Game.gameAudio.playAudio("sfx", EnumSFX.Pause.toString());
			}
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
