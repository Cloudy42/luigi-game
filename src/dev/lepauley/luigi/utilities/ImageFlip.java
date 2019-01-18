package dev.lepauley.luigi.utilities;

/*
 * Flips images
 */

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageFlip {

	//Takes image and booleans for x and y axis flips. Returns flipped image accordingly
	public static BufferedImage flip(BufferedImage bi, boolean xFlip, boolean yFlip) {
		int x = 1, y = 1;
		double width = 0, height = 0;

		if(xFlip) {
			x = -1;
			width = bi.getWidth() * -1;
		}
		if(yFlip) {
			y = -1;
			height = bi.getHeight() * -1;
		}

		AffineTransform tx = AffineTransform.getScaleInstance(x,y);
	    tx.translate(width, height);
	    AffineTransformOp op = new AffineTransformOp(tx,
	        AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	    return op.filter(bi, null);
	}
	
	//Takes an array of BufferedImages and pushes through flip()
	public static BufferedImage[] flip(BufferedImage[] bi, boolean xFlip, boolean yFlip) {
		for(int i = 0; i < bi.length; i++) {
			bi[i] = flip(bi[i], xFlip, yFlip);
		}
		return bi;
	}
}
