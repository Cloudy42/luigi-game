package dev.lepauley.luigi.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import dev.lepauley.luigi.GVar;

/*
 * Contains Everything that every tile must have
 */

public class Tile {

	//STATIC VARIABLES
	
	public static Tile[] tiles = new Tile[1000];

	public static Tile brick001 = new Brick001(1);

	public static Tile brick001CastleWall = new Brick001CastleWall(2);
	public static Tile brick001CastleRampartTransparent = new Brick001CastleRampartTransparent(11);
	public static Tile brick001CastleRampartBrick = new Brick001CastleRampartBrick(38);
	public static Tile brick001CastleShadowLeft = new Brick001CastleShadowLeft(12);
	public static Tile brick001CastleShadowRight = new Brick001CastleShadowRight(14);
	public static Tile brick001CastleShadowDoorTop = new Brick001CastleShadowDoorTop(39);
	public static Tile brick001CastleShadowDoorBottom = new Brick001CastleShadowDoorBottom(40);

	public static Tile stone001 = new Stone001(28);
	
	public static Tile flag001Pole = new Flag001Pole(286);	
	public static Tile flag001Top = new Flag001Top(225);	
	public static Tile flag001Flag = new Flag001Flag(288);	
	
	public static Tile cloud001Bottom = new Cloud001Bottom(562);	
	public static Tile cloud001BottomLeft = new Cloud001BottomLeft(561);
	public static Tile cloud001BottomRight = new Cloud001BottomRight(563);
	public static Tile cloud001Top = new Cloud001Top(551);
	public static Tile cloud001TopLeft = new Cloud001TopLeft(550);
	public static Tile cloud001TopRight = new Cloud001TopRight(552);
	public static Tile greenMountain001Base = new GreenMountain001Base(279);
	public static Tile greenMountain001DotsLeft = new GreenMountain001DotsLeft(278);
	public static Tile greenMountain001DotsRight = new GreenMountain001DotsRight(280);
	public static Tile greenMountain001LeftIncline = new GreenMountain001LeftIncline(252);
	public static Tile greenMountain001RightIncline = new GreenMountain001RightIncline(254);
	public static Tile greenMountain001Top = new GreenMountain001Top(253);
	public static Tile greenYellowBush001Left = new GreenYellowBush001Left(281);
	public static Tile greenYellowBush001Middle = new GreenYellowBush001Middle(282);
	public static Tile greenYellowBush001Right = new GreenYellowBush001Right(283);
	public static Tile pipeGreen001BottomLeft = new PipeGreen001BottomLeft(270);
	public static Tile pipeGreen001BottomRight = new PipeGreen001BottomRight(271);
	public static Tile pipeGreen001TopLeft = new PipeGreen001TopLeft(244);
	public static Tile pipeGreen001TopRight = new PipeGreen001TopRight(245);
	public static Tile puzzleBlock001 = new PuzzleBlock001(24);
	public static Tile rock001 = new Rock001(0);

	public static Tile bg001Sky = new BG001Sky(569);
	public static Tile missingTile = new MissingTile(998);
	
	//CLASS
	
	public static final int TILEWIDTH = 32, TILEHEIGHT = 32;
	
	protected BufferedImage texture;
	//Every id should be unique, so final
	protected final int id;
	
	public Tile(BufferedImage texture, int id) {
		this.texture = texture;
		this.id = id;
		
		//Sets tiles array at index (id) equal to current Tile being constructed
		tiles[id] = this;
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g, int x, int y) {
		g.drawImage(texture,  x,  y, TILEWIDTH * GVar.getMultiplier(), TILEHEIGHT * GVar.getMultiplier(), null);
	}
	
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return false;
	}
	
	/*************** GETTERS and SETTERS ***************/

	public int getId() {
		return id;
	}
	
}
