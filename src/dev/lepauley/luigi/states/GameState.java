package dev.lepauley.luigi.states;

import java.awt.Color;
import java.awt.Graphics;

import dev.lepauley.luigi.GVar;
import dev.lepauley.luigi.Game;
import dev.lepauley.luigi.entities.creatures.Player;
import dev.lepauley.luigi.levels.Level;
import dev.lepauley.luigi.utilities.Utilities;

/*
 * Where actual game play is at
 */
public class GameState extends State {

	private Player player;
	private Level level;

	//Handles Pause Message
	private final String PAUSED = "PAUSED"; 
	private final int PAUSED_LEN = PAUSED.length();
	private final int FONT_LEN = GVar.FONT_20.getSize();
	
	private boolean tempDebugPausedPosition = true;
	
	public GameState(Game game) {
		super(game);
		level = new Level("res/levels/1-1.txt");
		player = new Player(game, level.getSpawnX(), level.getSpawnY());
	}
	
	//Must tick level before player do to proper layer positioning
	@Override
	public void tick() {
		//Toggles Pause On/Off
		if(game.getKeyManager().pauseToggle)
			GVar.togglePause();

		//If Game = UnPaused, tick
		if(!GVar.getPause()) {
			level.tick();
			player.tick();
		}
	}

	//Must render level before player do to proper layer positioning
	@Override
	public void render(Graphics g) {
		level.render(g);
		player.render(g);
		//If Game = Paused, display to game
		if(GVar.getPause()) {
			g.setFont (GVar.FONT_30);
			Utilities.drawShadowString(g, PAUSED, GVar.GAME_WIDTH/2 - PAUSED_LEN/2 * FONT_LEN, GVar.GAME_HEIGHT/2 - FONT_LEN/2, GVar.FONT_30_SHADOW);
			//This seems like it SHOULD be writing it perfectly centered, but for some reason it's not. whyyyyyyy </3 
			//50% of GVar.GAME_WIDTH = 1050; //525
			//50% of GVar.GAME_HEIGHT = 470; //235
			//50% of Font = 30 //15
			//so makes sense (to me) that the X of font should start drawing at x = 435 because after you've written 3 letters (PAU)
			//    then you are at 3 * 30 = 90 = 525 (The 50% width mark)
			//    and when you do 50% of font subtracted from center = 235 - 15 = 220. 
			//So why is it not drawing dead center? Brian? MATH! I need help! (/ToT)/
			if(tempDebugPausedPosition) {
				tempDebugPausedPosition = false;
				System.out.println("//////////////////////////////////////");
				System.out.println("GVar.GAME_WIDTH/2: " + GVar.GAME_WIDTH/2);
				System.out.println("PAUSED_LEN/2: " + PAUSED_LEN/2);
				System.out.println("FONT_LEN: " + FONT_LEN);
				System.out.println("PAUSED_LEN/2 * FONT_LEN: " + PAUSED_LEN/2 * FONT_LEN);
				System.out.println("GVar.GAME_WIDTH/2 - PAUSED_LEN/2 * FONT_LEN: " + (GVar.GAME_WIDTH/2 - PAUSED_LEN/2 * FONT_LEN));
				System.out.println("------------------------------------");
				System.out.println("GVar.GAME_HEIGHT/2: " + GVar.GAME_HEIGHT/2);
				System.out.println("FONT_LEN/2: " + FONT_LEN/2);
				System.out.println("GVar.GAME_HEIGHT/2 - FONT_LEN/2: " + (GVar.GAME_HEIGHT/2 - FONT_LEN/2));
				System.out.println("//////////////////////////////////////");
			}
}
	}

}
