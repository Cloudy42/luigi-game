package dev.lepauley.luigi.levels;

import java.awt.Graphics;

import dev.lepauley.luigi.general.GVar;
import dev.lepauley.luigi.general.Game;
import dev.lepauley.luigi.gfx.Assets;
import dev.lepauley.luigi.tiles.Tile;
import dev.lepauley.luigi.utilities.Utilities;

/*
 * Loads levels from text files and renders them to the screen
 */

public class Level {

	//main game object
	private Game game;
	
	//Width and Height of level
	private int width, height;
	
	//X and Y Position the player will spawn at
	private int spawnX, spawnY;
	
	//level-specific song
	private String levelMusic;
	
	//will store tile id's in a x by y multidimensional array
	private int[][] tiles;
	
	//Used to test all tiles are there (Allows Scrolling through level)
	private int scrollPosition;
	private int scrollConst;
	
	private int defaultXSpawnOffset = -Tile.TILEWIDTH;
	
	//Constructor
	public Level(Game game, String path) {
		this.game = game;
		setScrollLevelDefaults();
		loadLevel(path);
	}
	
	public void tick() {

		//If player is NOT dead and scroll Toggle is enabled and NOT in "Stop" Mode
		if(!Game.gameHeader.getDead() && GVar.getScroll() && !GVar.getStop()) {

			//Used to test scrolling level, IF scrolling is toggled
			scrollPosition+=scrollConst;

			//I added "&& scrollConst < 0 and > 0" because if, for whatever reason, code ever gets behind those thresholds,
			//it will constantly be flicking from positive to negative and start a looped jitter effect
			if(scrollPosition <= Tile.TILEWIDTH && scrollConst < 0 || scrollPosition >= (width - 34) * Tile.TILEWIDTH && scrollConst > 0)
				toggleScrollConst();

			//Or Can set value here and look at specific areas of map 
			//scrollLevel = 6208;
		}
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
		int xStart = (int) Math.max(0, game.getGameCamera().getxOffset() / (Tile.TILEWIDTH*GVar.getMultiplier())) ;
		int xEnd = (int) Math.min(width, (game.getGameCamera().getxOffset() + game.getWidth()) / (Tile.TILEWIDTH*GVar.getMultiplier()) +2);
		int yStart = (int) Math.max(0, game.getGameCamera().getyOffset() / (Tile.TILEHEIGHT*GVar.getMultiplier())) ;
		int yEnd = (int) Math.min(height, (game.getGameCamera().getyOffset() + game.getHeight()) / (Tile.TILEHEIGHT*GVar.getMultiplier()) +2);
		
		//Loops through level text file and populates all tiles (inefficient until we are only rendering what is on screen) 
		//Note: Start with y for loop first because it can prevent issues (he didn't explain why)
		for(int y = yStart; y < yEnd; y++) {
			for(int x = xStart; x < xEnd; x++) {

				//If NOT a BG tile, will get tile and print to screen
				if(checkTile(x,y)) {

					//draws level, offset by GameCamera on the x and y axis
					getTile(x,y).render(g, (int)(defaultXSpawnOffset + x * Tile.TILEWIDTH * GVar.getMultiplier() - game.getGameCamera().getxOffset() - scrollPosition)
							             , (int)(y * Tile.TILEHEIGHT * GVar.getMultiplier() - game.getGameCamera().getyOffset()));
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
	
	//Sets Default Scroll Level Values
	public void setScrollLevelDefaults() {
		
		//if not continuing game, reset scroll position and const so starts at beginnging of level
		if(!GVar.getContinueGame()) {
			GVar.resetGVarDefaults();
		}
		
		//ScrollLevel is where the level will start at relative to x position
		scrollPosition = GVar.getScrollPosition();
		
		//ScrollConst is how fast level will scroll
		scrollConst = GVar.getScrollConst();
	}
	
	//Get Scroll Position (how much level has scrolled)
	public int getScrollPosition() {
		return scrollPosition;
	}
	
	//Set Scroll Position (how much level has scrolled)
	public void setScrollPosition(int i) {
		scrollPosition = i;
	}
	
	//Get Scroll Constant (direction Scrolling is going)
	public int getScrollConst() {
		return scrollConst;
	}
	
	//Set Scroll Constant (direction Scrolling is going)
	public void setScrollConst(int i) {
		scrollConst = i;
	}
	
	//Toggles Scroll Constant (direction Scrolling is going)
	public void toggleScrollConst() {
		scrollConst *= -1;
	}
	
}
