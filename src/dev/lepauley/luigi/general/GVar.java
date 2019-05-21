package dev.lepauley.luigi.general;

import static dev.lepauley.luigi.utilities.Utilities.print;
import static dev.lepauley.luigi.utilities.Utilities.printnb;

import java.awt.Font;
import java.io.File;

import dev.lepauley.luigi.states.GameState;
import dev.lepauley.luigi.states.StateManager;
import dev.lepauley.luigi.utilities.EnumFont;
import dev.lepauley.luigi.utilities.EnumSFX;
import dev.lepauley.luigi.utilities.FontManager;
import dev.lepauley.luigi.utilities.Utilities;

/*
 * Class of global variables to use throughout code:
 */

public class GVar {

	//Manages how small/large entities can scale to
	public static final int MIN_SCALE = 1;
	public static final int MAX_SCALE = 5;
	
	//Manages game screen size
	public static final int GAME_WIDTH = 1050; //525
	public static final int GAME_HEIGHT = 480; //235
	
	//Manages keyManual positioning for displaying controls to screen:
	public static final int KEY_MANUAL_POSITION_X = 200;
	public static final int KEY_MANUAL_POSITION_Y = 23;
	public static final int KEY_MANUAL_OFFSET_Y = 20;

	//Manages keyManual rectangle so easier to read
	public static final int KEY_MANUAL_RECT_Y = 6;
	public static final int KEY_MANUAL_RECT_X = GAME_WIDTH - KEY_MANUAL_POSITION_X - KEY_MANUAL_RECT_Y/2;
	public static final int KEY_MANUAL_RECT_WIDTH = KEY_MANUAL_POSITION_X - KEY_MANUAL_RECT_Y;
	//This one should be dynamic based on how many items are in controls array:
	//public static final int KEY_MANUAL_RECT_HEIGHT = 0;  

	//Min/Max/and Default FPS
	public static final int FPS_MIN = 1;
	public static final int FPS_MAX = 150;
	public static final int FPS_DEFAULT = 60;
	
	//Tracks FPS throughout game. Sets to default value at initialization.
	public static int FPS = FPS_DEFAULT;
	
	//Default font
	public static String defaultFont;
	
	//Message To Player (Pause, Game Over, etc.) & length of message
	private static String pauseMsg; 
	private static int pauseMsgLen;	

	//Tracks whether player is allowed to continue game or died and needs to start over
	private static boolean continueGame = false;
	
	//Tracks whether player is starting new level, which is needed as a check since we need "continue" still active, but need to reset SOME defaults, but not all
	private static boolean newLevel = false;
	
	//# of Players Selected
	private static int playerSelectCount;
	
	//Player 1-x current Character
	private static int player1CurrentCharacter;
	
	//Tracks player current Position for settings file
	private static float currentPlayerPositionX = 0
	          		   , currentPlayerPositionY = 0; 

	//Multiplier for speed/scale/etc.
	private static int multiplier;

	//Denotes whether debug mode is active or not
	private static boolean debugToggle;
	
	//Denotes whether game is paused or not
	private static boolean pauseToggle;

	//Denotes whether player is in "Stop" Mode or not
	private static boolean stopToggle;

	//Denotes whether key manual is active or not
	private static boolean keyManualToggle;

	//Resets GVar variables to their default Values
	public static void resetGVarDefaults() {
		multiplier = 1;
		debugToggle = true;
		pauseToggle = false;		
		stopToggle = false;		
		keyManualToggle = false;
		pauseMsg = "PAUSED";
		pauseMsgLen = pauseMsg.length();
		defaultFont = FontManager.mapFonts.get(EnumFont.LucidaSansUnicode);

		if(!continueGame) {
			FPS = FPS_DEFAULT;
			Game.gameAudio.manuallyResetSpeedDefault();			
			
			//If game is loaded, use these values, otherwise use defaults
			if(Game.getLoaded()) {
				currentPlayerPositionX = ((GameState)Game.getGameState()).getLevel().getSpawnX();
				currentPlayerPositionY = ((GameState)Game.getGameState()).getLevel().getSpawnY();
			}
		}
	}
	
	/*************** GETTERS and SETTERS ***************/
	
	//Gets font using the fontManager and currentSize
	//Honestly I'm not 100% sure we need to do it how we're doing it, but I haven't considered what an alternate way to do it would be, but works as we're doing it...
	public static Font getFont(String font, int size) {
		return new Font(font, 1, size);
	}

	//Gets font size used for shadow lettering by taking the currentFontSize and dividing to cast a smaller shadow
	//Note: larger denominator = larger shadow
	public static int getShadowFont(int currentFontSize) {
		return currentFontSize / 10;
	}
	
	//Sets FPS
	//Note: I didn't set 0 FPS since it will lock game, and I didn't do below 10 since that makes input very laggy and such. 10 still feels slow 
	//      but maybe in the heat of the game it'd be okay. I almost wonder if we should keep FPS higher and just slow movement so it's like a 
	//      smooth movement that doesn't feel laggy, but purposefully slow? Then slow enemies/items/everything  as well to match or something. I dunno.
	//      It would require reworking it considerably but I think would have a better effect. I imagine that's the correct way to do it, speed up/slow
	//      down player and enemies/items and keep FPS locked. The more I think about it I say 100%, just need to figure out the best way to do that since
	//      a lot of moving parts involved from various classes, so need to be smart about it.
	public static void setFPS(int newFPS) {

		int oldFPS = FPS;
		
		//sets FPS equal to the new value then we're going run some checks on it
		FPS = newFPS;
		
		//Only Print this if FPS is greater than the minimum or is less than the maximum (between range)
		//Note: We're setting FPS then doing this check which means the first time it gets to the min and max, it will print
		//      but after that it won't. Clever girl...
		if(FPS >= FPS_MIN && FPS <= FPS_MAX) {

			//If currently in "Stop" Mode and THEN you increase speed, it will exit "Stop" mode
			if(getStop() && FPS > FPS_MIN) {
				//Special adjustment for coming out of "Stop" mode since both "Stop" mode and the slowest setting share 10 FPS.
				FPS = oldFPS;

				//Exits "Stop" mode
				toggleStop();

				//Sets audio speed back to the standard threshold
				Game.gameAudio.setCurrentSpeed(Game.gameAudio.STOP_SPEED_ADJUST);
			}
			
			//Displays FPS if in Debug Mode and game is NOT paused!
        	if(GVar.getDebug()) 
        		print("FPS: " + FPS);		
		}
		
		//If you decrease FPS BELOW the min, it will "STOP" the game
		if(FPS < FPS_MIN) {
			
			//Set FPS to Min since cannot go below it			
			FPS = FPS_MIN;
			
			//This check makes it so it will only print STOPPED to console once
			if(!getStop()) {

				//Displays current Volume if in Debug Mode
	        	if(GVar.getDebug())
	        		print("FPS: STOPPED!");

				//Decreases audio speed considerably due to "Stop" mode
				Game.gameAudio.setCurrentSpeed(-Game.gameAudio.STOP_SPEED_ADJUST);

				//Put game in "STOP" mode
	        	toggleStop();
			}

		//If you increase FPS ABOVE the max, it will Set FPS to max since cannot go above it
		} else if(FPS > FPS_MAX) {
			FPS = FPS_MAX;	
		}
	}
	
	//Gets Game Multiplier (used for scaling and speed and such)
	public static int getMultiplier() {
		return multiplier;
	}

	//Sets the Game Multiplier
	public static void setMultiplier(int multiplier) {
		GVar.multiplier = multiplier;
	}

	//Increases Multipler by x amount (decreases (if you use a negative value))
	public static void incrementMultiplier(int x) {
		multiplier += x;
	}

	//Gets message used in pause screen
	public static String getPauseMsg() {
		return pauseMsg;
	}

	//Sets message used in pause screen
	public static void setPauseMsg(String newMsg) {
		pauseMsg = newMsg;
		pauseMsgLen = pauseMsg.length();
	}

	//Gets length of message used in pause screen
	//Useful for quickly centering with game screen (even if that's currently off :P )
	public static int getPauseMsgLen() {
		return pauseMsgLen;
	}

	//Gets how many players are currently selected
	public static int getPlayerSelectCount() {
		return playerSelectCount;
	}

	//Sets how many players are currently selected
	public static void setPlayerSelectCount(int no){
		playerSelectCount = no;
	}
	
	//Gets player 1 current Character
	public static int getPlayer1CurrentCharacter() {
		return player1CurrentCharacter;
	}
	
	//Sets player 1 current Character
	public static void setPlayer1CurrentCharacter(int i) {
		player1CurrentCharacter = i;
	}
	
	//Gets current Player Position X
	public static float getPlayerPositionX() {
		return currentPlayerPositionX;
	}
	
	//Sets current Player Position X
	public static void setPlayerPositionX(float f) {
		currentPlayerPositionX = f;
	}
	
	//Gets current Player Position Y
	public static float getPlayerPositionY() {
		return currentPlayerPositionY;
	}
	
	//Sets current Player Position Y
	public static void setPlayerPositionY(float f) {
		currentPlayerPositionY = f;
	}
	
	//Gets whether debug mode is currently enabled or not
	public static boolean getDebug() {
		return debugToggle;
	}

	//Toggles whether debug mode is currently enabled or not
	public static void toggleDebug() {

		// If currently enabled, disable
		if(debugToggle) 
			debugToggle = false;

		// If currently disabled, enable
		else
			debugToggle = true;
	}

	//Gets whether game is currently Paused or not
	//Note: Time "Stopped" also qualifies
	public static boolean getPause() {
		return pauseToggle;
	}
	
	//Toggles whether game is currently Paused or not
	public static void togglePause(String msg) {

		//Sets the pause message
		setPauseMsg(msg);

		//if Game is paused and you're not dead, unpause and resume audio
		if(pauseToggle && !Game.gameHeader.getDead()) { 

			//Unpauses game
			pauseToggle = false;

			//Pauses all audio
			Game.gameAudio.pauseAudioStagingArea("ALL");

			//Plays "UNPAUSE" SFX
			Game.gameAudio.playAudioStagingArea("SFX", EnumSFX.Pause.toString());

			//resumes song that was previously playing before pausing game
			Game.gameAudio.resumeAudio("MUSIC");
		}
		
		//Else pause game (Note: Game should already be paused if dead, so that check doesn't matter)
		else {

			//Pause game
			pauseToggle = true;

			//Pauses all audio
			Game.gameAudio.pauseAudioStagingArea("ALL");
			
			//Plays "PAUSE" SFX
			Game.gameAudio.playAudioStagingArea("SFX", EnumSFX.Pause.toString());
		}
	}	
	
	//Gets whether game is currently in "Stop" mode or not
	public static boolean getStop() {
		return stopToggle;
	}
	
	//Toggles whether game is currently in "Stop" mode or not
	public static void toggleStop() {
	
		//if NOT "Stop" Mode and in GameState, stop time
		if(!getStop() && StateManager.getCurrentStateName() == "GameState") {
				
			//Stops Game
			stopToggle = true;

		//If "Stop" Mode and in gameState, then resume time 
		} else if(getStop() && StateManager.getCurrentStateName() == "GameState") {

			//Resumes Game
			stopToggle = false;
		}
	}

	//Gets whether controls are displayed to Player or not
	public static boolean getKeyManual() {
		return keyManualToggle;
	}

	//Toggles whether controls are displayed to Player or not
	public static void toggleKeyManual() {

		// If currently enabled, disable		
		if(keyManualToggle)
			keyManualToggle = false;
			
		//If currently disabled, enable
		else
			keyManualToggle = true;
	}
	
	//Gets whether player can continue game or not
	public static boolean getContinueGame() {
		return continueGame;
	}

	//Sets whether player can continue game or not
	public static void setContinueGame(boolean tf) {
		continueGame = tf;
	}
	
	//Gets whether player is progressing to next level or not
	public static boolean getNewLevel() {
		return newLevel ;
	}

	//Sets whether player is progressing to next level or not
	public static void setNewLevel(boolean tf) {
		newLevel = tf;
	}
	
	//Checks whether settings File Exists or not
	public static boolean settingsExists() {
		return new File("res/files/settings.txt").exists();
	}

	//Loads setting file and populates the variables accordingly
	public static void loadSettings(String path) {
		
		//Holds txt level path
		String file = Utilities.loadFileAsString(path);
		
		//Split all characters from input file using spaces ("\\s+")
		String[] tokens = file.split("\\s+");
		
		//Using this to make incrementing (and reordering) easier for the future
		int z = 2;

		/*Sets HighScore*/ 					Game.gameHeader.setHighScore((int)(Utilities.parseFloat(tokens[z]))); z+=3;
		/*Sets Current Volume (SFX)*/ 		Game.gameAudio.setCurrentVolume("SFX", Utilities.parseFloat(tokens[z])-Game.gameAudio.DEFAULT_CURRENT_VOLUME_SFX); z+=3;
		/*Sets Current Volume (Music)*/ 	Game.gameAudio.setCurrentVolume("MUSIC", Utilities.parseFloat(tokens[z])-Game.gameAudio.DEFAULT_CURRENT_VOLUME_MUSIC); z+=3;
		/*Sets Current Rate (Audio)*/ 		Game.gameAudio.setCurrentRate(Utilities.parseFloat(tokens[z])-Game.gameAudio.DEFAULT_CURRENT_RATE); z+=3;
		/*Sets Current Pitch (Audio)*/ 		Game.gameAudio.setCurrentPitch(Utilities.parseFloat(tokens[z])-Game.gameAudio.DEFAULT_CURRENT_PITCH); z+=3;
		/*Sets Continue Game Boolean*/ 		setContinueGame(Utilities.convertIntToBool((int)(Utilities.parseFloat(tokens[z])))); z+=3;
		/*Sets Current World*/ 				Game.gameHeader.setCurrentWorld((int)(Utilities.parseFloat(tokens[z]))); z+=3;
		/*Sets Current Level*/ 				Game.gameHeader.setCurrentLevel((int)(Utilities.parseFloat(tokens[z]))); z+=3;
		/*Sets Current Song*/ 				Game.gameAudio.setCurrentMusic(tokens[z]); z+=3;
		/*Sets Current Time*/ 				Game.gameHeader.setCurrentTime((int)(Utilities.parseFloat(tokens[z]))); z+=3;
		/*Sets Current Score*/ 				Game.gameHeader.setCurrentScore((int)(Utilities.parseFloat(tokens[z]))); z+=3;
		/*Sets Current Coins*/ 				Game.gameHeader.setCurrentCoins((int)(Utilities.parseFloat(tokens[z]))); z+=3;
		/*Sets Current FPS*/ 				GVar.setFPS((int)(Utilities.parseFloat(tokens[z]))); z+=3;
		/*Sets Current Character*/ 			player1CurrentCharacter = ((int)(Utilities.parseFloat(tokens[z]))); z+=3;
		/*Sets Current Player Count*/ 		setPlayerSelectCount((int)(Utilities.parseFloat(tokens[z]))); z+=3;
		/*Sets Current Player Position X*/	currentPlayerPositionX = (Utilities.parseFloat(tokens[z])); z+=3;
		/*Sets Current Player Position Y*/	currentPlayerPositionY = (Utilities.parseFloat(tokens[z])); z+=3;

		//Do special math to load the actual level now:
		((GameState)Game.getGameState()).setLevel((Game.gameHeader.getCurrentWorld()-1) * 4 + Game.gameHeader.getCurrentLevel());
		
		//If currentTime < HURRY_TIME, set hurry = true
		if(Game.gameHeader.getCurrentTime() < Game.gameHeader.HURRY_TIME)
			Game.gameHeader.setHurry(true);
		
		//Print the above saved variables to console IF in debug mode
		z = 0;
		if(GVar.getDebug()) {
			print("---------------------");
			print("Settings File Loaded:");
			print("---------------------");
			//Print Current High Score
				printnb(tokens[z] + tokens[z+1]); z+=3;
				print(Game.gameHeader.getHighScore());
			//Print Current Volume (SFX)
				printnb(tokens[z] + tokens[z+1]); z+=3;
				print(Game.gameAudio.getCurrentVolume("SFX"));
			//Print Current Volume (MUSIC)
				printnb(tokens[z] + tokens[z+1]); z+=3;
				print(Game.gameAudio.getCurrentVolume("MUSIC"));	
			//Print Current Rate
				printnb(tokens[z] + tokens[z+1]); z+=3;
				print(Game.gameAudio.getCurrentRate());	
			//Print Current Pitch
				printnb(tokens[z] + tokens[z+1]); z+=3;
				print(Game.gameAudio.getCurrentPitch());
			//Print Saved Game
				printnb(tokens[z] + tokens[z+1]); z+=3;
				print(GVar.getContinueGame());
			//Print Current World
				printnb(tokens[z] + tokens[z+1]); z+=3;
				print(Game.gameHeader.getCurrentWorld());
			//Print Current Level
				printnb(tokens[z] + tokens[z+1]); z+=3;
				print(Game.gameHeader.getCurrentLevel());
			//Print Current Music
				printnb(tokens[z] + tokens[z+1]); z+=3;
				print(Game.gameAudio.getCurrentMusic());
			//Print Current Time
				printnb(tokens[z] + tokens[z+1]); z+=3;
				print(Game.gameHeader.getCurrentTime());
			//Print Current Score
				printnb(tokens[z] + tokens[z+1]); z+=3;
				print(Game.gameHeader.getCurrentScore());
			//Print Current Coins
				printnb(tokens[z] + tokens[z+1]); z+=3;
				print(Game.gameHeader.getCurrentCoins());
			//Print Current FPS
				printnb(tokens[z] + tokens[z+1]); z+=3;
				print(GVar.FPS);
			//Print Current Character
				printnb(tokens[z] + tokens[z+1]); z+=3;
				print(((GameState)Game.getGameState()).getPlayer().getCurrentPlayer());
			//Print Current Player Count
				printnb(tokens[z] + tokens[z+1]); z+=3;
				print(getPlayerSelectCount());
			//Print Current Player Position X
				printnb(tokens[z] + tokens[z+1]); z+=3;
				print(currentPlayerPositionX);
			//Print Current Player Position Y
				printnb(tokens[z] + tokens[z+1]); z+=3;
				print(currentPlayerPositionY);
			print("---------------------");
		}
	}
}
