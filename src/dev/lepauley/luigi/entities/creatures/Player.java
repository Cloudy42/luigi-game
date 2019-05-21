package dev.lepauley.luigi.entities.creatures;

import static dev.lepauley.luigi.utilities.Utilities.print;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import dev.lepauley.luigi.general.GVar;
import dev.lepauley.luigi.general.Game;
import dev.lepauley.luigi.general.Handler;
import dev.lepauley.luigi.gfx.Animation;
import dev.lepauley.luigi.gfx.Assets;
import dev.lepauley.luigi.utilities.ImageFlip;

/*
 * The player that our users will control.
 */

public class Player extends Creature{

	//Keeps Track of Current Player (Animation skin/palette)
	private int currentPlayer;
	
	//Animations
	private Animation animStand, animRun, animJump, animDuck, animDead;
	private int animSpeed = 180;
	
	//Various player selection Sprites (Alive and dead since not animating yet)
	//Once animating, we can consolidate
	//Note: Using arrays because it makes swapping players much easier, as you can
	//      increment index rather than doing series of if/else checks
	private BufferedImage[][] playerImage_Stand = {Assets.player1_stand, Assets.player2_stand, Assets.player3_stand
	   							                 , Assets.player4_stand, Assets.player5_stand, Assets.player6_stand};
	private BufferedImage[][] playerImage_Run = {Assets.player1_run, Assets.player2_run, Assets.player3_run
										   	   , Assets.player4_run, Assets.player5_run, Assets.player6_run};
	private BufferedImage[][] playerImage_Jump = {Assets.player1_jump, Assets.player2_jump, Assets.player3_jump
											   , Assets.player4_jump, Assets.player5_jump, Assets.player6_jump};
	private BufferedImage[][] playerImage_Duck = {Assets.player1_duck, Assets.player2_duck, Assets.player3_duck
											   , Assets.player4_duck, Assets.player5_duck, Assets.player6_duck};
	private BufferedImage[][] playerImage_Dead = {Assets.player1_Dead, Assets.player2_Dead, Assets.player3_Dead
			 								   , Assets.player4_Dead, Assets.player5_Dead, Assets.player6_Dead};

	//Keeps track of all "players" facing right
	private boolean[] rightArray = new boolean[playerImage_Stand.length];
	
	//Default Player Bounds
	public static final int DEFAULT_BOUNDS_X = 6, DEFAULT_BOUNDS_Y = 1
			              , DEFAULT_BOUNDS_WIDTH = 20, DEFAULT_BOUNDS_HEIGHT = Creature.DEFAULT_CREATURE_HEIGHT_BIG - 2;
	
	public static final int DEFAULT_COLLISION_BOUNDS_UP_X = DEFAULT_BOUNDS_X, DEFAULT_COLLISION_BOUNDS_UP_Y = DEFAULT_BOUNDS_Y
			              , DEFAULT_COLLISION_BOUNDS_UP_WIDTH = DEFAULT_BOUNDS_WIDTH, DEFAULT_COLLISION_BOUNDS_UP_HEIGHT = DEFAULT_COLLISION_BOUNDS_SIZE;
	
	public static final int DEFAULT_COLLISION_BOUNDS_DOWN_X = DEFAULT_BOUNDS_X, DEFAULT_COLLISION_BOUNDS_DOWN_Y = DEFAULT_BOUNDS_Y + DEFAULT_BOUNDS_HEIGHT - DEFAULT_COLLISION_BOUNDS_SIZE 
            			  , DEFAULT_COLLISION_BOUNDS_DOWN_WIDTH = DEFAULT_BOUNDS_WIDTH, DEFAULT_COLLISION_BOUNDS_DOWN_HEIGHT = DEFAULT_COLLISION_BOUNDS_SIZE;
	
	public static final int DEFAULT_COLLISION_BOUNDS_LEFT_X = DEFAULT_BOUNDS_X, DEFAULT_COLLISION_BOUNDS_LEFT_Y = DEFAULT_BOUNDS_Y
			  			  , DEFAULT_COLLISION_BOUNDS_LEFT_WIDTH = DEFAULT_COLLISION_BOUNDS_SIZE, DEFAULT_COLLISION_BOUNDS_LEFT_HEIGHT = DEFAULT_BOUNDS_HEIGHT;
	
	public static final int DEFAULT_COLLISION_BOUNDS_RIGHT_X = DEFAULT_BOUNDS_X + DEFAULT_BOUNDS_WIDTH - DEFAULT_COLLISION_BOUNDS_SIZE, DEFAULT_COLLISION_BOUNDS_RIGHT_Y = DEFAULT_BOUNDS_Y
			  			  , DEFAULT_COLLISION_BOUNDS_RIGHT_WIDTH = DEFAULT_COLLISION_BOUNDS_SIZE, DEFAULT_COLLISION_BOUNDS_RIGHT_HEIGHT = DEFAULT_BOUNDS_HEIGHT;

	//Constructor that sets up some defaults
	public Player(Handler handler, float x, float y) {
		super(handler, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT_BIG);

		//Sets currentPlayer = index 0 (used for array) if starting new game
		if(GVar.getContinueGame())
			currentPlayer = GVar.getPlayer1CurrentCharacter();
		
		//Boundary box for player
		//Note: I subtracted 2 from the height bounds to offset it since y = 1 and we need the play to have their animation on the ground
		//      So this allows us to move it down an extra pixel. We need the bounding box offset by 1 or else it cannot "slide"
		bounds = setBounds(DEFAULT_BOUNDS_X,DEFAULT_BOUNDS_Y,DEFAULT_BOUNDS_WIDTH,DEFAULT_BOUNDS_HEIGHT);
		
		//This is used for the collision indicator boxes, and ultimately what I used for the bounding box "display" as well (so can still see player)
		collisionBoundsUp = setBounds(DEFAULT_COLLISION_BOUNDS_UP_X, DEFAULT_COLLISION_BOUNDS_UP_Y, DEFAULT_COLLISION_BOUNDS_UP_WIDTH, DEFAULT_COLLISION_BOUNDS_UP_HEIGHT);
		collisionBoundsDown = setBounds(DEFAULT_COLLISION_BOUNDS_DOWN_X, DEFAULT_COLLISION_BOUNDS_DOWN_Y, DEFAULT_COLLISION_BOUNDS_DOWN_WIDTH, DEFAULT_COLLISION_BOUNDS_DOWN_HEIGHT);
		collisionBoundsLeft = setBounds(DEFAULT_COLLISION_BOUNDS_LEFT_X, DEFAULT_COLLISION_BOUNDS_LEFT_Y, DEFAULT_COLLISION_BOUNDS_LEFT_WIDTH, DEFAULT_COLLISION_BOUNDS_LEFT_HEIGHT);
		collisionBoundsRight = setBounds(DEFAULT_COLLISION_BOUNDS_RIGHT_X, DEFAULT_COLLISION_BOUNDS_RIGHT_Y, DEFAULT_COLLISION_BOUNDS_RIGHT_WIDTH, DEFAULT_COLLISION_BOUNDS_RIGHT_HEIGHT);
		
		//Sets current animations based on current Player
		setCurrentAnimations(false);
	}

	@Override
	public void tick() {

		//Animations
		animStand.tick();
		animRun.tick();
		animJump.tick();
		animDuck.tick();
		animDead.tick();
		
		//Gets movement using speed
		getInput();		
		
		//Checks whether player is falling and applies gravity if needed:
		
		//applies gravity
		airborne();
		/*if(!collisionDown) {
			airborne();
		}*/
		
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

		//Handles player Jump
		if(Game.keyManager.jump && !airborne) {
			airborne = true;
			gravity = -speed*5.5f;
			yMove = gravity;
		}
		
		//Handles player Movement
		//if(Game.keyManager.up)
			//yMove = -speed;
		
		if(Game.keyManager.down)
			yMove = speed;
		
		if(Game.keyManager.left) {
			xMove = -speed;
			setRight(false);
		}

		if(Game.keyManager.right) {
			xMove = speed;
			setRight(true);
		}
		
		//Scale player Down
		if(Game.keyManager.scaleDown)
			if(GVar.getMultiplier() > GVar.MIN_SCALE) {
				GVar.incrementMultiplier(-1);

				//Adjust speed accordingly
				setSpeed(DEFAULT_SPEED * GVar.getMultiplier());
				print(speed);
				
				//Adjust bounding box & collision indicators accordingly
				updateAllBounds();
			}

		//Scale player Up
		if(Game.keyManager.scaleUp)
			if(GVar.getMultiplier() < GVar.MAX_SCALE) {
				GVar.incrementMultiplier(1);
				
				//Adjust speed accordingly
				setSpeed(DEFAULT_SPEED * GVar.getMultiplier());
				print(speed);
				
				//Adjust bounding box & collision indicators accordingly
				updateAllBounds();
			}

		//Swap Current Player for next in lineup
		if(Game.keyManager.changePlayer) {
			incrementCurrentPlayer();

			//Sets current animations based on current Player
			setCurrentAnimations(false);
		}

	}

	@Override
	public void render(Graphics g) {
		
		//If player = Dead, shrink player size
		if(Game.gameHeader.getDead())
			this.setHeight(Creature.DEFAULT_CREATURE_HEIGHT_SMALL);

		//If player = Alive, draw player as alive sprite
		//We're not reviving player, so don't need to set height back to Big here
		
			//Draws player. Utilizes Cropping method via SpriteSheet class to only pull part of image
			// - Takes in integers, not floats, so need to cast x and y position:
			// - Image Observer = null. We won't use in tutorial
			g.drawImage(getCurrentAnimationFrame(), (int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()), (int) (width * GVar.getMultiplier()), (int) (height * GVar.getMultiplier()), null);

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
	
	public void updateAllBounds() {
		
		//All of the printing is for debugging obviously. can probably get rid of, but keeping until we get all of our camera stuff finalized.
		//printnb("bounds.x: " + bounds.x);printnb(" bounds.y: " + bounds.y);printnb(" bounds.width: " + bounds.width);print(" bounds.height: " + bounds.height);
		bounds = setBounds(DEFAULT_BOUNDS_X,DEFAULT_BOUNDS_Y,DEFAULT_BOUNDS_WIDTH,DEFAULT_BOUNDS_HEIGHT);
		//printnb("bounds.x: " + bounds.x);printnb(" bounds.y: " + bounds.y);printnb(" bounds.width: " + bounds.width);print(" bounds.height: " + bounds.height);

		//printnb("collisionBoundsUp.x: " + collisionBoundsUp.x);printnb(" collisionBoundsUp.y: " + collisionBoundsUp.y);printnb(" collisionBoundsUp.width: " + collisionBoundsUp.width);print(" collisionBoundsUp.height: " + collisionBoundsUp.height);
		collisionBoundsUp = setBounds(DEFAULT_COLLISION_BOUNDS_UP_X, DEFAULT_COLLISION_BOUNDS_UP_Y, DEFAULT_COLLISION_BOUNDS_UP_WIDTH, DEFAULT_COLLISION_BOUNDS_UP_HEIGHT);
		//printnb("collisionBoundsUp.x: " + collisionBoundsUp.x);printnb(" collisionBoundsUp.y: " + collisionBoundsUp.y);printnb(" collisionBoundsUp.width: " + collisionBoundsUp.width);print(" collisionBoundsUp.height: " + collisionBoundsUp.height);
		
		//printnb("collisionBoundsDown.x: " + collisionBoundsDown.x);printnb(" collisionBoundsDown.y: " + collisionBoundsDown.y);printnb(" collisionBoundsDown.width: " + collisionBoundsDown.width);print(" collisionBoundsDown.height: " + collisionBoundsDown.height);
		collisionBoundsDown = setBounds(DEFAULT_COLLISION_BOUNDS_DOWN_X, DEFAULT_COLLISION_BOUNDS_DOWN_Y, DEFAULT_COLLISION_BOUNDS_DOWN_WIDTH, DEFAULT_COLLISION_BOUNDS_DOWN_HEIGHT);
		//printnb("collisionBoundsDown.x: " + collisionBoundsDown.x);printnb(" collisionBoundsDown.y: " + collisionBoundsDown.y);printnb(" collisionBoundsDown.width: " + collisionBoundsDown.width);print(" collisionBoundsDown.height: " + collisionBoundsDown.height);
		
		//printnb("collisionBoundsLeft.x: " + collisionBoundsLeft.x);printnb(" collisionBoundsLeft.y: " + collisionBoundsLeft.y);printnb(" collisionBoundsLeft.width: " + collisionBoundsLeft.width);print(" collisionBoundsLeft.height: " + collisionBoundsLeft.height);
		collisionBoundsLeft = setBounds(DEFAULT_COLLISION_BOUNDS_LEFT_X, DEFAULT_COLLISION_BOUNDS_LEFT_Y, DEFAULT_COLLISION_BOUNDS_LEFT_WIDTH, DEFAULT_COLLISION_BOUNDS_LEFT_HEIGHT);
		//printnb("collisionBoundsLeft.x: " + collisionBoundsLeft.x);printnb(" collisionBoundsLeft.y: " + collisionBoundsLeft.y);printnb(" collisionBoundsLeft.width: " + collisionBoundsLeft.width);print(" collisionBoundsLeft.height: " + collisionBoundsLeft.height);
		
		//printnb("collisionBoundsRight.x: " + collisionBoundsRight.x);printnb(" collisionBoundsRight.y: " + collisionBoundsRight.y);printnb(" collisionBoundsRight.width: " + collisionBoundsRight.width);print(" collisionBoundsRight.height: " + collisionBoundsRight.height);
		collisionBoundsRight = setBounds(DEFAULT_COLLISION_BOUNDS_RIGHT_X, DEFAULT_COLLISION_BOUNDS_RIGHT_Y, DEFAULT_COLLISION_BOUNDS_RIGHT_WIDTH, DEFAULT_COLLISION_BOUNDS_RIGHT_HEIGHT);
		//printnb("collisionBoundsRight.x: " + collisionBoundsRight.x);printnb(" collisionBoundsRight.y: " + collisionBoundsRight.y);printnb(" collisionBoundsRight.width: " + collisionBoundsRight.width);print(" collisionBoundsRight.height: " + collisionBoundsRight.height);
	}
	
	/*************** GETTERS and SETTERS ***************/

	//Set animations based on currentPlayer
	public void setCurrentAnimations(boolean flip) {
		boolean xFlip = false, yFlip = false;

		for(int i = 0; i < rightArray.length; i++) {
			rightArray[i] = right;
		}
		
		//If changing directions, change it for ALL characters
		if(flip) {
			for(int i = 0; i < rightArray.length; i++) {
				animStand = new Animation(animSpeed, ImageFlip.flip(playerImage_Stand[i],true,yFlip));
				animRun = 	new Animation(animSpeed, ImageFlip.flip(playerImage_Run[i],true,yFlip));
				animJump = 	new Animation(animSpeed, ImageFlip.flip(playerImage_Jump[i],true,yFlip));
				animDuck = 	new Animation(animSpeed, ImageFlip.flip(playerImage_Duck[i],true,yFlip));
				animDead = 	new Animation(animSpeed, ImageFlip.flip(playerImage_Dead[i],true,yFlip));
			}
		}
		//Then display character
		animStand = new Animation(animSpeed, ImageFlip.flip(playerImage_Stand[currentPlayer],xFlip,yFlip));
		animRun = 	new Animation(animSpeed, ImageFlip.flip(playerImage_Run[currentPlayer],xFlip,yFlip));
		animJump = 	new Animation(animSpeed, ImageFlip.flip(playerImage_Jump[currentPlayer],xFlip,yFlip));
		animDuck = 	new Animation(animSpeed, ImageFlip.flip(playerImage_Duck[currentPlayer],xFlip,yFlip));
		animDead = 	new Animation(animSpeed, ImageFlip.flip(playerImage_Dead[currentPlayer],xFlip,yFlip));
		
	}
	
	//Gets current animation frame depending on movement/other
	private BufferedImage getCurrentAnimationFrame() {
		if(Game.gameHeader.getDead()) {
			return animDead.getCurrentFrame();
		} else  if (yMove < 0 || airborne) {
			return animJump.getCurrentFrame();
		} else  if (yMove > 0) {
			return animDuck.getCurrentFrame();
		} else if(xMove < 0) {
			return animRun.getCurrentFrame();
		} else  if (xMove > 0) {
			return animRun.getCurrentFrame();
		} else 
			return animStand.getCurrentFrame();
	}
	
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
		if(currentPlayer > playerImage_Stand.length-1)
			currentPlayer = 0;
		GVar.setPlayer1CurrentCharacter(currentPlayer);
	}
	
	//Sets whether player is facing right or not
	@Override
	public void setRight(boolean newRight) {
		if(right != newRight) {
			right = newRight;
			setCurrentAnimations(true);
		}
	}
	
}
