package dev.lepauley.luigi.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * Loads Images
 */

public class ImageLoader {

	//Images are stored within BufferedImage
	public static BufferedImage loadImage(String path) {
		try {
			//Loads image to BufferedImage via the path provided.
			return ImageIO.read(ImageLoader.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
			//If we don't load image, we don't want to run game, so this will exit if nothing is loaded. 
			System.exit(1);
		}
		return null;
	}
}
