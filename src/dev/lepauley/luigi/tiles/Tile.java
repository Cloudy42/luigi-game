package dev.lepauley.luigi.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/*
 * Contains Everything that every tile must have
 */

public class Tile {

	//STATIC VARIABLES
	
	public static Tile[] tiles = new Tile[256];
	public static Tile skyTile = new SkyTile(0);
	public static Tile block1Tile = new Block1Tile(1);
	public static Tile block2Tile = new Block2Tile(2);
	public static Tile block3Tile = new Block3Tile(3);
	public static Tile missingTile = new MissingTile(255);
	
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
		g.drawImage(texture,  x,  y, TILEWIDTH, TILEHEIGHT, null);
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
