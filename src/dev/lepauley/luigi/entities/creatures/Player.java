package dev.lepauley.luigi.entities.creatures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
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
		//Note: I subtracted 2 from the height bounds to offset it since y = 1 and we need the play to have their animation on the ground
		//      So this allows us to move it down an extra pixel. We need the bounding box offset by 1 or else it cannot "slide"
		bounds.x = 6;
		bounds.y = 1;
		bounds.width = 20;
		bounds.height = Creature.DEFAULT_CREATURE_HEIGHT_BIG - 2;
		
		//This is used for the collision indicator boxes, and ultimately what I used for the bounding box "display" as well (so can still see player)
		collisionBoundsUp.x = bounds.x; collisionBoundsUp.y = bounds.y; collisionBoundsUp.width = bounds.width; collisionBoundsUp.height = collisionBoundsSize;
		collisionBoundsDown.x = bounds.x; collisionBoundsDown.y = bounds.y + bounds.height - collisionBoundsSize; collisionBoundsDown.width = bounds.width; collisionBoundsDown.height = collisionBoundsSize;
		collisionBoundsLeft.x = bounds.x; collisionBoundsLeft.y = bounds.y; collisionBoundsLeft.width = collisionBoundsSize; collisionBoundsLeft.height = bounds.height;
		collisionBoundsRight.x = bounds.x + bounds.width - collisionBoundsSize; collisionBoundsRight.y = bounds.y; collisionBoundsRight.width = collisionBoundsSize; collisionBoundsRight.height = bounds.height;
		
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

		//Draw debug box(es)...IF in debug mode
		if(GVar.getDebug()) {
			g.setColor(Color.red);
			
			//This was the original "filled Rectangle" but I switched to the bars
			/*g.fillRect((int) (x+bounds.x - handler.getGameCamera().getxOffset()), 
					(int) (y+bounds.y - handler.getGameCamera().getyOffset()), 
					bounds.width, bounds.height);*/
			//Up
				g.fillRect((int) (x+collisionBoundsUp.x - handler.getGameCamera().getxOffset()), 
						   (int) (y+collisionBoundsUp.y - handler.getGameCamera().getyOffset()), 
						          collisionBoundsUp.width, collisionBoundsUp.height);		
			//Down
				g.fillRect((int) (x+collisionBoundsDown.x - handler.getGameCamera().getxOffset()), 
						   (int) (y+collisionBoundsDown.y - handler.getGameCamera().getyOffset()), 
						          collisionBoundsDown.width, collisionBoundsDown.height);		
			//Left
				g.fillRect((int) (x+collisionBoundsLeft.x - handler.getGameCamera().getxOffset()), 
						   (int) (y+collisionBoundsLeft.y - handler.getGameCamera().getyOffset()), 
						          collisionBoundsLeft.width, collisionBoundsLeft.height);		
			//Right
				g.fillRect((int) (x+collisionBoundsRight.x - handler.getGameCamera().getxOffset()), 
						   (int) (y+collisionBoundsRight.y - handler.getGameCamera().getyOffset()), 
						          collisionBoundsRight.width, collisionBoundsRight.height);		

			//Draw collision indicator boxes
			g.setColor(Color.yellow);

			//Up
			if(collisionUp)
				g.fillRect((int) (x+collisionBoundsUp.x - handler.getGameCamera().getxOffset()), 
						   (int) (y+collisionBoundsUp.y - handler.getGameCamera().getyOffset()), 
						          collisionBoundsUp.width, collisionBoundsUp.height);		
			//Down
			if(collisionDown)
				g.fillRect((int) (x+collisionBoundsDown.x - handler.getGameCamera().getxOffset()), 
						   (int) (y+collisionBoundsDown.y - handler.getGameCamera().getyOffset()), 
						          collisionBoundsDown.width, collisionBoundsDown.height);		
			//Left
			if(collisionLeft)
				g.fillRect((int) (x+collisionBoundsLeft.x - handler.getGameCamera().getxOffset()), 
						   (int) (y+collisionBoundsLeft.y - handler.getGameCamera().getyOffset()), 
						          collisionBoundsLeft.width, collisionBoundsLeft.height);		
			//Right
			if(collisionRight)
				g.fillRect((int) (x+collisionBoundsRight.x - handler.getGameCamera().getxOffset()), 
						   (int) (y+collisionBoundsRight.y - handler.getGameCamera().getyOffset()), 
						          collisionBoundsRight.width, collisionBoundsRight.height);		
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
		GVar.setPlayer1CurrentCharacter(currentPlayer);
	}
	
}
