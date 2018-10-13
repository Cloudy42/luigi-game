package dev.lepauley.luigi.gfx;
/*
 * Will house all of the header information
 */

import java.awt.Graphics;

import dev.lepauley.luigi.general.GVar;
import dev.lepauley.luigi.general.Game;
import dev.lepauley.luigi.states.MenuState;
import dev.lepauley.luigi.states.StateManager;
import dev.lepauley.luigi.utilities.ColorManager;
import dev.lepauley.luigi.utilities.EnumColor;
import dev.lepauley.luigi.utilities.EnumPause;
import dev.lepauley.luigi.utilities.EnumSFX;
import dev.lepauley.luigi.utilities.Utilities;

public class Header {

	//Tracks Current Font Size
	private int currentFontSize;
	
	//Default values to display in menu State
	private final int DEFAULT_WORLD = 1, DEFAULT_LEVEL =1;

	//tracks "current" Variables for Header
	private int currentScore
              , currentCoins
              , currentWorld
              , currentLevel
              , currentTime;

	//used for dynamic menuState (namely Continue being visible or not. Let's us do spacing for Options accordingly.
	private float menuSpacing = 0.0f, menuSpacingConstant = 1.5f;
	
	//Holds High Score
	//(we'll need to figure out a way to save this between play throughs. Setting to 0 until then)
	private int highScore = 0;
		
	//Default Hurry time
	//once time dips below default time, audio will change to "Hurry" version
	private final int HURRY_TIME = 60;

	//Adjusts spacing if minutes = 0 AND/OR tens of seconds < 10
	//Example:				              WITH       vs       WITHOUT
	//                   				  TIME                 TIME
	//Minutes >=  1      				  2:00                 2:00
	//Minutes  =  0 && Seconds >= 10       39                  39
	//Seconds <  10       				    8                  8
	private int timeSpacing; 
	
	//boolean checks (likely a smarter way to do this)
	//Also dead check should be on player, but have here for now. Easy enough to move when time comes.
	//Hurry = time < default HURRY_TIME so "Hurry" audio should be playing
	//Dead = player = dead (in this case, it's cause time ran out)
	private boolean hurry;
	private boolean dead;
	
	//Header Constructor
	public Header() {

		//Resets defaults to their...default values ;P 
		resetDefaults();
	}

	//Header tick() method updates various aspects of Header
	public void tick(){
		
		//If Player = Dead and Game is NOT paused, adjust various variables
		if(!dead && !GVar.getPause()) {
			currentScore++;
			currentTime--;
			currentCoins++;

			//If Coin count >= 100, reset to zero and play the 1-up sound (and give player extra life when we get there :P )
			if(currentCoins >= 100) {
				currentCoins = 0;
				Game.gameAudio.playAudioStagingArea("SFX", EnumSFX.OneUp.toString());
			}

			//If Time is almost out (HURRY_TIME) and haven't set hurry boolean yet,
			//change song to indicate "hurry" state and set hurry boolean
			if(currentTime <= HURRY_TIME && !hurry) {
				hurry = true;

				//Need to pause audio and set setSecondsToSkip = 0 so it plays hurry state song from the start
				Game.gameAudio.pauseAudioStagingArea("MUSIC");
				Game.gameAudio.setSecondsToSkip(0);
				Game.gameAudio.playAudioStagingArea("MUSIC", Game.gameAudio.getCurrentMusic() + "(Hurry!)");
			}

			//If Current Time <=0, kill player (and various other related bookkeeping things)
			if(currentTime <= 0) {

				//I'm setting this first since I swear the first time I tested this it said "PAUSED"
				//for a hot second before switching to TIME UP! Just seems safer to do this first.
				dead = true;
				currentTime = 0;
				GVar.setContinueGame(false);

				//Sets pauseMessage = "TIME UP"
				GVar.togglePause(EnumPause.TIMESUP.toString());
				
				//Need to pause audio and plays death song
				//(don't need to set setSecondsToSkip = 0 because we don't do that for SFX) 
				Game.gameAudio.pauseAudioStagingArea("ALL");
				Game.gameAudio.playAudioStagingArea("SFX", EnumSFX.LuigiDie.toString());

				//Sets HighScore if a new one was reached
				if(currentScore > highScore) {
					highScore = currentScore;
					
			        //If highscore is set, update the settings file so that upon reloading, it's still saved
					Utilities.writeSettingsFile();
				}
			}
		}
	}
	
	//Draws Header assets to display
	public void render(Graphics g) {

		//Reset menuSpacing with each pass
		menuSpacing = 0.0f;
		
		//sets current Font & size
		currentFontSize = 20;
		g.setFont (GVar.getFont(GVar.defaultFont, currentFontSize));

		//Only Display the title screen, player select, and high score if in MenuState
		if(StateManager.getCurrentStateName() == "MenuState") {

			//Menu
			g.drawImage(Assets.menu,215,50,625,185,null);
	
			//Menu Select (with toad icon as selector)
			g.drawImage(Assets.toad,380,(int)(260 + (((MenuState)StateManager.getCurrentState()).getCurrentSelection()) * currentFontSize * 1.5),null);
			Utilities.drawShadowString(g, "1 PLAYER GAME",	400, (int)(275 + currentFontSize * menuSpacing), GVar.getShadowFont(currentFontSize)); menuSpacing += menuSpacingConstant; 
			Utilities.drawShadowString(g, "2 PLAYER GAME",	400, (int)(275 + currentFontSize * menuSpacing), GVar.getShadowFont(currentFontSize)); menuSpacing += menuSpacingConstant;
			if(GVar.getContinueGame()) {
				Utilities.drawShadowString(g, "CONTINUE (" + currentWorld + "-" + currentLevel + ")",	400, (int)(275 + currentFontSize * menuSpacing), GVar.getShadowFont(currentFontSize)); menuSpacing += menuSpacingConstant;
			}
			Utilities.drawShadowString(g, "OPTIONS",	400, (int)(275 + currentFontSize * menuSpacing), GVar.getShadowFont(currentFontSize)); menuSpacing += menuSpacingConstant;
	
			//High Score
			Utilities.drawShadowString(g, "TOP - " + zeroPrefixToString(5, highScore),	400 + currentFontSize * 1, (int)(275 + currentFontSize * 6.0), GVar.getShadowFont(currentFontSize));
		}
	
		//Header Info
		//Current Score
		Utilities.drawShadowString(g, "MARIO",	215, 20, GVar.getShadowFont(currentFontSize));
		Utilities.drawShadowString(g, zeroPrefixToString(5, currentScore),	215, 20 + currentFontSize, GVar.getShadowFont(currentFontSize));
	
		//Current Coins (with Coin icon) - should be animated in future
		g.drawImage(Assets.coin,410,25,null);
		Utilities.drawShadowString(g, "x" + zeroPrefixToString(2, currentCoins),	425, 20 + currentFontSize, GVar.getShadowFont(currentFontSize));
	
		//Current World-Level
		Utilities.drawShadowString(g, "WORLD", 	585, 20, GVar.getShadowFont(currentFontSize));
		
		//If in menuState, display default values, otherwise display actual
		if(StateManager.getCurrentStateName() == "MenuState") {
			Utilities.drawShadowString(g, DEFAULT_WORLD + "-" + DEFAULT_LEVEL,   	585  + currentFontSize * 1, 20 + currentFontSize, GVar.getShadowFont(currentFontSize));
		} else {
			Utilities.drawShadowString(g, currentWorld + "-" + currentLevel,   	585  + currentFontSize * 1, 20 + currentFontSize, GVar.getShadowFont(currentFontSize));
		}
	
		//Current Time (Minutes:seconds)
		Utilities.drawShadowString(g, "TIME", 	785, 20, GVar.getShadowFont(currentFontSize));

		//If in "Hurry" Mode, display time in different color
		if(StateManager.getCurrentStateName() == "GameState") {

			//Sets Color to red if in hurry mode, else default:
			if(hurry) {
				Utilities.drawShadowString(g, ColorManager.mapColors.get(EnumColor.PrimaryRed), convertTime(), 785 + currentFontSize/2 * timeSpacing, 20 + currentFontSize * 1, GVar.getShadowFont(currentFontSize));

			//Sets color to default (currently white) if NOT in hurry mode
			} else {
				Utilities.drawShadowString(g, convertTime(), 	785 + currentFontSize/2 * timeSpacing, 20 + currentFontSize * 1, GVar.getShadowFont(currentFontSize));
			}
		}
	}
	
	//Probably a smarter algorithm for this, but basically forces string to be count # of characters prefixed with all zeroes:
	//Example: zeroPrefixToString(5,15) means we want the String to contain 5 digits and we're going to append 15 on the end,
	//         which gives us "0000015", but since we only want it to be 5 digits, we're going to keep chopping off the first
	//         digit until we have 5 digits, so final result would be "00015"
	public String zeroPrefixToString(int target, int value) {
		String s = "";
		int sLen = 0;
		for(int z = 0; z < target; z++)
			s += "0";
		s += value;
		sLen = s.length();
		
		if(sLen > target) {
			for(int j = 1; j <= sLen; j++) {
				if((sLen - j) == target)
					return s.substring(j,j + target); 
			}
		}
		return s.substring(0, target);
	}
	
	//Convert time into minutes and seconds:
	public String convertTime() {
		String time = "";
		int minutes = currentTime / 60;

		//Only display minutes if > 0
		if(minutes > 0)
			time += String.valueOf(minutes) + ":";
		
		//Seconds is the remainder when dividing by 60
		int seconds = currentTime % 60;

		//Only prefix seconds with 0 IF minutes still exist 
		if(minutes != 0 || seconds >= 10) {
		    time += zeroPrefixToString(2, seconds);

		//Otherwise should only be 1 digit
		} else {
		    time += zeroPrefixToString(1, seconds);
		}

		//Used to adjust spacing when losing digits
		if(minutes == 0)
			timeSpacing = 1;
		if(minutes == 0 && seconds < 10)
			timeSpacing = 2;

		//Return time to display in header
		return time;
	}
	
	//Resets defaults for Header
	public void resetDefaults() {
		currentScore = 0;
		currentCoins = 0;
		
		//Only reset to 1-1 IF player died
		if(dead) {
			currentWorld = 1;
			currentLevel = 1;
		}
		currentTime = 80;
		timeSpacing = 0;

		//If no settings file exists or game is not loaded, use this default
		if(!GVar.settingsExists() || !Game.getLoaded())
			highScore = 0;

		hurry = false;
		dead = false;
	}
	
	/*************** GETTERS and SETTERS ***************/
	
	//Gets whether game is in hurry state or not
	public boolean getHurry() {
		return hurry;
	}
	
	//Gets whether player is dead or not
	public boolean getDead() {
		return dead;
	}
	
	//Sets whether player is dead or not
	public void setDead(boolean bool) {
		dead = bool;
	}
	
	//Allows user to change time
	public void adjustTime(int time){

		//Only allow changing time if Player is NOT dead and Game is NOT paused 
		//(and don't let values go negative so stop it 1 early since will tick down to 0)
		if(!dead && currentTime >= 1 && !GVar.getPause()) {

			//Increment time
			currentTime += time;

			//Get some song stats so we can "undo" the Hurry song if we add time and take it out of hurry mode 
			String tempHurry = "(Hurry!)";
			int tempHurryLen = tempHurry.length();
			int tempSongLen = Game.gameAudio.getCurrentMusic().length();
			
			//Resets hurry mode if currently in hurry mode and current time > HURRY_TIME
			if(hurry && currentTime > HURRY_TIME) {
				hurry = false;

				//Pauses audio and converts it from Hurry audio to standard audio (same song)
				Game.gameAudio.pauseAudioStagingArea("MUSIC");
				Game.gameAudio.playAudioStagingArea("MUSIC", Game.gameAudio.getCurrentMusic().substring(0, tempSongLen - tempHurryLen));
			}
		}
	}
	
	//Gets Current High Score
	public int getHighScore() {
		return highScore;
	}
		
	//Sets Current High Score
	public void setHighScore(int i) {
		highScore = i;
	}
	
	//Gets Current Level
	public int getCurrentLevel() {
		return currentLevel;
	}	
		
	//Sets Current Level
	public void setCurrentLevel(int i) {
		currentLevel = i;
	}	

	//Gets Current World
	public int getCurrentWorld() {
		return currentWorld;
	}	
		
	//Sets Current World
	public void setCurrentWorld(int i) {
		currentWorld = i;
	}	
		
}
