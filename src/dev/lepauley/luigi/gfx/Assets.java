package dev.lepauley.luigi.gfx;

import java.awt.image.BufferedImage;

/*
 * Will Load Everything into Game only once (then can use assets at any time)
 */

public class Assets {

	//Width and Height of grid space of sprite sheet
	private static final int width = 16, heightBig = 32, heightSmall = 16, newPlayer = 51;
	public static BufferedImage player1, player2, player3, player4, player5, player6, player7, player8, player9, player10; 
	public static BufferedImage rPlayer2;
	public static BufferedImage sky, block1, block2, block3;
	
	public static void init() {
		//All Playersets:
		SpriteSheet playerSheet = new SpriteSheet(ImageLoader.loadImage("/textures/Umario_and_Galugi.png"));
		
		player1  = playerSheet.crop(width *  0, heightBig * 0 + newPlayer * 0, width, heightBig);
		player2  = playerSheet.crop(width *  0, heightBig * 0 + newPlayer * 1, width, heightBig);
		player3  = playerSheet.crop(width *  0, heightBig * 0 + newPlayer * 2, width, heightBig);
		player4  = playerSheet.crop(width *  0, heightBig * 0 + newPlayer * 3, width, heightBig);
		player5  = playerSheet.crop(width *  0, heightBig * 0 + newPlayer * 4, width, heightBig);
		player6  = playerSheet.crop(width *  0, heightBig * 0 + newPlayer * 5, width, heightBig);
		//I made this just to test "flipped". I also wonder how we'll want to handle these. We can:
		//A.) Write logic to account for it within the code
		//B.) Make duplicates of all assets with them flipped then call that in the code
		//C.) Make duplicate spritesheets for all with all assets flipped
		//D.) Other?
		rPlayer2 = playerSheet.crop(width * 9, heightBig * 1 + newPlayer * 0, width, heightSmall);

		//All Tilesets:
		SpriteSheet tileSheet = new SpriteSheet(ImageLoader.loadImage("/textures/miscellaneous.png"));

		sky = tileSheet.crop(width *  3, heightSmall * 21 + newPlayer * 0, width, heightSmall);
		block1 = tileSheet.crop(width *  0, heightSmall * 0 + newPlayer * 0, width, heightSmall);
		block2 = tileSheet.crop(width *  1, heightSmall * 0 + newPlayer * 0, width, heightSmall);
		block3 = tileSheet.crop(width *  2, heightSmall * 0 + newPlayer * 0, width, heightSmall);
		
	}
	
}
