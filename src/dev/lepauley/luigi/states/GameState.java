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
	private Level[] level = {new Level("res/levels/level 1-1.txt")
			               , new Level("res/levels/level 1-2.txt")
    					   , new Level("res/levels/level 1-3.txt")
    					   , new Level("res/levels/level 1-4.txt")};	

	//Tracks current Level
	private int lvl = 0;

	//Tracks Current Font Size
	public int currentFontSize;
	
	//Game Constructor
	public GameState(Game game) {
		
		//Sets State = "GameState"
		super(game,"GameState");
		
		//sets game variables (in State) to current game
		this.game = game;
		
		//Creates new Player
		player = new Player(game, level[lvl].getSpawnX(), level[lvl].getSpawnY());
	}
	
	//Updates Player and Level (if game is NOT paused)
	@Override
	public void tick() {
		
		//Toggles Pause On/Off (only if not dead)
		if(Game.keyManager.start && !Game.gameHeader.getDead())
			GVar.togglePause(EnumPause.PAUSED.toString());

		//If Game = UnPaused, tick
		//Note: Must tick level before player do to proper layer positioning
		if(!GVar.getPause()) {
			level[lvl].tick();
			player.tick();
		}
		//If Game = "Stop" mode, tick player only (if not dead)
		else if(GVar.getStop() && !Game.gameHeader.getDead()) {
			player.tick();
		}
		
}

	//Draws Level, header, player, and pause Message to screen
	@Override
	public void render(Graphics g) {

		//Shows BG Level
		//Note: Must render level before player do to proper layer positioning
		level[lvl].render(g);

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
			Utilities.drawShadowString(g, GVar.getPauseMsg(), GVar.GAME_WIDTH/2 - GVar.getPauseMsgLen() * (GVar.getPauseMsgLen()/2 -1)/GVar.getPauseMsgLen() * currentFontSize, GVar.GAME_HEIGHT/2 - currentFontSize/2+currentFontSize, GVar.getShadowFont(currentFontSize));

		}
	}

	//Self-explanatory, resets Player defaults
	public void resetPlayerDefaults() {
		
		//Resets Player's Position
		player.setX((float)level[lvl].getSpawnX());
		player.setY((float)level[lvl].getSpawnY());
		
		//Resets Player's Character selection skin
		player.setCurrentPlayer(0);
		
		//Resets Player's Height (since death = small Mario)
		player.setHeight(Creature.DEFAULT_CREATURE_HEIGHT_BIG);
	}
	
	//Self-explanatory, resets Level defaults
	public void resetLevelDefaults() {
		
		//Sets Default Scroll Level Values
		level[lvl].setScrollLevelDefaults(0);
	}

	/*************** GETTERS and SETTERS ***************/

	//Gets Current Level
	public Level getLevel() {
		return level[lvl];
	}

	//Sets Current Level
	public void setLevel(int lvl) {
		this.lvl = lvl;
		Game.gameAudio.setCurrentMusic(level[lvl].getLevelMusic());
	}

	//Toggles level (increases to next level. If hits cap, resets to first level)
	public void toggleLevel() {
		lvl++;
		if(lvl == level.length)
			lvl = 0;		

		//Sets currentLevel
		setLevel(lvl);
		
		//Resets defaults (EXCEPT header)
		Game.gameAudio.resetDefaults();
		//Game.gameHeader.resetDefaults();
		resetLevelDefaults();
		resetPlayerDefaults();
		
		//plays Level song
		Game.gameAudio.playAudioStagingArea("MUSIC", Game.gameAudio.getCurrentMusic());

	}
	
	//Gets Current Player
	public Player getPlayer() {
		return player;
	}
	
}