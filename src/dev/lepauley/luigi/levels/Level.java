package dev.lepauley.luigi.levels;

import java.awt.Graphics;

import dev.lepauley.luigi.general.GVar;
import dev.lepauley.luigi.general.Game;
import dev.lepauley.luigi.general.Handler;
import dev.lepauley.luigi.gfx.Assets;
import dev.lepauley.luigi.tiles.Tile;
import dev.lepauley.luigi.utilities.Utilities;

/*
 * Loads levels from text files and renders them to the screen
 */

public class Level {

	//Main Handler object (which can reference game)
	private Handler handler;
	
	//Width and Height of level
	private int width, height;
	
	//X and Y Position the player will spawn at
	private int spawnX, spawnY;
	
	//level-specific song
	private String levelMusic;
	
	//will store tile id's in a x by y multidimensional array
	private int[][] tiles;
	
	//Removing but only commenting out for now. if this is found to still be here by 02/01/2018 or later, delete it! Means we forgot! :P
	//private int defaultXSpawnOffset = -Tile.TILEWIDTH;
	
	//Constructor
	public Level(Handler handler, String path) {
		this.handler = handler;
		loadLevel(path);
	}
	
	public void tick() {

	}
	
	public void render(Graphics g) {

		//Honestly not sure why I was doing this and not just drawing a big rectangle in the background.
		//The only thing I can think of is because you mentioned wanting to do stars and such eventually
		//so if/when that time comes, we can revisit, but for now I'm gonna just stretch since only sky atm
		//and leaving old code so easy enough to swap. 
		//
		//This causes issues in zones with different backgrounds but I imagine we either can:
		//  1.) Won't be an issue and can just use tiles when only rendering what's on screen (probably best solution long term - code below iirc)
		//  2.) We can just change background imagine/color when changing zones (feels more "hacky". I like above solution better if retains FPS)
		g.drawImage(Assets.bg001Sky, 0, 0, GVar.GAME_WIDTH, GVar.GAME_HEIGHT, null);

			/*************** Temporary Fix: START ***************/
			//Note: This is likely the long term solution we want when only rendering what is on screen:
			//Temporary Fix for transparency: VERY inefficient! Likely we'd want 
			//to target which are transparent and add background for those only. Should be
			//easy enough...
			//for(int y = 0; y < height; y++) {
			//	for(int x = 0; x < width; x++) {
			//		Tile.bg001Sky.render(g, x * Tile.TILEWIDTH * GVar.getMultiplier(), y * Tile.TILEHEIGHT * GVar.getMultiplier());
			//	}
			//} 
			/*************** Temporary Fix: END ***************/

		/*
		 * Below for loops used to go through all tiles, starting with 0, end with height/width
		 * Now will loop only through VISIBLE tiles, as denoted by these Start/End variables
		 * 
		 * Uses max method to ensure we never start with a negative. And offset divided by Tile width/height
		 *      to convert from pixels to actual tile number
		 *      
		 * Uses min for end, whether we reach end of map (width/height) or not there yet (game camera)
		 */
		int xStart = (int) Math.max(0, handler.getGameCamera().getxOffset() / (Tile.TILEWIDTH*GVar.getMultiplier())) ;
		int xEnd = (int) Math.min(width, (handler.getGameCamera().getxOffset() + handler.getWidth()) / (Tile.TILEWIDTH*GVar.getMultiplier()) +2);
		int yStart = (int) Math.max(0, handler.getGameCamera().getyOffset() / (Tile.TILEHEIGHT*GVar.getMultiplier())) ;
		int yEnd = (int) Math.min(height, (handler.getGameCamera().getyOffset() + handler.getHeight()) / (Tile.TILEHEIGHT*GVar.getMultiplier()) +2);
		
		//Loops through level text file and populates all tiles (inefficient until we are only rendering what is on screen) 
		//Note: Start with y for loop first because it can prevent issues (he didn't explain why)
		for(int y = yStart; y < yEnd; y++) {
			for(int x = xStart; x < xEnd; x++) {

				//If NOT a BG tile, will get tile and print to screen
				if(checkTile(x,y)) {

					//draws level, offset by GameCamera on the x and y axis
					getTile(x,y).render(g, (int)(x * Tile.TILEWIDTH * GVar.getMultiplier() - handler.getGameCamera().getxOffset())
							             , (int)(y * Tile.TILEHEIGHT * GVar.getMultiplier() - handler.getGameCamera().getyOffset()));
				}
			}
		}
	}
	
	//Borrows same logic from getTile, BUT returns false if a BG tile, thus stopping it from being drawn and saving memory.
	public boolean checkTile(int x, int y) {

		//If a BG tile, return false to stop from drawing to screen
		if(getTile(x,y) == Tile.bg001Sky)
			return false;
		
		//Otherwise, will draw to screen
		return true;
	}
	
	//Takes tile array and indexes at whatever tile is in the tile array at each x and y position
	public Tile getTile(int x, int y) {
		/*In case player somehow gets outside of game/level boundaries - i.e. a glitch - 
			do this check and return a normal tile so game doesn't crash*/
		if(x < 0 || y < 0 || x >= width || y >= height)
			return Tile.bg001Sky;
		
		Tile t = Tile.tiles[tiles[x][y]];
		
		//If cannot find a result, return missingTile to point out that there is an issue
		if(t == null) 
			return Tile.missingTile;
		
		//Returns tile as x|y index
		return t;
	}
	
	//Gets data from txt file and stores in tiles multidimensional array
	public void loadLevel(String path) {
		
		//Holds txt level path
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
		
		//Set Level Song
		levelMusic = (tokens[4]);

		//Creates tiles multidimensional array based on width and height
		tiles = new int[width][height];
		
		//Loops through width and height and adds tiles to multidimensional array
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {

				//Need to do + 5 because we are setting first 5 variables above from level file, so skip those when doing tiles
				tiles[x][y] = Utilities.parseInt(tokens[(x + y * width) + 5]);
			}
		}
	}

	/*************** GETTERS and SETTERS ***************/
	
	//Gets Player Spawn Position - X
	public int getSpawnX() {
		return spawnX;
	}

	//Gets Player Spawn Position - Y
	public int getSpawnY() {
		return spawnY;
	}

	//Gets Level Specific Music
	public String getLevelMusic() {
		if(Game.gameHeader.getHurry()) {
			return levelMusic + "(Hurry!)";
		} else {
			return levelMusic;
		}
	}		
}
