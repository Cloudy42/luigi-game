package dev.lepauley.luigi.entities.creatures;

import java.awt.Graphics;

import dev.lepauley.luigi.Game;
import dev.lepauley.luigi.gfx.Assets;


/*
 * The player that our users will control.
 */
public class Player extends Creature{
	//So we can access things like key manager
	private Game game;
	
	public Player(Game game, float x, float y) {
		super(x, y);
		this.game = game;
	}

	@Override
	public void tick() {
		//Test code for movement
		if(game.getKeyManager().up)
			y -= 3;
		if(game.getKeyManager().down)
			y += 3;
		if(game.getKeyManager().left)
			x -= 3;
		if(game.getKeyManager().right)
			x += 3;
	}

	@Override
	public void render(Graphics g) {
		//Draws player. Utilizes Cropping method via SpriteSheet class to only pull part of image
		// - Takes in integers, not floats, so need to cast x and y position:
		// - Image Observer = null. We won't use in tutorial
		g.drawImage(Assets.player1, (int) x, (int) y, (int) (x * multiplier), (int) (y * multiplier), null);
	}

}
