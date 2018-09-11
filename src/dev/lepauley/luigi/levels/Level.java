package dev.lepauley.luigi.levels;

import java.awt.Graphics;

import dev.lepauley.luigi.GVar;
import dev.lepauley.luigi.tiles.Tile;
import dev.lepauley.luigi.utilities.Utilities;

/*
 * Loads levels from text files and renders them to the screen
 */

public class Level {

	//Width and Height of level
	private int width, height;
	
	//X and Y Position the player will spawn at
	private int spawnX, spawnY;
	
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
				getTile(x,y).render(g, x * Tile.TILEWIDTH * GVar.getMultiplier(), y * Tile.TILEHEIGHT * GVar.getMultiplier());
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
		String file = Utilities.loadFileAsString(path);
		//Split all characters from input file using spaces ("\\s+")
		String[] tokens = file.split("\\s+");
		
		//Width of Level
		width = Utilities.parseInt(tokens[0]);

		//Height of Level
		height = Utilities.parseInt(tokens[1]);
		
		//Player Spawn X Position
		spawnX = Utilities.parseInt(tokens[2]);

		//Player Spawn X Position
		spawnY = Utilities.parseInt(tokens[3]);

		tiles = new int[width][height];
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				//Need to add 4 because we are setting first 4 elements above from level file
				tiles[x][y] = Utilities.parseInt(tokens[(x + y * width) + 4]);
			}
		}
	}
}
