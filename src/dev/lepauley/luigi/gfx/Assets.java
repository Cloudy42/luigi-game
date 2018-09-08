package dev.lepauley.luigi.gfx;

import java.awt.image.BufferedImage;

/*
 * Will Load Everything into Game only once (then can use assets at any time)
 */

public class Assets {

	//Width and Height of grid space of sprite sheet
	private static final int width = 16, heightBig = 32, heightSmall = 16, newPlayer = 52;
	public static BufferedImage player1, player2, player3, player4, player5, player6, player7, player8, player9, player10; 
	public static BufferedImage rPlayer2;	
	
	public static void init() {
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/textures/Umario_and_Galugi.png"));
		
		player1  = sheet.crop(width *  0, heightBig * 0 + newPlayer * 0, width, heightBig);
		player2  = sheet.crop(width *  0, heightBig * 0 + newPlayer * 1, width, heightBig);
		player3  = sheet.crop(width *  0, heightBig * 0 + newPlayer * 2, width, heightBig);
		player4  = sheet.crop(width *  0, heightBig * 0 + newPlayer * 3, width, heightBig);
		player5  = sheet.crop(width *  0, heightBig * 0 + newPlayer * 4, width, heightBig);
		player6  = sheet.crop(width *  0, heightBig * 0 + newPlayer * 5, width, heightBig);
		player7  = sheet.crop(width *  0, heightBig * 0 + newPlayer * 6, width, heightBig);
		player8  = sheet.crop(width *  0, heightBig * 0 + newPlayer * 7, width, heightBig);
		player9  = sheet.crop(width *  0, heightBig * 0 + newPlayer * 8, width, heightBig);
		player10 = sheet.crop(width *  0, heightBig * 0 + newPlayer * 9, width, heightBig);
		//I made this just to test "flipped". I also wonder how we'll want to handle these. We can:
		//A.) Write logic to account for it within the code
		//B.) Make duplicates of all assets with them flipped then call that in the code
		//C.) Make duplicate spritesheets for all with all assets flipped
		//D.) Other?
		rPlayer2 = sheet.crop(width * 9, heightBig * 1 + newPlayer * 0, width, heightSmall);
	}
	
}
