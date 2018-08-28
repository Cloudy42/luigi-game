package dev.lepauley.luigi.gfx;

import java.awt.image.BufferedImage;

/*
 * Stores BufferedImage and crops based on coordinates
 */

public class SpriteSheet {

	private BufferedImage sheet;
	
	public SpriteSheet(BufferedImage sheet) {
		this.sheet = sheet;
	}
	
	//Method to Crop the spriteSheet into animations/images
	public BufferedImage crop(int x, int y, int width, int height) {
		//Returns BufferedImage with coordinates we specify
		return sheet.getSubimage(x, y, width, height);
	}
	
}
