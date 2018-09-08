package dev.lepauley.luigi.entities.creatures;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import dev.lepauley.luigi.Game;
import dev.lepauley.luigi.GVar;
import dev.lepauley.luigi.gfx.Assets;


/*
 * The player that our users will control.
 */
public class Player extends Creature{
	//So we can access things like key manager
	private Game game;
	
	//Keeps Track of Current Player
	private BufferedImage currentPlayer;
	
	public Player(Game game, float x, float y) {
		super(x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);
		this.game = game;
		this.currentPlayer = Assets.player1;
	}

	@Override
	public void tick() {
		//Gets movement using speed
		getInput();		
		//Sets position using movement
		move();
	}
	
	private void getInput() {
		//Very important that every time we call this method we set xMove and yMove to 0
		xMove = 0;
		yMove = 0;
		
		//Setting x/y move to a certain speed, THEN moving player that much
		if(game.getKeyManager().up)
			yMove = -speed;
		if(game.getKeyManager().down)
			yMove = speed;
		if(game.getKeyManager().left)
			xMove = -speed;
		if(game.getKeyManager().right)
			xMove = speed;
		
		//Scale player Down/Up AND adjust speed
		if(game.getKeyManager().scaleDown)
			if(GVar.getMultiplier() > GVar.MIN_SCALE)
				GVar.changeMultiplier(-1);
				setSpeed(DEFAULT_SPEED * GVar.getMultiplier());
		if(game.getKeyManager().scaleUp)
			if(GVar.getMultiplier() < GVar.MAX_SCALE)
				GVar.changeMultiplier(1);
				setSpeed(DEFAULT_SPEED * GVar.getMultiplier());

		//Swap Current Player
		if(game.getKeyManager().changePlayer)
			setCurrentPlayer();
	}

	@Override
	public void render(Graphics g) {
		//Draws player. Utilizes Cropping method via SpriteSheet class to only pull part of image
		// - Takes in integers, not floats, so need to cast x and y position:
		// - Image Observer = null. We won't use in tutorial
		g.drawImage(currentPlayer, (int) x, (int) y, (int) (width * GVar.getMultiplier()), (int) (height * GVar.getMultiplier()), null);
	}

	/*************** GETTERS and SETTERS ***************/
	
	public BufferedImage getCurrentPlayer() {
		return currentPlayer;
	}

	//Def should be cleaned up, likely using an array or something and looping through. Just wanted to test:
	public void setCurrentPlayer() {
		if(currentPlayer == Assets.player1)
			this.currentPlayer = Assets.player2;
		else if(currentPlayer == Assets.player2)
			this.currentPlayer = Assets.player3;
		else if(currentPlayer == Assets.player3)
			this.currentPlayer = Assets.player4;
		else if(currentPlayer == Assets.player4)
			this.currentPlayer = Assets.player5;
		else if(currentPlayer == Assets.player5)
			this.currentPlayer = Assets.player6;
		else
			this.currentPlayer = Assets.player1;
	}

	
}
