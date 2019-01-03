package dev.lepauley.luigi.entities.creatures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import dev.lepauley.luigi.general.GVar;
import dev.lepauley.luigi.general.Game;
import dev.lepauley.luigi.general.Handler;
import dev.lepauley.luigi.gfx.Assets;

/*
 * The player that our users will control.
 */

public class Player extends Creature{

	//Keeps Track of Current Player (Animation skin/palette)
	private int currentPlayer;
	
	//Various player selection Sprites (Alive and dead since not animating yet)
	//Once animating, we can consolidate
	//Note: Using arrays because it makes swapping players much easier, as you can
	//      increment index rather than doing series of if/else checks
	private BufferedImage[] playerImage = {Assets.player1, Assets.player2, Assets.player3
										 , Assets.player4, Assets.player5, Assets.player6};
	private BufferedImage[] playerImageDead = {Assets.player1Dead, Assets.player2Dead, Assets.player3Dead
			 								 , Assets.player4Dead, Assets.player5Dead, Assets.player6Dead};
	
	//Constructor that sets up some defaults
	public Player(Handler handler, float x, float y) {
		super(handler, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT_BIG);

		//Sets currentPlayer = index 0 (used for array) if starting new game
		if(GVar.getContinueGame())
			currentPlayer = GVar.getPlayer1CurrentCharacter();
		
		//Boundary box for player
		//Note: I subtracted 1 from the height bounds to offset it since we need the play to have their animation on the ground
		//      but we need the bounding box offset by 1 or else it cannot "slide"
		bounds.x = 8;
		bounds.y = 1;
		bounds.width = 20;
		bounds.height = Creature.DEFAULT_CREATURE_HEIGHT_BIG - 2;
		
	}

	@Override
	public void tick() {

		//Gets movement using speed
		getInput();		
		
		//Sets position using movement
		move();
		
		//Centers camera on player
		handler.getGameCamera().centerOnEntity(this);
	}
	
	//Takes user input and performs various actions
	private void getInput() {

		//Very important that every time we call this method we set xMove and yMove to 0
		xMove = 0;
		yMove = 0;
		
		//Setting x/y move to a certain speed, THEN moving player that much
		if(Game.keyManager.up) 
			yMove = -speed;
		
		if(Game.keyManager.down)
			yMove = speed;
		
		if(Game.keyManager.left)
			xMove = -speed;

		if(Game.keyManager.right)
			xMove = speed;
		
		//Scale player Down
		if(Game.keyManager.scaleDown)
			if(GVar.getMultiplier() > GVar.MIN_SCALE) {
				GVar.incrementMultiplier(-1);

				//Adjust speed accordingly
				setSpeed(DEFAULT_SPEED * GVar.getMultiplier());
			}

		//Scale player Up
		if(Game.keyManager.scaleUp)
			if(GVar.getMultiplier() < GVar.MAX_SCALE) {
				GVar.incrementMultiplier(1);
				
				//Adjust speed accordingly
				setSpeed(DEFAULT_SPEED * GVar.getMultiplier());
			}

		//Swap Current Player for next in lineup
		if(Game.keyManager.changePlayer)
			incrementCurrentPlayer();
	}

	@Override
	public void render(Graphics g) {
		
		//If player = Dead, draw player as dead sprite (short term solution):
		if(Game.gameHeader.getDead()) {
		
			//Need to readjust height too since shorter than Big Luigi
			this.setHeight(Creature.DEFAULT_CREATURE_HEIGHT_SMALL);
			g.drawImage(playerImageDead[currentPlayer], (int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()), (int) (width * GVar.getMultiplier()), (int) (height * GVar.getMultiplier()), null);

		//If player = Alive, draw player as alive sprite
		//We're not reviving player, so don't need to set height back to Big here
		} else {
			//Draws player. Utilizes Cropping method via SpriteSheet class to only pull part of image
			// - Takes in integers, not floats, so need to cast x and y position:
			// - Image Observer = null. We won't use in tutorial
			g.drawImage(playerImage[currentPlayer], (int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()), (int) (width * GVar.getMultiplier()), (int) (height * GVar.getMultiplier()), null);
		}
		
		//Draw debug box...IF in debug mode
		if(GVar.getDebug()) {
			g.setColor(Color.red);
			g.fillRect((int) (x+bounds.x - handler.getGameCamera().getxOffset()), 
					(int) (y+bounds.y - handler.getGameCamera().getyOffset()), 
					bounds.width, bounds.height);
		}
	}
	
	/*************** GETTERS and SETTERS ***************/
	
	//Gets current Player (or sprite skin swapping)
	public int getCurrentPlayer() {
		return currentPlayer;
	}

	//Selects Specific character from lineup
	public void setCurrentPlayer(int i) {
		currentPlayer = i;
	}

	//Selects next Player in lineup
	public void incrementCurrentPlayer() {
		currentPlayer++;
		if(currentPlayer > playerImage.length-1)
			currentPlayer = 0;
	}
	
}
