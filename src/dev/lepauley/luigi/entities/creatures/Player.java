package dev.lepauley.luigi.entities.creatures;

import java.awt.Graphics;

import dev.lepauley.luigi.gfx.Assets;

/*
 * The player that our users will control.
 */
public class Player extends Creature{

	
	public Player(float x, float y) {
		super(x, y);

	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		//Draws player. Utilizes Cropping method via SpriteSheet class to only pull part of image
		// - Takes in integers, not floats, so need to cast x and y position:
		// - Image Observer = null. We won't use in tutorial
		g.drawImage(Assets.player1, (int) x, (int) y, (int) (x * 1), (int) (y * 1), null);
	}

}
