package dev.lepauley.luigi.gfx;

import java.awt.image.BufferedImage;

/*
 * Will Load Everything into Game only once (then can use assets at any time)
 */

public class Assets {

	//Width and Height of grid space of sprite sheet
	private static final int width = 16, heightBig = 32, heightSmall = 16, newPlayer = 51;
	public static BufferedImage player1, player2, rPlayer2;	
	
	public static void init() {
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/textures/Umario_and_Galugi.png"));
		
		player1 = sheet.crop(width * 3, heightBig * 0 + newPlayer * 0, width, heightBig);
		player2 = sheet.crop(width * 5, heightBig * 0 + newPlayer * 1, width, heightBig);
		//I made this just to test "flipped". I also wonder how we'll want to handle these. We can:
		//A.) Write logic to account for it within the code
		//B.) Make duplicates of all assets with them flipped then call that in the code
		//C.) Make duplicate spritesheets for all with all assets flipped
		//D.) Other?
		rPlayer2 = sheet.crop(width * 9, heightBig * 1 + newPlayer * 0, width, heightSmall);
	}
	
}
