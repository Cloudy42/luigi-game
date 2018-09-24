package dev.lepauley.luigi.states;

import java.awt.Graphics;
import dev.lepauley.luigi.GVar;
import dev.lepauley.luigi.Game;
import dev.lepauley.luigi.entities.creatures.Creature;
import dev.lepauley.luigi.entities.creatures.Player;
import dev.lepauley.luigi.levels.Level;
import dev.lepauley.luigi.utilities.Utilities;

/*
 * Where actual game play is at
 */
public class GameState extends State {

	private Player player;
	private Level level;

	//Font Info
	public int currentFontSize;
	
	private boolean tempDebugPausedPosition = true;
	
	public GameState(Game game) {
		super(game,"GameState");
		level = new Level("res/levels/1-1.txt");
		player = new Player(game, level.getSpawnX(), level.getSpawnY());
	}
	
	//Must tick level before player do to proper layer positioning
	@Override
	public void tick() {
		//Toggles Pause On/Off (only if not dead)
		if(game.getKeyManager().pauseToggle && !Game.gameHeader.getDead())
			GVar.togglePause();

		//If Game = UnPaused, tick
		if(!GVar.getPause()) {
			level.tick();
			player.tick();
			Game.gameHeader.tick();
		}
	}

	//Must render level before player do to proper layer positioning
	@Override
	public void render(Graphics g) {
		level.render(g);
		Game.gameHeader.render(g);
		player.render(g);
		//If Game = Paused, display to game
		if(GVar.getPause()) {
			currentFontSize = 30;
			g.setFont (GVar.setFont(GVar.fontA, currentFontSize));
			Utilities.drawShadowString(g, GVar.getPauseMsg(), GVar.GAME_WIDTH/2 - GVar.getPauseMsgLen()/2 * currentFontSize, GVar.GAME_HEIGHT/2 - currentFontSize/2, GVar.getShadowFont(currentFontSize));
			//This seems like it SHOULD be writing it perfectly centered, but for some reason it's not. whyyyyyyy </3 
			//50% of GVar.GAME_WIDTH = 1050; //525
			//50% of GVar.GAME_HEIGHT = 470; //235
			//50% of Font = 30 //15
			//so makes sense (to me) that the X of font should start drawing at x = 435 because after you've written 3 letters (PAU)
			//    then you are at 3 * 30 = 90 = 525 (The 50% width mark)
			//    and when you do 50% of font subtracted from center = 235 - 15 = 220. 
			//So why is it not drawing dead center? Brian? MATH! I need help! (/ToT)/
			//I think Left and right it's fine, but vertically I think it's too high... :'( 
			if(tempDebugPausedPosition) {
				tempDebugPausedPosition = false;
				System.out.println("//////////////////////////////////////");
				System.out.println("GVar.GAME_WIDTH/2: " + GVar.GAME_WIDTH/2);
				System.out.println("pauseMsgLen/2: " + GVar.getPauseMsgLen()/2);
				System.out.println("currentFontSize: " + currentFontSize);
				System.out.println("pauseMsgLen/2 * currentFontSize: " + GVar.getPauseMsgLen()/2 * currentFontSize);
				System.out.println("GVar.GAME_WIDTH/2 - pauseMsgLen/2 * currentFontSize: " + (GVar.GAME_WIDTH/2 - GVar.getPauseMsgLen()/2 * currentFontSize));
				System.out.println("------------------------------------");
				System.out.println("GVar.GAME_HEIGHT/2: " + GVar.GAME_HEIGHT/2);
				System.out.println("currentFontSize/2: " + currentFontSize/2);
				System.out.println("GVar.GAME_HEIGHT/2 - currentFontSize/2: " + (GVar.GAME_HEIGHT/2 - currentFontSize/2));
				System.out.println("//////////////////////////////////////");
			}
		}
	}

	//Self-explanatory, resets Player defaults
	public void resetPlayerDefaults() {
		//Resets Players Position & selection:
		player.setX((float)level.getSpawnX());
		player.setY((float)level.getSpawnY());
		player.setCurrentPlayer(0);
		player.setHeight(Creature.DEFAULT_CREATURE_HEIGHT_BIG);
	}
	
	//Self-explanatory, resets Level defaults
	public void resetLevelDefaults() {
		//Resets Level Tile Position:
		level.setDebugScrollLevel(0);
	}

	/*************** GETTERS and SETTERS ***************/

	public Level getLevel() {
		return level;
	}

	public Player getPlayer() {
		return player;
	}
	
}
