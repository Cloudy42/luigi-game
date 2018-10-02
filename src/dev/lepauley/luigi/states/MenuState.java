package dev.lepauley.luigi.states;

import java.awt.Graphics;

import dev.lepauley.luigi.entities.creatures.Player;
import dev.lepauley.luigi.general.GVar;
import dev.lepauley.luigi.general.Game;
import dev.lepauley.luigi.levels.Level;

/*
 * Menu/starting screen (before game begins)
 */
public class MenuState extends State {

	//BG Level & Player(s)
	//Note: (May consider creating a unique menu level. if so, don't need to load level)
	private Level level;
	private Player player1, player2;
	
	//Menu Constructor
	public MenuState(Game game) {

		//Sets State = "GameState"
		super(game, "MenuState");

		//sets game variables (in State) to current game
		this.game = game;

		//May be worthwhile to just use the level and players created in GameState? Unsure if would pose issues/be better, just a thought.
		//Creates new Level
		level = new Level("res/levels/level 1-1.txt");
		
		//Creates new Player
		player1 = new Player(game, level.getSpawnX(), level.getSpawnY());
		player2 = new Player(game, level.getSpawnX() + Player.DEFAULT_CREATURE_WIDTH * 2, level.getSpawnY());
		
		//Just making Alan & Brian by default for menuScreen for funsies. I mean we DID make the game afterall ;P RESPECT!
		player1.setCurrentPlayer(1);
		player2.setCurrentPlayer(3);
	}
	
	//Updates Player and Level (if game is NOT paused)
	@Override
	public void tick() {
		//Selects # of players 
		//Note: if we had multiple characters or options and other settings and such, likely a better way to implement would be
		//      using index and incrementing with down and decrementing with up and looping at the caps. If we ever need, do that.
		if(GVar.getPlayerSelectCount() == 1 && Game.keyManager.down)
			GVar.setPlayerSelectCount(2);
		if(GVar.getPlayerSelectCount() == 2 && Game.keyManager.up)
			GVar.setPlayerSelectCount(1);
	}

	//Draws Level, header, player, and player2 IF 2 player mode selected
	@Override
	public void render(Graphics g) {

		//Shows BG Level
		//Note: Must render level before player do to proper layer positioning
		level.render(g);

		//Displays Header info
		Game.gameHeader.render(g);

		//Shows Player1
		player1.render(g);
		
		//If 2 Player mode is selected, display Player 2
		//Note, I did >1 rather than == 2. Allows for more freedom if we add more players.
		if(GVar.getPlayerSelectCount() > 1)
			player2.render(g);
	}

}
