package dev.lepauley.luigi.states;

import java.awt.Graphics;

import dev.lepauley.luigi.entities.creatures.Creature;
import dev.lepauley.luigi.entities.creatures.Player;
import dev.lepauley.luigi.general.GVar;
import dev.lepauley.luigi.general.Game;
import dev.lepauley.luigi.general.Handler;
import dev.lepauley.luigi.levels.Level;
import dev.lepauley.luigi.utilities.EnumPause;
import dev.lepauley.luigi.utilities.Utilities;

/*
 * Where actual game play is at
 */
public class GameState extends State {

	//Holds current Player and Level
	private Player player;
	private Level[] level = {new Level(handler, "res/levels/level 0-1.txt")
							, new Level(handler, "res/levels/level 1-1.txt")
							, new Level(handler, "res/levels/level 1-2.txt")
							, new Level(handler, "res/levels/level 1-3.txt")
							, new Level(handler, "res/levels/level 1-4.txt")
							, new Level(handler, "res/levels/level 2-1.txt")
							, new Level(handler, "res/levels/level 2-2.txt")};
	
	//Tracks current Level
	private int lvl = 0;

	//Tracks Current Font Size
	public int currentFontSize;
	
	//Game Constructor
	public GameState(Handler handler) {
		
		//Sets State = "GameState"
		super(handler,"GameState");
		
		//Sets level object to that of handler's level object
		handler.setLevel(level);
		
		//Creates new Player
		player = new Player(handler, level[lvl].getSpawnX(), level[lvl].getSpawnY());		
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
		
		//Temporarily disabled since keep hitting the button too fast (plus there is likely a smarter way to do this)
		//Resets Player's Character selection skin If starting new game
		//if(!GVar.getContinueGame() && (player.getX() == level[lvl].getSpawnX() || player.getY() == level[lvl].getSpawnY()))
		//	player.setCurrentPlayer(0);
		
		//Resets Player's Position
		if(!GVar.getContinueGame() || GVar.getNewLevel()) {
			player.setX((float)level[lvl].getSpawnX());
			player.setY((float)level[lvl].getSpawnY());
		}
		
		//Resets Player's Height (since death = small Mario)
		player.setHeight(Creature.DEFAULT_CREATURE_HEIGHT_BIG);
	}
	
	//Self-explanatory, resets Level defaults
	public void resetLevelDefaults() {
		
	}

	/*************** GETTERS and SETTERS ***************/

	//Gets Current Level
	public Level getLevel() {
		return level[lvl];
	}

	//Sets Current Level
	public void setLevel(int lvl) {
		
		//I put this in because I'm getting lvl = -5 which is an array out of bounds exception error. 
		//I haven't been able to look into the cause/fix yet, just got tired of deleting settings file
		//everytime this happened so put this fix in until I can look into it.
		if(lvl < 0)
			lvl = 0;

		this.lvl = lvl;

		//Sets Current World and Level based on lvl
		Game.gameHeader.setCurrentWorld(level[lvl].getWorld());
		Game.gameHeader.setCurrentLevel(level[lvl].getLevel());

		//Updates Player
		player = new Player(handler, level[lvl].getSpawnX(), level[lvl].getSpawnY());

		//Sets current Song for level IF do not select continue
		if(!GVar.getContinueGame() || GVar.getNewLevel())
			Game.gameAudio.setCurrentMusic(level[lvl].getLevelMusic());
			
	}

	//Toggles level (increases to next level. If hits cap, resets to first level)
	public void toggleLevel() {

		//Toggles that we are setting a new level, which is used to bypass continueGame and reset some defaults
		GVar.setNewLevel(true);
		
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

		//Toggles that we are done setting a new level
		GVar.setNewLevel(false);		

	}
	
	//Gets Current Player
	public Player getPlayer() {
		return player;
	}
	
}