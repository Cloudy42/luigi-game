package dev.lepauley.luigi.states;

import java.awt.Graphics;

import dev.lepauley.luigi.entities.creatures.Creature;
import dev.lepauley.luigi.entities.creatures.Player;
import dev.lepauley.luigi.general.GVar;
import dev.lepauley.luigi.general.Game;
import dev.lepauley.luigi.levels.Level;
import dev.lepauley.luigi.utilities.EnumPause;
import dev.lepauley.luigi.utilities.Utilities;

/*
 * Where actual game play is at
 */
public class GameState extends State {

	//Holds current Player and Level
	private Player player;
	private Level level;

	//Tracks Current Font Size
	public int currentFontSize;
	
	//Game Constructor
	public GameState(Game game) {
		
		//Sets State = "GameState"
		super(game,"GameState");
		
		//sets game variables (in State) to current game
		this.game = game;
		
		//Creates new Level
		level = new Level("res/levels/level 1-1.txt");
		
		//Creates new Player
		player = new Player(game, level.getSpawnX(), level.getSpawnY());
	}
	
	//Updates Player and Level (if game is NOT paused)
	@Override
	public void tick() {
		
		//Toggles Pause On/Off (only if not dead) AND not in "STOP" mode
		if(Game.keyManager.pauseToggle && !Game.gameHeader.getDead() && !GVar.getPauseMsg().equals(EnumPause.STOP.toString()))
			GVar.togglePause(EnumPause.PAUSED.toString());

		//If Game = UnPaused, tick
		//Note: Must tick level before player do to proper layer positioning
		if(!GVar.getPause()) {
			level.tick();
			player.tick();
		}
	}

	//Draws Level, header, player, and pause Message to screen
	@Override
	public void render(Graphics g) {

		//Shows BG Level
		//Note: Must render level before player do to proper layer positioning
		level.render(g);

		//Displays Header info
		Game.gameHeader.render(g);

		//Shows Player
		player.render(g);

		//If Game = Paused, display pause Message to screen
		if(GVar.getPause()) {

			//Sets font & font size
			currentFontSize = 30;
			g.setFont (GVar.getFont(GVar.defaultFont, currentFontSize));
			
			//Draws pause Message to screen with a shadow
			Utilities.drawShadowString(g, GVar.getPauseMsg(), GVar.GAME_WIDTH/2 - GVar.getPauseMsgLen()/2 * currentFontSize, GVar.GAME_HEIGHT/2 - currentFontSize/2, GVar.getShadowFont(currentFontSize));

			//The below is an audit I did when I realized pause message was not printing to screen in the center. Requires further research. 
			/*
			 *  This seems like it SHOULD be writing it perfectly centered, but for some reason it's not. whyyyyyyy </3 
			    50% of GVar.GAME_WIDTH = 1050; //525
			    50% of GVar.GAME_HEIGHT = 470; //235
			    50% of Font = 30 //15
			    so makes sense (to me) that the X of font should start drawing at x = 435 because after you've written 3 letters (PAU)
			        then you are at 3 * 30 = 90 = 525 (The 50% width mark)
			        and when you do 50% of font subtracted from center = 235 - 15 = 220. 
			    So why is it not drawing dead center? Brian? MATH! I need help! (/ToT)/
			    I think Left and right it's fine, but vertically I think it's too high... :'( 
			    I screenshotted and it's definitely off, but actually "works" because the bottom of the game is dirt, so it looks "normal"
		     *  here are results from below:

			 *  if(GVar.getDebug()) {
				print("//////////////////////////////////////");
				print("GVar.GAME_WIDTH/2: " + GVar.GAME_WIDTH/2);
				print("pauseMsgLen/2: " + GVar.getPauseMsgLen()/2);
				print("currentFontSize: " + currentFontSize);
				print("pauseMsgLen/2 * currentFontSize: " + GVar.getPauseMsgLen()/2 * currentFontSize);
				print("GVar.GAME_WIDTH/2 - pauseMsgLen/2 * currentFontSize: " + (GVar.GAME_WIDTH/2 - GVar.getPauseMsgLen()/2 * currentFontSize));
				print("------------------------------------");
				print("GVar.GAME_HEIGHT/2: " + GVar.GAME_HEIGHT/2);
				print("currentFontSize/2: " + currentFontSize/2);
				print("GVar.GAME_HEIGHT/2 - currentFontSize/2: " + (GVar.GAME_HEIGHT/2 - currentFontSize/2));
			 *  print("//////////////////////////////////////");
			 
			 * //////////////////////////////////////
				GVar.GAME_WIDTH/2: 525
				pauseMsgLen/2: 3
				currentFontSize: 30
				pauseMsgLen/2 * currentFontSize: 90
				GVar.GAME_WIDTH/2 - pauseMsgLen/2 * currentFontSize: 435
				------------------------------------
				GVar.GAME_HEIGHT/2: 235
				currentFontSize/2: 15
				GVar.GAME_HEIGHT/2 - currentFontSize/2: 220
		     * //////////////////////////////////////
			 */
		}
	}

	//Self-explanatory, resets Player defaults
	public void resetPlayerDefaults() {
		
		//Resets Player's Position
		player.setX((float)level.getSpawnX());
		player.setY((float)level.getSpawnY());
		
		//Resets Player's Character selection skin
		player.setCurrentPlayer(0);
		
		//Resets Player's Height (since death = small Mario)
		player.setHeight(Creature.DEFAULT_CREATURE_HEIGHT_BIG);
	}
	
	//Self-explanatory, resets Level defaults
	public void resetLevelDefaults() {
		
		//Sets Default Scroll Level Values
		level.setScrollLevelDefaults(0);
	}

	/*************** GETTERS and SETTERS ***************/

	//Gets Current Level
	public Level getLevel() {
		return level;
	}

	//Gets Current Player
	public Player getPlayer() {
		return player;
	}
	
}