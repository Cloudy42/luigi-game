package dev.lepauley.luigi.levels;

import java.awt.Graphics;

import dev.lepauley.luigi.tiles.Tile;

/*
 * Contains Everything that every level must have
 */

public class Level {

	private int width, height;
	//will store tile id's in a x by y multidimensional array
	private int[][] tiles;
	
	
	public Level(String path) {
		loadLevel(path);
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		//Start with y for loop first because it can prevent issues (he didn't explain why)
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				getTile(x,y).render(g, x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT);
			}
		}
	}
	
	//Takes tile array and indexes at whatever tile is in the tile array at each x and y position
	public Tile getTile(int x, int y) {
		Tile t = Tile.tiles[tiles[x][y]];
		//If cannot find a result, return missingTile to point out that there is an issue
		if(t == null) 
			return Tile.missingTile;
		return t;
	}
	
	//Gets data from txt file and stores in tiles multidimensional array
	public void loadLevel(String path) {
		width = 5;
		height = 5;
		tiles = new int[width][height];
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				tiles[x][y] = 0;
			}
		}
		
	}
}
