package dev.lepauley.luigi.gfx;

import java.awt.image.BufferedImage;

/*
 * Will Load Everything into Game only once (then can use assets at any time)
 */

public class Assets {

	//Width and Height of grid space of sprite sheet
	private static final int width = 16, height = 32;
	//I added this myself to account for our sprite sheet, but we can take off the "grid" 
	//and player names from sprite sheet to avoid this issue if you'd like. Lemme know and I can edit the file.
	//Not sure what the best solution is - cleaner code seems like it'd make more sense to me, 
	//but maybe having it all on one file with weird spacing is better. I feel like We could find a solution
	//that is the best of both world where it's all on one file but maybe without the grid
	//and the weird player splits and such so we don't need all of these were spacing variables. Thoughts?
	private static final int gridStartX = 79, gridStartY = 65, grid = 1;
	
	public static BufferedImage player1, player2, rPlayer2;	
	
	public static void init() {
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/textures/Umario_and_Galugi.png"));
		
		player1 = sheet.crop(gridStartX + width * 0 + grid * 1, gridStartY * 0 + height * 0 + grid * 1, width, height);
		player2 = sheet.crop(gridStartX + width * 0 + grid * 1, gridStartY * 1 + height * 0 + grid * 1, width, height);
		//I made this just to test "flipped". I also wonder how we'll want to handle these. We can:
		//A.) Write logic to account for it within the code
		//B.) Make duplicates of all assets with them flipped then call that in the code
		//C.) Make duplicate spritesheets for all with all assets flipped
		//D.) Other?
		rPlayer2 = sheet.crop(182, 66, 16, 32);
	}
	
}
