package dev.lepauley.luigi.states;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import dev.lepauley.luigi.GVar;
import dev.lepauley.luigi.Game;
import dev.lepauley.luigi.entities.creatures.Player;
import dev.lepauley.luigi.gfx.Assets;
import dev.lepauley.luigi.levels.Level;
import dev.lepauley.luigi.utilities.Utilities;

/*
 * Menu/starting screen (before game begins)
 */
public class MenuState extends State {

	//Font Info
	private int currentFontSize;
	
	//BG Level & Player (May consider creating a unique menu level)
	private Level level;
	private Player player1, player2;
	
	public MenuState(Game game) {
		super(game);
		this.game = game;
		level = new Level("res/levels/1-1.txt");
		player1 = new Player(game, level.getSpawnX(), level.getSpawnY());
		player2 = new Player(game, level.getSpawnX() + Player.DEFAULT_CREATURE_WIDTH * 2, level.getSpawnY());
		
		//Useless, just using to force selection of character and too lazy to make a better selector :P
		for(int i = 0; i < 5; i++) player2.setCurrentPlayer();
	}
	
	@Override
	public void tick() {
		//Selects # of players 
		if(GVar.getPlayerSelectCount() == 1 && game.getKeyManager().down)
			GVar.setPlayerSelectCount(2);
		if(GVar.getPlayerSelectCount() == 2 && game.getKeyManager().up)
			GVar.setPlayerSelectCount(1);
	}

	@Override
	public void render(Graphics g) {
		//Shows BG Level & Player
		level.render(g);
		player1.render(g);
		
		//If 2 Player game is selected, display Player 2
		if(GVar.getPlayerSelectCount() == 2)
			player2.render(g);

		//Menu
		g.drawImage(Assets.menu,215,50,625,185,null);

		g.setFont (GVar.FONT_20);
		currentFontSize = GVar.FONT_20.getSize();

		//Header Info
		Utilities.drawShadowString(g, "MARIO",	215, 20, GVar.FONT_20_SHADOW);
		Utilities.drawShadowString(g, "000000",	215, 20 + currentFontSize, GVar.FONT_20_SHADOW);

		g.drawImage(Assets.coin,410,25,null);
		Utilities.drawShadowString(g, "x00",	425, 20 + currentFontSize, GVar.FONT_20_SHADOW);

		Utilities.drawShadowString(g, "WORLD", 	585, 20, GVar.FONT_20_SHADOW);
		Utilities.drawShadowString(g, "1-1",   	585  + currentFontSize * 1, 20 + currentFontSize, GVar.FONT_20_SHADOW);

		Utilities.drawShadowString(g, "TIME", 	785, 20, GVar.FONT_20_SHADOW);

		//Player Select
		g.drawImage(Assets.toad,380,(int)(260 + (GVar.getPlayerSelectCount() - 1) * currentFontSize * 1.5),null);
		Utilities.drawShadowString(g, "1 PLAYER GAME",	400, 275, GVar.FONT_20_SHADOW);
		Utilities.drawShadowString(g, "2 PLAYER GAME",	400, (int)(275 + currentFontSize * 1.5), GVar.FONT_20_SHADOW);

		//High Score
		Utilities.drawShadowString(g, "TOP- 000000",	400 + currentFontSize * 1, (int)(275 + currentFontSize * 3), GVar.FONT_20_SHADOW);
		
		
	}

}
