package dev.lepauley.luigi.gfx;
/*
 * Will house all of the header information
 */

import java.awt.Color;
import java.awt.Graphics;

import dev.lepauley.luigi.GVar;
import dev.lepauley.luigi.Game;
import dev.lepauley.luigi.states.StateManager;
import dev.lepauley.luigi.utilities.EnumPause;
import dev.lepauley.luigi.utilities.EnumSFX;
import dev.lepauley.luigi.utilities.Utilities;

public class Header {

	//Font Info
	private int currentFontSize;
	
	//"current" Variables
	private int currentScore;
	private int currentCoins;
	private int currentWorld;
	private int currentLevel;
	private int currentTime;

	//Holds High Score
	//(we'll need to figure out a way to save this between play throughs
	//  currently setting to 0)
	private int highScore = 0;
		
	//Default Hurry time
	private final int HURRY_TIME = 60;

	//Adjusts spacing if minutes = 0
	private int timeSpacing = 0; 
	
	//boolean checks (likely a smarter way to do this)
	private boolean hurry;
	private boolean dead;
	
	public Header() {
		resetDefaults();
	}
	
	public void tick() {
		if(!dead && !GVar.getPause()) {
			currentScore++;
			currentTime--;
			currentCoins++;
			if(currentCoins >= 100) {
				currentCoins = 0;
				Game.gameAudio.playAudio("SFX", EnumSFX.OneUp.toString());
			}
			//If Time is almost out (100 seconds left), change song to indicate "hurry" state.
			if(currentTime <= HURRY_TIME && !hurry) {
				hurry = true;
				Game.gameAudio.pauseAudio("Music");
				Game.gameAudio.playAudio("Music", Game.gameAudio.getCurrentSong() + " (Hurry!)");
			}
			if(currentTime <= 0) {
				//I'm setting this first since I swear the first time I tested this it said "PAUSED"
				//for a hot second before switching to TIME UP! Just seems safer to do this first.
				dead = true;
				currentTime = 0;
				GVar.togglePause(EnumPause.TIMESUP.toString());
				Game.gameAudio.pauseAudio("all");
				Game.gameAudio.playAudio("sfx", EnumSFX.LuigiDie.toString());
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
		currentTime = 40;
		timeSpacing = 0;
		
		hurry = false;
		dead = false;
	}
	
	/*************** GETTERS and SETTERS ***************/
	
	public boolean getDead() {
		return dead;
	}
	
	//Allows user to change time
	public void adjustTime(int time) {
		//Only allow changing time if NOT dead and NOT paused (and don't let values go negative)
		if(!dead && currentTime >= 1 && !GVar.getPause()) {
			currentTime += time;
			String tempSong = Game.gameAudio.getCurrentSong();
			String tempHurry = " (Hurry!)";
			int tempSongLen = tempSong.length();
			int tempHurryLen = tempHurry.length();
			
			//Resets hurry
			if(hurry && currentTime > HURRY_TIME) {
				hurry = false;
				Game.gameAudio.pauseAudio("Music");
				Game.gameAudio.playAudio("Music", tempSong.substring(0, tempSongLen - tempHurryLen));
			}
		}
	}
		
}
