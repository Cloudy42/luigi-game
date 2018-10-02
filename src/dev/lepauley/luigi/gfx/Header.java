package dev.lepauley.luigi.gfx;
/*
 * Will house all of the header information
 */

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import dev.lepauley.luigi.GVar;
import dev.lepauley.luigi.Game;
import dev.lepauley.luigi.states.StateManager;
import dev.lepauley.luigi.utilities.EnumPause;
import dev.lepauley.luigi.utilities.EnumSFX;
import dev.lepauley.luigi.utilities.Utilities;

public class Header {

	//Tracks Current Font Size
	private int currentFontSize;
	
	//tracks "current" Variables for Header
	private int currentScore;
	private int currentCoins;
	private int currentWorld;
	private int currentLevel;
	private int currentTime;

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
				Game.gameAudio.playAudioStagingArea("MUSIC", Game.gameAudio.getCurrentMusic() + " (Hurry!)");
			}

			//If Current Time <=0, kill player (and various other related bookkeeping things)
			if(currentTime <= 0) {

				//I'm setting this first since I swear the first time I tested this it said "PAUSED"
				//for a hot second before switching to TIME UP! Just seems safer to do this first.
				dead = true;
				currentTime = 0;

				//Sets pauseMessage = "TIME UP"
				GVar.togglePause(EnumPause.TIMESUP.toString());
				
				//Need to pause audio and plays death song
				//(don't need to set setSecondsToSkip = 0 because we don't do that for SFX) 
				Game.gameAudio.pauseAudioStagingArea("ALL");
				Game.gameAudio.playAudioStagingArea("SFX", EnumSFX.LuigiDie.toString());

				//Sets HighScore if a new one was reached
				if(currentScore > highScore)
					highScore = currentScore;
			}
		}
	}
	
	public void render(Graphics g) {
		currentFontSize = 20;
		g.setFont (GVar.setFont(GVar.fontA, currentFontSize));

		//Only Display the title screen, player select, and high score in the MenuState
		if(StateManager.getCurrentStateName() == "MenuState") {
			//Menu
			g.drawImage(Assets.menu,215,50,625,185,null);
	
			//Player Select
			g.drawImage(Assets.toad,380,(int)(260 + (GVar.getPlayerSelectCount() - 1) * currentFontSize * 1.5),null);
			Utilities.drawShadowString(g, "1 PLAYER GAME",	400, 275, GVar.getShadowFont(currentFontSize));
			Utilities.drawShadowString(g, "2 PLAYER GAME",	400, (int)(275 + currentFontSize * 1.5), GVar.getShadowFont(currentFontSize));
	
			//High Score
			Utilities.drawShadowString(g, "TOP - " + zeroPrefixToString(4, highScore),	400 + currentFontSize * 1, (int)(275 + currentFontSize * 3), GVar.getShadowFont(currentFontSize));
		}
	
		//Header Info
		Utilities.drawShadowString(g, "MARIO",	215, 20, GVar.getShadowFont(currentFontSize));
		Utilities.drawShadowString(g, zeroPrefixToString(4, currentScore),	215, 20 + currentFontSize, GVar.getShadowFont(currentFontSize));
	
		g.drawImage(Assets.coin,410,25,null);
		Utilities.drawShadowString(g, "x" + zeroPrefixToString(1, currentCoins),	425, 20 + currentFontSize, GVar.getShadowFont(currentFontSize));
	
		Utilities.drawShadowString(g, "WORLD", 	585, 20, GVar.getShadowFont(currentFontSize));
		Utilities.drawShadowString(g, currentWorld + "-" + currentLevel,   	585  + currentFontSize * 1, 20 + currentFontSize, GVar.getShadowFont(currentFontSize));
	
		Utilities.drawShadowString(g, "TIME", 	785, 20, GVar.getShadowFont(currentFontSize));

		if(StateManager.getCurrentStateName() == "GameState") {
			//Sets Color to red if in hurry mode, else default:
			if(hurry) {
				Utilities.drawShadowString(g, Color.red, convertTime(), 785 + currentFontSize/2 * timeSpacing, 20 + currentFontSize * 1, GVar.getShadowFont(currentFontSize));
			} else {
				Utilities.drawShadowString(g, convertTime(), 	785 + currentFontSize/2 * timeSpacing, 20 + currentFontSize * 1, GVar.getShadowFont(currentFontSize));
			}
		}
	}
	
	//Probably a smarter algorithm for this, but basically forces string to be count # of characters prefixed with all zeroes:
	public String zeroPrefixToString(int count, int stat) {
		String s = "";
		int sLen = 0;
		int target = count + 1;
		for(int z = 0; z < count; z++)
			s += "0";
		s += stat;
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
		int seconds = currentTime % 60;

		//Only prefix seconds with 0 IF minutes still exist 
		if(minutes != 0 || seconds >= 10) {
		    time += zeroPrefixToString(1, seconds);
		} else {
		    time += zeroPrefixToString(0, seconds);
		}

		//Used to adjust spacing when losing digits
		if(minutes == 0)
			timeSpacing = 1;
		if(minutes == 0 && seconds < 10)
			timeSpacing = 2;

		return time;
	}
	
	public void resetDefaults() {
		currentScore = 0;
		currentCoins = 0;
		currentWorld = 1;
		currentLevel = 1;
		currentTime = 80;
		timeSpacing = 0;
		
		hurry = false;
		dead = false;
	}
	
	/*************** GETTERS and SETTERS ***************/
	
	public boolean getDead() {
		return dead;
	}
	
	//Allows user to change time
	public void adjustTime(int time) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		//Only allow changing time if NOT dead and NOT paused (and don't let values go negative)
		if(!dead && currentTime >= 1 && !GVar.getPause()) {
			currentTime += time;
			String tempSong = Game.gameAudio.getCurrentMusic();
			String tempHurry = " (Hurry!)";
			int tempSongLen = tempSong.length();
			int tempHurryLen = tempHurry.length();
			
			//Resets hurry
			if(hurry && currentTime > HURRY_TIME) {
				hurry = false;
				Game.gameAudio.pauseAudioStagingArea("MUSIC");
				Game.gameAudio.playAudioStagingArea("MUSIC", tempSong.substring(0, tempSongLen - tempHurryLen));
			}
		}
	}
		
}
