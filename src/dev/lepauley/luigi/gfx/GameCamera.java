package dev.lepauley.luigi.gfx;

import dev.lepauley.luigi.entities.Entity;
import dev.lepauley.luigi.general.Game;

/*
 * Tracks and follows player (or more accurately moves all tiles as player "moves" to appear like there is movement)
 */
public class GameCamera {

	//main game object
	private Game game;
	
	//the amount we move tiles on the x and y axis in relation to original position
	private float xOffset, yOffset;
	
	public GameCamera(Game game, float xOffset, float yOffset) {
		this.game = game;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	//Centers camera on target entity utilizing x and y offsets
	public void centerOnEntity(Entity e) {
		
		//We divide by 2 so centered on screen (and not on edge) and centered on entity
		xOffset = e.getX() - game.getWidth() / 2 + e.getWidth() / 2;
		
		//Temporarily disabled until we fix this for camera limits
		//Though MAY be permanent if we are sticking to old school camera for Mario game...
		//yOffset = e.getY() - game.getHeight() / 2 + e.getHeight() / 2;
	}

	//Takes inputs and adds them to the associated offset variables respecitvely
	public void move(float xAmt, float yAmt) {
		xOffset += xAmt;
		yOffset += yAmt;
	}
	
	/*************** GETTERS and SETTERS ***************/
		
	//Gets X Offset
	public float getxOffset() {
		return xOffset;
	}

	//Sets X Offset
	public void setxOffset(float xOffset) {
		this.xOffset = xOffset;
	}

	//Gets Y Offset
	public float getyOffset() {
		return yOffset;
	}

	//Sets Y Offset
	public void setyOffset(float yOffset) {
		this.yOffset = yOffset;
	}

}
