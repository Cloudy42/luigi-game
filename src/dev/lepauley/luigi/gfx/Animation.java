package dev.lepauley.luigi.gfx;

import java.awt.image.BufferedImage;

/*
 * Handles animations (player, enemies, etc.
 */

public class Animation {
	
	private int speed, index;
	private long lastTime, timer;
	private BufferedImage[] frames;
	
	public Animation(int speed, BufferedImage[] frames) {
		this.speed = speed;
		this.frames = frames;
		index = 0; //Should always start on the first animation frame
		timer = 0;
		lastTime = System.currentTimeMillis(); //Amount of time that has elapsed since we started the game
	}

	public void tick() {
		
		//Time in milliseconds between this call and the last call
		timer += System.currentTimeMillis() - lastTime;
		
		//Reset lastTime
		lastTime = System.currentTimeMillis();
		
		//if timer > speed, increment to the next animation frame
		if(timer > speed) {
			index++;
			timer = 0;
			if(index >= frames.length)
				index = 0; //Stops index from getting outside of array length
		}
	}
	
	/*************** GETTERS and SETTERS ***************/
			
	//Gets the current animation frame
	public BufferedImage getCurrentFrame() {
		return frames[index];
	}

}
