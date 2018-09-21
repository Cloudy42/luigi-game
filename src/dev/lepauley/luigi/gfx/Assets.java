package dev.lepauley.luigi.gfx;

import java.awt.image.BufferedImage;

/*
 * Will Load Everything into Game only once (then can use assets at any time)
 */

public class Assets {

	//Width and Height of grid space of sprite sheet
	private static final int width = 16, heightBig = 32, heightSmall = 16, newPlayer = 51, scale = 10;
	public static BufferedImage menu, coin, toad;
	public static BufferedImage player1, player2, player3, player4, player5, player6; 
	public static BufferedImage player1Dead, player2Dead, player3Dead, player4Dead, player5Dead, player6Dead; 
	public static BufferedImage rPlayer2;
	public static BufferedImage bg001Sky
	                          , rock001
	                          , brick001, brick001CastleWall, brick001CastleRampartTransparent, brick001CastleRampartBrick
	                          , brick001CastleShadowLeft, brick001CastleShadowRight, brick001CastleShadowDoorBottom, brick001CastleShadowDoorTop
	                          , stone001
	                          , flag001Pole, flag001Top, flag001Flag
	                          , cloud001TopLeft, cloud001Top, cloud001TopRight, cloud001BottomLeft, cloud001Bottom, cloud001BottomRight
	                          , puzzleBlock001
	                          , greenMountain001LeftIncline, greenMountain001Top, greenMountain001RightIncline, greenMountain001Base, greenMountain001DotsLeft, greenMountain001DotsRight
	                          , greenYellowBush001Left, greenYellowBush001Middle, greenYellowBush001Right
	                          , pipeGreen001BottomLeft, pipeGreen001TopLeft, pipeGreen001TopRight, pipeGreen001BottomRight
	                          , missing;
	
	public static void init() {
		//Menusets
		SpriteSheet menuSheet = new SpriteSheet(ImageLoader.loadImage("/textures/title_screen.png"));
		menu = menuSheet.crop(1, 60,176,88);

		//160/5/20
		SpriteSheet itemSheet = new SpriteSheet(ImageLoader.loadImage("/textures/items_and_objects.png"));
		toad	= itemSheet.crop(width * 0, heightSmall * 0, 16, 16);
		coin 	= itemSheet.crop(width * 0, heightSmall * 6, 16, 16);
		
		
		//All Playersets:
		SpriteSheet playerSheet = new SpriteSheet(ImageLoader.loadImage("/textures/Umario_and_Galugi_10x.png"));
		
		player1  = playerSheet.crop(width *  0 * scale, heightBig * 0 + newPlayer * 0 * scale, width * scale, heightBig * scale);
		player2  = playerSheet.crop(width *  0 * scale, heightBig * 0 + newPlayer * 1 * scale, width * scale, heightBig * scale);
		player3  = playerSheet.crop(width *  0 * scale, heightBig * 0 + newPlayer * 2 * scale, width * scale, heightBig * scale);
		player4  = playerSheet.crop(width *  0 * scale, heightBig * 0 + newPlayer * 3 * scale, width * scale, heightBig * scale);
		player5  = playerSheet.crop(width *  0 * scale, heightBig * 0 + newPlayer * 4 * scale, width * scale, heightBig * scale);
		player6  = playerSheet.crop(width *  0 * scale, heightBig * 0 + newPlayer * 5 * scale, width * scale, heightBig * scale);

		//Made just for funsies:
		player1Dead  = playerSheet.crop(width *  6 * scale, (heightBig * 1 + newPlayer * 0) * scale, width * scale, heightBig * scale);
		player2Dead  = playerSheet.crop(width *  6 * scale, (heightBig * 1 + newPlayer * 1) * scale, width * scale, heightBig * scale);
		player3Dead  = playerSheet.crop(width *  6 * scale, (heightBig * 1 + newPlayer * 2) * scale, width * scale, heightBig * scale);
		player4Dead  = playerSheet.crop(width *  6 * scale, (heightBig * 1 + newPlayer * 3) * scale, width * scale, heightBig * scale);
		player5Dead  = playerSheet.crop(width *  6 * scale, (heightBig * 1 + newPlayer * 4) * scale, width * scale, heightBig * scale);
		//player6Dead  = playerSheet.crop(width *  6 * scale, (heightBig * 1 + newPlayer * 5) * scale, width * scale, heightBig * scale);

		//I made this just to test "flipped". I also wonder how we'll want to handle these. We can:
		//A.) Write logic to account for it within the code
		//B.) Make duplicates of all assets with them flipped then call that in the code
		//C.) Make duplicate spritesheets for all with all assets flipped
		//D.) Other?
		rPlayer2 = playerSheet.crop(width * 9, heightBig * 1 + newPlayer * 0, width, heightSmall);

		//All Tilesets:
		SpriteSheet tileSheet = new SpriteSheet(ImageLoader.loadImage("/textures/miscellaneous.png"));

		bg001Sky = tileSheet.crop(width *  8, heightSmall * 21 + newPlayer * 0, width, heightSmall);
		rock001 = tileSheet.crop(width *  0, heightSmall * 0 + newPlayer * 0, width, heightSmall);
		brick001 = tileSheet.crop(width *  1, heightSmall * 0 + newPlayer * 0, width, heightSmall);
		brick001CastleWall = tileSheet.crop(width *  2, heightSmall * 0 + newPlayer * 0, width, heightSmall); //0002
		brick001CastleRampartTransparent = tileSheet.crop(width * 11, heightSmall * 0 + newPlayer * 0, width, heightSmall); //0011
		brick001CastleRampartBrick = tileSheet.crop(width * 11, heightSmall * 1 + newPlayer * 0, width, heightSmall); //0038
		brick001CastleShadowRight = tileSheet.crop(width * 14, heightSmall * 0 + newPlayer * 0, width, heightSmall); //0012
		brick001CastleShadowLeft = tileSheet.crop(width * 12, heightSmall * 0 + newPlayer * 0, width, heightSmall); //0014
		brick001CastleShadowDoorTop = tileSheet.crop(width * 12, heightSmall * 1 + newPlayer * 0, width, heightSmall); //0039
		brick001CastleShadowDoorBottom = tileSheet.crop(width * 13, heightSmall * 1 + newPlayer * 0, width, heightSmall); //0040

		stone001 = tileSheet.crop(width *  0, heightSmall * 1 + newPlayer * 0, width, heightSmall);
		flag001Pole = tileSheet.crop(width *  16, heightSmall * 9 + newPlayer * 0, width, heightSmall); //0286
		flag001Top = tileSheet.crop(width *  14, heightSmall * 7 + newPlayer * 0, width, heightSmall); //0225
		flag001Flag = tileSheet.crop(width *  18, heightSmall * 9 + newPlayer * 0, width, heightSmall); //0288
		cloud001TopLeft = tileSheet.crop(width *  0, heightSmall * 20 + newPlayer * 0, width, heightSmall);
		cloud001Top = tileSheet.crop(width *  1, heightSmall * 20 + newPlayer * 0, width, heightSmall);
		cloud001TopRight = tileSheet.crop(width *  2, heightSmall * 20 + newPlayer * 0, width, heightSmall);
		cloud001BottomLeft = tileSheet.crop(width *  0, heightSmall * 21 + newPlayer * 0, width, heightSmall);
		cloud001Bottom = tileSheet.crop(width *  1, heightSmall * 21 + newPlayer * 0, width, heightSmall);
		cloud001BottomRight = tileSheet.crop(width *  2, heightSmall * 21 + newPlayer * 0, width, heightSmall);
		puzzleBlock001 = tileSheet.crop(width *  24, heightSmall * 0 + newPlayer * 0, width, heightSmall);
        greenMountain001LeftIncline = tileSheet.crop(width *  8, heightSmall * 8 + newPlayer * 0, width, heightSmall);
        greenMountain001Top = tileSheet.crop(width *  9, heightSmall * 8 + newPlayer * 0, width, heightSmall);
        greenMountain001RightIncline = tileSheet.crop(width *  10, heightSmall * 8 + newPlayer * 0, width, heightSmall);
        greenMountain001Base = tileSheet.crop(width *  9, heightSmall * 9 + newPlayer * 0, width, heightSmall);
        greenMountain001DotsLeft = tileSheet.crop(width *  8, heightSmall * 9 + newPlayer * 0, width, heightSmall);
        greenMountain001DotsRight = tileSheet.crop(width *  10, heightSmall * 9 + newPlayer * 0, width, heightSmall);        
        greenYellowBush001Left = tileSheet.crop(width *  11, heightSmall * 9 + newPlayer * 0, width, heightSmall);
        greenYellowBush001Middle = tileSheet.crop(width *  12, heightSmall * 9 + newPlayer * 0, width, heightSmall);
        greenYellowBush001Right = tileSheet.crop(width *  13, heightSmall * 9 + newPlayer * 0, width, heightSmall);
        pipeGreen001BottomLeft = tileSheet.crop(width *  0, heightSmall * 9 + newPlayer * 0, width, heightSmall);
        pipeGreen001TopLeft = tileSheet.crop(width *  0, heightSmall * 8 + newPlayer * 0, width, heightSmall);
        pipeGreen001TopRight = tileSheet.crop(width *  1, heightSmall * 8 + newPlayer * 0, width, heightSmall);
        pipeGreen001BottomRight = tileSheet.crop(width *  1, heightSmall * 9 + newPlayer * 0, width, heightSmall);
        missing = tileSheet.crop(width *  24, heightSmall * 6 + newPlayer * 0, width, heightSmall);
	}
	
}
