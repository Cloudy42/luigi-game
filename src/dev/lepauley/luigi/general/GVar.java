package dev.lepauley.luigi.general;

import java.awt.Font;

import dev.lepauley.luigi.states.StateManager;
import dev.lepauley.luigi.utilities.EnumFont;
import dev.lepauley.luigi.utilities.EnumPause;
import dev.lepauley.luigi.utilities.EnumSFX;
import dev.lepauley.luigi.utilities.FontManager;
import static dev.lepauley.luigi.utilities.Utilities.*;

/*
 * Class of global variables to use throughout code:
 */

public class GVar {
sd
	//Manages how small/large entities can scale to
	public static final int MIN_SCALE = 1;
	public static final int MAX_SCALE = 5;
	
	//Manages game screen size
	public static final int GAME_WIDTH = 1050; //525
	public static final int GAME_HEIGHT = 470; //235
	
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
	public static final int FPS_MIN = 10;
	public static final int FPS_MAX = 150;
	public static final int FPS_DEFAULT = 60;
	
	//Tracks FPS throughout game. Sets to default value at initialization.
	public static int FPS = FPS_DEFAULT;
	
	//Default font
	public static String defaultFont;
	
	//Message To Player (Pause, Game Over, etc.) & length of message
	private static String pauseMsg; 
	private static int pauseMsgLen;	

	//# of Players Selected
	private static int playerSelectCount;
	
	//Multiplier for speed/scale/etc.
	private static int multiplier;

	//Denotes whether debug mode is active or not
	private static boolean debugToggle;
	
	//Denotes whether game is scrolling or not
	private static boolean scrollToggle;

	//Denotes whether game is paused or not
	private static boolean pauseToggle;

	//Denotes whether key manual is active or not
	private static boolean keyManualToggle;

	//Resets GVar variables to their default Values
	public static void resetGVarDefaults() {
		playerSelectCount = 1;
		multiplier = 1;
		FPS = FPS_DEFAULT;
		debugToggle = true;
		scrollToggle = true;
		pauseToggle = false;		
		keyManualToggle = false;
		pauseMsg = "PAUSED";
		pauseMsgLen = pauseMsg.length();
		defaultFont = FontManager.mapFonts.get(EnumFont.LucidaSansUnicode);
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

		//sets FPS equal to the new value then we're going run some checks on it
		FPS = newFPS;
		
		//Only Print this if FPS is greater than the minimum or is less than the maximum (between range)
		//Note: We're setting FPS then doing this check which means the first time it gets to the min and max, it will print
		//      but after that it won't. Clever girl...
		if(FPS >= FPS_MIN && FPS <= FPS_MAX)

			//Displays FPS if in Debug Mode and game is NOT paused!
        	if(GVar.getDebug()) 
        		print("FPS: " + FPS);
		
		//If Pause Message = "STOP" and THEN you increase speed, it will resume (don't have to push pause to get out)
		if(pauseMsg.equals(EnumPause.STOP.toString()) && FPS > FPS_MIN)
			togglePause(EnumPause.RESUME.toString());
		
		//If you decrease FPS BELOW the min, it will "STOP" the game
		if(FPS < FPS_MIN) {

			//Set FPS to Min since cannot go below it
			FPS = FPS_MIN;
			
			//This check makes it so it will only print STOPPED to console once
			if(!getPause()) {

				//Displays current Volume if in Debug Mode
	        	if(GVar.getDebug())
	        		print("FPS: STOPPED!");

        		//Put game in "STOP" mode
	        	togglePause(EnumPause.STOP.toString());
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
		
		//Plays character switch audio.
		//Note: So I was just like "this is NOT the place for this" but then I looked at code
		//      in MenuState and realized I'd have to write extra logic there or paste duplicate code
		//      so turns out that this is 100% the place for this. Who knew (me a few weeks ago, that's who :P )
		Game.gameAudio.pauseAudioStagingArea("SFX");
		Game.gameAudio.playAudioStagingArea("SFX", EnumSFX.Bump.toString());
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

	//Gets whether scroll mode is currently enabled or not
	public static boolean getScroll() {
		return scrollToggle;
	}

	//Toggles whether debug mode is currently enabled or not
	public static void toggleScroll() {

		// If currently enabled, disable
		if(scrollToggle) 
			scrollToggle = false;

		//If currently disabled, enable
		else
			scrollToggle = true;
	}

	//Gets whether game is currently Paused or not
	//Note: Time "Stopped" also qualifies
	public static boolean getPause() {
		return pauseToggle;
	}
	
	//Toggles whether game is currently Paused or not
	//Note: Time "Stopped" also qualifies
	public static void togglePause(String msg) {

		//Sets the pause message
		setPauseMsg(msg);

		//if Pause Message == STOP, stop time (if not currently paused)
		if(msg.equals(EnumPause.STOP.toString())){
			if(!pauseToggle && StateManager.getCurrentStateName() == "GameState") {
				
				//Clears message and "pauses" game
				setPauseMsg("");
				pauseToggle = true;
			}

		//If Pause Message == RESUME AND game is paused and in game state, then unpause game 
		} else if(msg.equals(EnumPause.RESUME.toString())){
			if(pauseToggle && StateManager.getCurrentStateName() == "GameState") {

				//Clears message and "unpauses" game
				setPauseMsg("");
				pauseToggle = false;
			}

		//Otherwise do normal pause rules:
		} else if(!msg.equals(EnumPause.STOP.toString())) {

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

}
