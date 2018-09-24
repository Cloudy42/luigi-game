package dev.lepauley.luigi.levels;

import java.awt.Graphics;

import dev.lepauley.luigi.GVar;
import dev.lepauley.luigi.Game;
import dev.lepauley.luigi.gfx.Assets;
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
	
	//Used to test all tiles are there (Allows Scrolling through level)
	private int debugScrollLevel = 0;
	private int debugConst = 4;
	
	
	public Level(String path) {
		loadLevel(path);
	}
	
	public void tick() {
		if(!Game.gameHeader.getDead()) {
			//Used to test scrolling level, IF scrolling is toggled
			debugScrollLevel+=debugConst;
			if(debugScrollLevel <= 0 || debugScrollLevel >= 5700) 
				debugConst *= -1;
			//Or Can set value here and look at specific areas of map 
			//debugScrollLevel = 5700;
		}
	}
	
	public void render(Graphics g) {
		//Start with y for loop first because it can prevent issues (he didn't explain why)

		//Honestly not sure why I was doing this and not just drawing a big rectangle in the background.
		//The only thing I can think of is because you mentioned wanting to do stars and such eventually
		//so if/when that time comes, we can revisit, but for now I'm gonna just stretch since only sky atm
		//and leaving old code so easy enough to swap:
		g.drawImage(Assets.bg001Sky, 0, 0, GVar.GAME_WIDTH, GVar.GAME_HEIGHT, null);
			/*************** Temporary Fix: START ***************/
			//Temporary Fix for transparency: VERY inefficient! Likely we'd want 
			//to target which are transparent and add background for those only. Should be
			//easy enough...
			//for(int y = 0; y < height; y++) {
			//	for(int x = 0; x < width; x++) {
			//		Tile.bg001Sky.render(g, x * Tile.TILEWIDTH * GVar.getMultiplier(), y * Tile.TILEHEIGHT * GVar.getMultiplier());
			//	}
			//} 
			/*************** Temporary Fix: END ***************/

		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				//If NOT a BG tile, will get tile and print to screen
				if(checkTile(x,y)) {
					//debugScrollLevel used to test scrolling level:
					getTile(x,y).render(g, x * Tile.TILEWIDTH * GVar.getMultiplier() - debugScrollLevel, y * Tile.TILEHEIGHT * GVar.getMultiplier());
				}
			}
		}
	}
	
	//Borrows same logic from getTile, BUT returns false if a BG tile, thus stopping it from being drawn and saving memory.
	public boolean checkTile(int x, int y) {
		//If a BG tile, return false to stop from drawing to screen
		if(getTile(x,y) == Tile.bg001Sky)
			return false;
		return true;
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

	/*************** GETTERS and SETTERS ***************/
	
	public int getSpawnX() {
		return spawnX;
	}

	public int getSpawnY() {
		return spawnY;
	}
	
	public void setDebugScrollLevel(int i) {
		debugScrollLevel = i;
	}
}
