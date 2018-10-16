package dev.lepauley.luigi.states;

import java.awt.Graphics;

import dev.lepauley.luigi.entities.creatures.Player;
import dev.lepauley.luigi.general.GVar;
import dev.lepauley.luigi.general.Game;
import dev.lepauley.luigi.levels.Level;
import dev.lepauley.luigi.utilities.EnumSFX;

/*
 * Menu/starting screen (before game begins)
 */
public class MenuState extends State {

	//BG Level & Player(s)
	//Note: (May consider creating a unique menu level. if so, don't need to load level)
	private Level level;
	private Player player1, player2;
	
	//Tracks currentSelection
	private int currentSelection;
	
	//Menu Constructor
	public MenuState(Game game) {

		//Sets State = "GameState"
		super(game, "MenuState");

		//sets game variables (in State) to current game
		this.game = game;

		//May be worthwhile to just use the level and players created in GameState? Unsure if would pose issues/be better, just a thought.
		//Creates new Level
		level = new Level(game, "res/levels/level 1-1.txt");
		
		//Creates new Player
		player1 = new Player(game, level.getSpawnX(), level.getSpawnY());
		player2 = new Player(game, level.getSpawnX() + Player.DEFAULT_CREATURE_WIDTH * 2, level.getSpawnY());
		
		//Just making Alan & Brian by default for menuScreen for funsies. I mean we DID make the game afterall ;P RESPECT!
		player1.setCurrentPlayer(1);
		player2.setCurrentPlayer(3);
		
		//Sets current selection at the top
		currentSelection = 0;
		
		Game.setLoaded(true);

		//If ContinueGame = true, then set current Selection = Continue so there by default
		//Wouldn't want someone to accidentally start over game if continue was an option.
		//It may be early but I can't think of a prettier way to tell it "Continue" rather than #2. I may need 
		//a helper method for this.
		if(GVar.getContinueGame())
			currentSelection = 2;
	}
	
	//Updates Player and Level (if game is NOT paused)
	@Override
	public void tick() {

		//Changes menu selection down 
		if(Game.keyManager.down) {
			currentSelection++;
			
			//If at bottom of list and press down it goes back to the top of list
			if(currentSelection == Game.menuManager.getSize())
				currentSelection = 0;
			
			//If Cannot continue game, account for that option missing from menu
			if(!GVar.getContinueGame() && currentSelection == Game.menuManager.getSize()-1)
				currentSelection = 0;			

			//Plays menu selection audio
			playMenuSelectionAudio();
		}

		//Changes menu selection up 
		if(Game.keyManager.up) {
			currentSelection--;

			//Note: Below order is very important. Cannot swap
			//If Cannot continue game, account for that option missing from menu
			if(!GVar.getContinueGame() && currentSelection < 0)
				currentSelection = Game.menuManager.getSize() - 2;			

			//If at top of list and press up it goes back to the bottom of list
			if(currentSelection < 0)
				currentSelection = Game.menuManager.getSize() - 1;

			//Plays menu selection audio
			playMenuSelectionAudio();
		}
		
		//Sets player count based on selection
		//Note: If a player goes from top and cycles down, they may be in option menu as a 1 player game
		//      and vice versa
		if(currentSelection == 0)
			GVar.setPlayerSelectCount(1);
		if(currentSelection == 1)
			GVar.setPlayerSelectCount(2);
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
	
	//Plays character switch audio.
	public void playMenuSelectionAudio() {
		Game.gameAudio.pauseAudioStagingArea("SFX");
		Game.gameAudio.playAudioStagingArea("SFX", EnumSFX.Bump.toString());
	}

	
	/*************** GETTERS and SETTERS ***************/
	
	//Gets currentSelection
	public int getCurrentSelection() {
		return currentSelection;
	}

	//Sets currentSelection
	public void setCurrentSelection(int i) {
		currentSelection = i;
	}

}
