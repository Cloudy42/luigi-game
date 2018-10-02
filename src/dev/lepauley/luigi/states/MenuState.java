package dev.lepauley.luigi.states;

import java.awt.Graphics;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import dev.lepauley.luigi.entities.creatures.Player;
import dev.lepauley.luigi.general.GVar;
import dev.lepauley.luigi.general.Game;
import dev.lepauley.luigi.levels.Level;

/*
 * Menu/starting screen (before game begins)
 */
public class MenuState extends State {

	//BG Level & Player (May consider creating a unique menu level)
	private Level level;
	private Player player1, player2;
	
	public MenuState(Game game) {
		super(game, "MenuState");
		this.game = game;
		level = new Level("res/levels/level 1-1.txt");
		player1 = new Player(game, level.getSpawnX(), level.getSpawnY());
		player2 = new Player(game, level.getSpawnX() + Player.DEFAULT_CREATURE_WIDTH * 2, level.getSpawnY());
		
		//Just making Alan & Brian by default for menuScreen
		player1.setCurrentPlayer(1);
		player2.setCurrentPlayer(3);
	}
	
	@Override
	public void tick() {
		//Selects # of players 
		if(GVar.getPlayerSelectCount() == 1 && game.getKeyManager().down)
			try {
				GVar.setPlayerSelectCount(2);
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(GVar.getPlayerSelectCount() == 2 && game.getKeyManager().up)
			try {
				GVar.setPlayerSelectCount(1);
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	public void render(Graphics g) {
		//Shows BG Level & Player
		level.render(g);

		//Displays Header info
		Game.gameHeader.render(g);

		player1.render(g);
		
		//If 2 Player game is selected, display Player 2
		if(GVar.getPlayerSelectCount() == 2)
			player2.render(g);
	}

}
