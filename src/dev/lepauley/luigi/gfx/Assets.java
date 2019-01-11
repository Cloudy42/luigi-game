package dev.lepauley.luigi.gfx;

import java.awt.image.BufferedImage;

/*
 * Will Load Everything into Game only once (then can use assets at any time)
 */

public class Assets {

	//Width and Height of tiles/player (except for heightBig which is double the size)
	private static final int width = 16, heightSmall = width, heightBig = heightSmall * 2;
	
	//newPlayer = y offset to get to each new player in sprite sheet
	private static final int newPlayer = 51;
	
	//This is because I made sprite sheet 10x bigger (to allow for clearer IRL images for faces)
	//This value would be changed to whatever spritesheet we decide to use, though 10 is recurring
	//for now.
	private static final int scale = 10;

	//Holds all tiles/player/items/etc.
	public static BufferedImage /*PLAYERS*/
									  player1, player2, player3, player4, player5, player6
								/*PLAYERS (DEAD)*/
									, player1Dead, player2Dead, player3Dead, player4Dead, player5Dead, player6Dead
								/*PLAYERS (REVERSE) - NOT CURRENTLY BEING USED, JUST THEORY*/
									, rPlayer2
								/*ITEMS*/ 		
									, menu, coin, toad
	                            /*TILES*/
									, bg001Sky, bg002Sky, bg003PaddedWall
									, rock001
									, rock002
									, brick001, brick001CastleWall, brick001CastleRampartTransparent, brick001CastleRampartBrick
									, brick002
									, brick001CastleShadowLeft, brick001CastleShadowRight, brick001CastleShadowDoorBottom, brick001CastleShadowDoorTop
									, brick003
									, brick003Glisten
									, brick003GlistenPartial
									, ceilingBrick002
									, stone001
									, stone002
									, conveyerBelt003Vertical
									, flag001Pole, flag001Top, flag001Flag
									, cloud001TopLeft, cloud001Top, cloud001TopRight, cloud001BottomLeft, cloud001Bottom, cloud001BottomRight
									, puzzleBlock001
									, puzzleBlock003, puzzleBlock003Used
									, axeLever003
									, stableBridge002Rope
									, stableBridge002
									, bridge003
									, chain003
									, plant001Trunk, plant001Small, plant001BigBottom, plant001BigTop
									, fence001
									, cloudFace001
									, underwater001Rock, underwater001Coral, water001Waves, water001Body 
									, tree001Bark, tree001LeavesLeft, tree001LeavesMid, tree001LeavesRight
									, greenMountain001LeftIncline, greenMountain001Top, greenMountain001RightIncline, greenMountain001Base, greenMountain001DotsLeft, greenMountain001DotsRight
									, greenYellowBush001Left, greenYellowBush001Middle, greenYellowBush001Right
									, pipeGreen001BottomLeft, pipeGreen001TopLeft, pipeGreen001TopRight, pipeGreen001BottomRight
									, pipeGreen001BottomLeftSideways, pipeGreen001TopLeftSideways, pipeGreen001TopRightSideways, pipeGreen001BottomRightSideways, pipeGreen001TopRightSidewaysConnector, pipeGreen001BottomRightSidewaysConnector
									, lava001Waves, lava001Body
									, missing;
	
	//Initializes Assets
	public static void init() {

		//Title Screen sprites, namely the big "Super Mario Brothers" menu
		SpriteSheet menuSheet = new SpriteSheet(ImageLoader.loadImage("/textures/title_screen.png"));
		menu = menuSheet.crop(1, 60,176,88);

		//All Playersets:
		SpriteSheet playerSheet = new SpriteSheet(ImageLoader.loadImage("/textures/Umario_and_Galugi_10x.png"));
		
		player1  = playerSheet.crop(width *  0 * scale, heightBig * 0 + newPlayer * 0 * scale, width * scale, heightBig * scale);
		player2  = playerSheet.crop(width *  0 * scale, heightBig * 0 + newPlayer * 1 * scale, width * scale, heightBig * scale);
		player3  = playerSheet.crop(width *  0 * scale, heightBig * 0 + newPlayer * 2 * scale, width * scale, heightBig * scale);
		player4  = playerSheet.crop(width *  0 * scale, heightBig * 0 + newPlayer * 3 * scale, width * scale, heightBig * scale);
		player5  = playerSheet.crop(width *  0 * scale, heightBig * 0 + newPlayer * 4 * scale, width * scale, heightBig * scale);
		player6  = playerSheet.crop(width *  0 * scale, heightBig * 0 + newPlayer * 5 * scale, width * scale, heightBig * scale);

		//Made just for funsies:
		player1Dead  = playerSheet.crop(width *  6 * scale, (heightSmall * 2 + newPlayer * 0) * scale, width * scale, heightSmall * scale);
		player2Dead  = playerSheet.crop(width *  6 * scale, (heightSmall * 2 + newPlayer * 1) * scale, width * scale, heightSmall * scale);
		player3Dead  = playerSheet.crop(width *  6 * scale, (heightSmall * 2 + newPlayer * 2) * scale, width * scale, heightSmall * scale);
		player4Dead  = playerSheet.crop(width *  6 * scale, (heightSmall * 2 + newPlayer * 3) * scale, width * scale, heightSmall * scale);
		player5Dead  = playerSheet.crop(width *  6 * scale, (heightSmall * 2 + newPlayer * 4) * scale, width * scale, heightSmall * scale);
		player6Dead  = playerSheet.crop(width *  6 * scale, (heightSmall * 2 + newPlayer * 5) * scale, width * scale, heightSmall * scale);

		//I made this just to test "flipped". I also wonder how we'll want to handle these. We can:
		//A.) Write logic to account for it within the code
		//B.) Make duplicates of all assets with them flipped then call that in the code
		//C.) Make duplicate spritesheets for all with all assets flipped
		//D.) Other?
		rPlayer2 = playerSheet.crop(width * 9, heightBig * 1 + newPlayer * 0, width, heightSmall);

		//Item sprite sheet
		//Currently only used for toad (# of characters selection) and Coin icons on title screen
		SpriteSheet itemSheet = new SpriteSheet(ImageLoader.loadImage("/textures/items_and_objects.png"));
		toad	= itemSheet.crop(width * 0, heightSmall * 0, 16, 16);
		coin 	= itemSheet.crop(width * 0, heightSmall * 6, 16, 16);
		
		//All Tilesets:
		SpriteSheet tileSheet = new SpriteSheet(ImageLoader.loadImage("/textures/miscellaneous.png"));

		/*0000*/ rock001 = tileSheet.crop(width *  0, heightSmall * 0, width, heightSmall); 
		/*0001*/ brick001 = tileSheet.crop(width *  1, heightSmall * 0, width, heightSmall);

		/*0002*/ brick001CastleWall = tileSheet.crop(width *  2, heightSmall * 0, width, heightSmall);

		/*0005*/ fence001 = tileSheet.crop(width *  5, heightSmall * 0, width, heightSmall);

		/*0011*/ brick001CastleRampartTransparent = tileSheet.crop(width * 11, heightSmall * 0, width, heightSmall); 
		/*0012*/ brick001CastleShadowRight = tileSheet.crop(width * 14, heightSmall * 0, width, heightSmall); 
		/*0014*/ brick001CastleShadowLeft = tileSheet.crop(width * 12, heightSmall * 0, width, heightSmall); 
		/*0024*/ puzzleBlock001 = tileSheet.crop(width *  24, heightSmall * 0, width, heightSmall); 
		/*0028*/ stone001 = tileSheet.crop(width *  0, heightSmall * 1, width, heightSmall); 
		/*0031*/ stableBridge002 = tileSheet.crop(width *  3, heightSmall * 1, width, heightSmall);
		/*0033*/ tree001Bark = tileSheet.crop(width *  5, heightSmall * 1, width, heightSmall); 

		/*0035*/ plant001Trunk = tileSheet.crop(width * 7, heightSmall * 1, width, heightSmall); 

		/*0038*/ brick001CastleRampartBrick = tileSheet.crop(width * 11, heightSmall * 1, width, heightSmall); 
		/*0039*/ brick001CastleShadowDoorTop = tileSheet.crop(width * 12, heightSmall * 1, width, heightSmall); 
		/*0040*/ brick001CastleShadowDoorBottom = tileSheet.crop(width * 13, heightSmall * 1, width, heightSmall);

		/*0061*/ rock002 = tileSheet.crop(width *  0, heightSmall * 2, width, heightSmall); 
		/*0063*/ brick002 = tileSheet.crop(width *  2, heightSmall * 2, width, heightSmall);
		/*0089*/ stone002 = tileSheet.crop(width *  0, heightSmall * 3, width, heightSmall); 

		/*0124*/ brick003 = tileSheet.crop(width *  2, heightSmall * 4, width, heightSmall);

		/*0146*/ puzzleBlock003 = tileSheet.crop(width *  24, heightSmall * 4, width, heightSmall); 
		/*0149*/ puzzleBlock003Used = tileSheet.crop(width *  27, heightSmall * 4, width, heightSmall); 
		
		/*0152*/ brick003Glisten = tileSheet.crop(width *  2, heightSmall * 5, width, heightSmall);
		/*0152b*/ brick003GlistenPartial = tileSheet.crop(width *  2, heightSmall * 5, width/2, heightSmall);

		/*0156*/ conveyerBelt003Vertical = tileSheet.crop(width *  6, heightSmall * 5, width, heightSmall);
		
		/*0177*/ axeLever003 = tileSheet.crop(width *  27, heightSmall * 5, width, heightSmall);

		/*0207*/ missing = tileSheet.crop(width *  24, heightSmall * 6, width, heightSmall); 

		/*0212*/ underwater001Rock = tileSheet.crop(width *  1, heightSmall * 7, width, heightSmall); 
		
		/*0225*/ flag001Top = tileSheet.crop(width *  14, heightSmall * 7, width, heightSmall); 

		/*0244*/ pipeGreen001TopLeft = tileSheet.crop(width *  0, heightSmall * 8, width, heightSmall); 
		/*0245*/ pipeGreen001TopRight = tileSheet.crop(width *  1, heightSmall * 8, width, heightSmall); 
		/*0246*/ pipeGreen001TopLeftSideways = tileSheet.crop(width *  2, heightSmall * 8, width, heightSmall); 
		/*0247*/ pipeGreen001TopRightSideways = tileSheet.crop(width *  3, heightSmall * 8, width, heightSmall); 
		/*0248*/ pipeGreen001TopRightSidewaysConnector = tileSheet.crop(width *  4, heightSmall * 8, width, heightSmall); 

		/*0249*/ tree001LeavesLeft = tileSheet.crop(width *  5, heightSmall * 8, width, heightSmall); 
		/*0250*/ tree001LeavesMid = tileSheet.crop(width *  6, heightSmall * 8, width, heightSmall); 
		/*0251*/ tree001LeavesRight = tileSheet.crop(width *  7, heightSmall * 8, width, heightSmall); 

		/*0252*/ greenMountain001LeftIncline = tileSheet.crop(width *  8, heightSmall * 8, width, heightSmall); 
		/*0253*/ greenMountain001Top = tileSheet.crop(width *  9, heightSmall * 8, width, heightSmall); 
		/*0254*/ greenMountain001RightIncline = tileSheet.crop(width *  10, heightSmall * 8, width, heightSmall);
		
		/*0257*/ plant001Small = tileSheet.crop(width *  13, heightSmall * 8, width, heightSmall);
		/*0258*/ plant001BigTop = tileSheet.crop(width *  14, heightSmall * 8, width, heightSmall);

		/*0270*/ pipeGreen001BottomLeft = tileSheet.crop(width *  0, heightSmall * 9, width, heightSmall); 
		/*0271*/ pipeGreen001BottomRight = tileSheet.crop(width *  1, heightSmall * 9, width, heightSmall); 
		/*0272*/ pipeGreen001BottomLeftSideways = tileSheet.crop(width *  2, heightSmall * 9, width, heightSmall); 
		/*0273*/ pipeGreen001BottomRightSideways = tileSheet.crop(width *  3, heightSmall * 9, width, heightSmall); 
		/*0274*/ pipeGreen001BottomRightSidewaysConnector = tileSheet.crop(width *  4, heightSmall * 9, width, heightSmall); 
		
		/*0278*/ greenMountain001DotsLeft = tileSheet.crop(width *  8, heightSmall * 9, width, heightSmall); 
		/*0279*/ greenMountain001Base = tileSheet.crop(width *  9, heightSmall * 9, width, heightSmall); 
		/*0280*/ greenMountain001DotsRight = tileSheet.crop(width *  10, heightSmall * 9, width, heightSmall);   
		/*0281*/ greenYellowBush001Left = tileSheet.crop(width *  11, heightSmall * 9, width, heightSmall); 
		/*0282*/ greenYellowBush001Middle = tileSheet.crop(width *  12, heightSmall * 9, width, heightSmall); 
		/*0283*/ greenYellowBush001Right = tileSheet.crop(width *  13, heightSmall * 9, width, heightSmall); 
		
		/*0284*/ plant001BigBottom = tileSheet.crop(width *  14, heightSmall * 9, width, heightSmall); 
		
		/*0286*/ flag001Pole = tileSheet.crop(width *  16, heightSmall * 9, width, heightSmall);
		/*0288*/ flag001Flag = tileSheet.crop(width *  18, heightSmall * 9, width, heightSmall);
		
		/*0310*/ stableBridge002Rope = tileSheet.crop(width *  15, heightSmall * 10, width, heightSmall);

		/*0460*/ chain003 = tileSheet.crop(width *  12, heightSmall * 16, width, heightSmall);

		/*0510*/ underwater001Coral = tileSheet.crop(width * 11, heightSmall * 18, width, heightSmall);

		/*0550*/ cloud001TopLeft = tileSheet.crop(width *  0, heightSmall * 20, width, heightSmall);
		/*0551*/ cloud001Top = tileSheet.crop(width *  1, heightSmall * 20, width, heightSmall); 
		/*0552*/ cloud001TopRight = tileSheet.crop(width *  2, heightSmall * 20, width, heightSmall); 
		/*0561*/ cloud001BottomLeft = tileSheet.crop(width *  0, heightSmall * 21, width, heightSmall);
		/*0562*/ cloud001Bottom = tileSheet.crop(width *  1, heightSmall * 21, width, heightSmall); 
		/*0563*/ cloud001BottomRight = tileSheet.crop(width *  2, heightSmall * 21, width, heightSmall); 

		/*0565*/ cloudFace001 = tileSheet.crop(width *  4, heightSmall * 21, width, heightSmall);
		
		/*0569*/ bg001Sky = tileSheet.crop(width *  8, heightSmall * 21, width, heightSmall);
		/*0570*/ bg002Sky = tileSheet.crop(width *  9, heightSmall * 21, width, heightSmall);
		/*0571*/ bg003PaddedWall = tileSheet.crop(width *  10, heightSmall * 21, width, heightSmall);

		/*0593*/ lava001Waves = tileSheet.crop(width *  3, heightSmall * 24, width, heightSmall);
		
		/*0593*/ bridge003 = tileSheet.crop(width *  4, heightSmall * 24, width, heightSmall);
		
		/*0604*/ lava001Body = tileSheet.crop(width *  3, heightSmall * 25, width, heightSmall);

		/*0612*/ water001Waves = tileSheet.crop(width *  3, heightSmall * 26, width, heightSmall);
		/*0623*/ water001Body = tileSheet.crop(width *  3, heightSmall * 27, width, heightSmall);
	
	}	
}
