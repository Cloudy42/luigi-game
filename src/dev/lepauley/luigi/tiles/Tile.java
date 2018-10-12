package dev.lepauley.luigi.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import dev.lepauley.luigi.general.GVar;

/*
 * Contains Everything that every tile must have
 */

public class Tile {

	/*************STATIC VARIABLES*****************/	

		//Holds all tile references
		public static Tile[] tiles = new Tile[1000];
	
		//Initializes Tiles with id values
		public static Tile rock001 = new Rock001(0);
		public static Tile brick001 = new Brick001(1);
	
		public static Tile brick001CastleWall = new Brick001CastleWall(2);

		public static Tile fence001 = new Fence001(5);

		public static Tile brick001CastleRampartTransparent = new Brick001CastleRampartTransparent(11);
		public static Tile brick001CastleShadowLeft = new Brick001CastleShadowLeft(12);
		public static Tile brick001CastleShadowRight = new Brick001CastleShadowRight(14);
		
		public static Tile puzzleBlock001 = new PuzzleBlock001(24);
	
		public static Tile stone001 = new Stone001(28);
		
		public static Tile tree001Bark = new Tree001Bark(33);

		public static Tile plant001Trunk = new Plant001Trunk(35);

		public static Tile brick001CastleRampartBrick = new Brick001CastleRampartBrick(38);
		public static Tile brick001CastleShadowDoorTop = new Brick001CastleShadowDoorTop(39);
		public static Tile brick001CastleShadowDoorBottom = new Brick001CastleShadowDoorBottom(40);
	
		public static Tile rock002 = new Rock002(61);
		public static Tile brick002 = new Brick002(63);
		public static Tile stone002 = new Stone002(89);
		
		public static Tile puzzleBlock003 = new PuzzleBlock003(146);	
		public static Tile puzzleBlock003Used = new PuzzleBlock003Used(149);	

		public static Tile brick003Glisten = new Brick003Glisten(152);	
		public static Tile brick003GlistenPartial = new Brick003GlistenPartial(153);	

		public static Tile axeLever003 = new AxeLever003(177);	

		public static Tile underwater001Rock = new Underwater001Rock(212);	
		
		public static Tile flag001Top = new Flag001Top(225);	
		
		public static Tile pipeGreen001TopLeft = new PipeGreen001TopLeft(244);
		public static Tile pipeGreen001TopRight = new PipeGreen001TopRight(245);
		public static Tile pipeGreen001TopLeftSideways = new PipeGreen001TopLeftSideways(246);
		public static Tile pipeGreen001TopRightSideways = new PipeGreen001TopRightSideways(247);
		public static Tile pipeGreen001TopRightSidewaysConnector = new PipeGreen001TopRightSidewaysConnector(248);

		public static Tile tree001LeavesLeft = new Tree001LeavesLeft(249);
		public static Tile tree001LeavesMid = new Tree001LeavesMid(250);
		public static Tile tree001LeavesRight = new Tree001LeavesRight(251);
		
		
		public static Tile greenMountain001LeftIncline = new GreenMountain001LeftIncline(252);
		public static Tile greenMountain001Top = new GreenMountain001Top(253);
		public static Tile greenMountain001RightIncline = new GreenMountain001RightIncline(254);
		
		public static Tile plant001Small = new Plant001Small(257);
		public static Tile plant001BigTop = new Plant001BigTop(258);
		
		public static Tile pipeGreen001BottomLeft = new PipeGreen001BottomLeft(270);
		public static Tile pipeGreen001BottomRight = new PipeGreen001BottomRight(271);
		public static Tile pipeGreen001BottomLeftSideways = new PipeGreen001BottomLeftSideways(272);
		public static Tile pipeGreen001BottomRightSideways = new PipeGreen001BottomRightSideways(273);
		public static Tile pipeGreen001BottomRightSidewaysConnector = new PipeGreen001BottomRightSidewaysConnector(274);
		
		public static Tile greenMountain001DotsLeft = new GreenMountain001DotsLeft(278);
		public static Tile greenMountain001Base = new GreenMountain001Base(279);
		public static Tile greenMountain001DotsRight = new GreenMountain001DotsRight(280);
		
		public static Tile greenYellowBush001Left = new GreenYellowBush001Left(281);
		public static Tile greenYellowBush001Middle = new GreenYellowBush001Middle(282);
		public static Tile greenYellowBush001Right = new GreenYellowBush001Right(283);
		
		public static Tile plant001BigBottom = new Plant001BigBottom(284);	
		
		public static Tile flag001Pole = new Flag001Pole(286);	
		public static Tile flag001Flag = new Flag001Flag(288);	
		
		public static Tile chain003 = new Chain003(460);	

		public static Tile underwater001Coral = new Underwater001Coral(510);
		
		public static Tile cloud001TopLeft = new Cloud001TopLeft(550);
		public static Tile cloud001Top = new Cloud001Top(551);
		public static Tile cloud001TopRight = new Cloud001TopRight(552);
		public static Tile cloud001BottomLeft = new Cloud001BottomLeft(561);
		public static Tile cloud001Bottom = new Cloud001Bottom(562);	
		public static Tile cloud001BottomRight = new Cloud001BottomRight(563);
		
		public static Tile cloudFace001 = new CloudFace001(565);
		
		public static Tile bg001Sky = new BG001Sky(569);
		public static Tile bg002Sky = new BG002Sky(570);
	
		public static Tile lava001Waves = new Lava001Waves(593);

		public static Tile bridge003 = new Bridge003(594);

		public static Tile lava001Body = new Lava001Body(604);

		public static Tile water001Waves = new Water001Waves(612);
		public static Tile water001Body = new Water001Body(623);

		public static Tile missingTile = new MissingTile(998);
	
	/*************CLASS*****************/	
	
		//Default Tile size
		public static final int TILEWIDTH = 32, TILEHEIGHT = TILEWIDTH;
		
		protected BufferedImage texture;

		//Every id should be unique, so final
		protected final int id;
		
		//Tile Constructor
		public Tile(BufferedImage texture, int id) {
			this.texture = texture;
			this.id = id;
			
			//Sets tiles array at index (id) equal to current Tile being constructed
			tiles[id] = this;
		}
		
		//Unused tick() Method
		public void tick() {}

		//draws tile at x & y position with proper size using final variables and multiplier
		public void render(Graphics g, int x, int y) {
			g.drawImage(texture,  x,  y, TILEWIDTH * GVar.getMultiplier(), TILEHEIGHT * GVar.getMultiplier(), null);
		}
		
		//Checks whether player can go through tile or not
		public boolean isSolid() {
			return false;
		}
		
		/*************** GETTERS and SETTERS ***************/

		//Gets Tile id
		public int getId() {
			return id;
		}
		
}
