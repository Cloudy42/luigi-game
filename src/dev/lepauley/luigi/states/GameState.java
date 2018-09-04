package dev.lepauley.luigi.states;

import java.awt.Color;
import java.awt.Graphics;

import dev.lepauley.luigi.gfx.Assets;

/*
 * Where actual game play is at
 */
public class GameState extends State {

	int x = 0;  //Temp test code for moving test images
	
	public GameState() {
		
	}
	
	@Override
	public void tick() {
		x += 1; //Temp test code for moving test images
	}

	@Override
	public void render(Graphics g) {
		//Test images
		g.setColor(Color.red);
		g.fillRect(320 + x, 20 + x, 75, 90);
		
		g.setColor(Color.blue);
		g.fillRect(100 + x, 80 - x, 75, 90);
		
		//Utilizes Cropping method via SpriteSheet class to only pull part of image
		// - Image Observer = null. We won't use in tutorial
		g.drawImage(Assets.player1,   25 + x, 180,  150, 250, null);
		g.drawImage(Assets.player2,  250, 180 + x,  150, 250, null);
		g.drawImage(Assets.rPlayer2, 625 - x, 180 - x, -150, 250, null);
	}

}
