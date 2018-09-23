package dev.lepauley.luigi.gfx;
/*
 * Will house all of the header information
 */

import java.awt.Graphics;

import dev.lepauley.luigi.GVar;
import dev.lepauley.luigi.Game;
import dev.lepauley.luigi.states.StateManager;
import dev.lepauley.luigi.utilities.EnumMusic;
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
	private int currentTime ;

	//Holds High Score
	//(we'll need to figure out a way to save this between play throughs
	//  currently setting to 0)
	private int highScore = 0;
	
	//Divider for "time" Variables to extend with ticks.
	//Alternate system would be just do a tickLoop in the tick so that it only ever increments
	//once every 10 ticks then we don't need all of this individual variable adjustments all
	//over the place. I think that'll be better, but I gotta go now so I'll test next time I can.
	private final int TIME_DIVIDER = 10;
	
	//boolean checks (likely a smarter way to do this)
	private boolean hurry;
	private boolean dead;
	
	public Header() {
		resetDefaults();
	}
	
	public void tick() {
		if(!dead) {
			currentScore++;
			currentTime--;
			currentCoins++;
			if(currentCoins >= 100 * TIME_DIVIDER) {
				currentCoins = 0;
				Game.gameAudio.playAudio("SFX", EnumSFX.OneUp.toString());
			}
			//If Time is almost out (100 seconds left), change song to indicate "hurry" state.
			if(currentTime <= 100 * TIME_DIVIDER && !hurry) {
				hurry = true;
				Game.gameAudio.pauseAudio("Music");
				Game.gameAudio.playAudio("Music", EnumMusic.RunningAround_Hurry.toString());
			}
			if(currentTime <= 0) {
				dead = true;
				Game.gameAudio.pauseAudio("all");
				Game.gameAudio.playAudio("sfx", EnumSFX.LuigiDie.toString());
				//Sets HighScore if a new one was reached
				if(currentScore > highScore)
					highScore = currentScore;
			}
		}
	}
	
	public void render(Graphics g) {

		g.setFont (GVar.FONT_20);
		currentFontSize = GVar.FONT_20.getSize();

		//Only Display the title screen, player select, and high score in the MenuState
		if(StateManager.getCurrentStateName() == "MenuState") {
			//Menu
			g.drawImage(Assets.menu,215,50,625,185,null);
	
			//Player Select
			g.drawImage(Assets.toad,380,(int)(260 + (GVar.getPlayerSelectCount() - 1) * currentFontSize * 1.5),null);
			Utilities.drawShadowString(g, "1 PLAYER GAME",	400, 275, GVar.FONT_20_SHADOW);
			Utilities.drawShadowString(g, "2 PLAYER GAME",	400, (int)(275 + currentFontSize * 1.5), GVar.FONT_20_SHADOW);
	
			//High Score
			Utilities.drawShadowString(g, "TOP - " + zeroPrefixToString(4, highScore),	400 + currentFontSize * 1, (int)(275 + currentFontSize * 3), GVar.FONT_20_SHADOW);
		}
	
		//Header Info
		Utilities.drawShadowString(g, "MARIO",	215, 20, GVar.FONT_20_SHADOW);
		Utilities.drawShadowString(g, zeroPrefixToString(4, currentScore),	215, 20 + currentFontSize, GVar.FONT_20_SHADOW);
	
		g.drawImage(Assets.coin,410,25,null);
		Utilities.drawShadowString(g, "x" + zeroPrefixToString(1, currentCoins/TIME_DIVIDER),	425, 20 + currentFontSize, GVar.FONT_20_SHADOW);
	
		Utilities.drawShadowString(g, "WORLD", 	585, 20, GVar.FONT_20_SHADOW);
		Utilities.drawShadowString(g, currentWorld + "-" + currentLevel,   	585  + currentFontSize * 1, 20 + currentFontSize, GVar.FONT_20_SHADOW);
	
		Utilities.drawShadowString(g, "TIME", 	785, 20, GVar.FONT_20_SHADOW);

		if(StateManager.getCurrentStateName() == "GameState") {
			Utilities.drawShadowString(g, "" + currentTime/TIME_DIVIDER, 	785, 20 + currentFontSize * 1, GVar.FONT_20_SHADOW);
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
	
	public void resetDefaults() {
		currentScore = 0;
		currentCoins = 0;
		currentWorld = 1;
		currentLevel = 1;
		currentTime = 400 * TIME_DIVIDER;
		
		hurry = false;
		dead = false;
	}
	
	/*************** GETTERS and SETTERS ***************/
	
	public boolean getDead() {
		return dead;
	}
		
}
