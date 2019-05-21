package dev.lepauley.luigi.general;

import static dev.lepauley.luigi.utilities.Utilities.print;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import dev.lepauley.luigi.audio.Audio;
import dev.lepauley.luigi.display.Display;
import dev.lepauley.luigi.gfx.Assets;
import dev.lepauley.luigi.gfx.GameCamera;
import dev.lepauley.luigi.gfx.Header;
import dev.lepauley.luigi.input.KeyManager;
import dev.lepauley.luigi.states.GameState;
import dev.lepauley.luigi.states.MenuState;
import dev.lepauley.luigi.states.State;
import dev.lepauley.luigi.states.StateManager;
import dev.lepauley.luigi.tiles.Tile;
import dev.lepauley.luigi.utilities.ColorManager;
import dev.lepauley.luigi.utilities.EnumColor;
import dev.lepauley.luigi.utilities.EnumMenu;
import dev.lepauley.luigi.utilities.FontManager;
import dev.lepauley.luigi.utilities.MenuManager;
import dev.lepauley.luigi.utilities.Utilities;

/*
 * Main class for game - holds all base code: 
 *     1.) Starts Game
 *     2.) Runs Game
 *     3.) Closes Game
 */

//Runnable allows Game to run on a thread (a mini program within the bigger program)
//Note: Requires a public void run(){} method otherwise will get error
public class Game implements Runnable {

	//Tracks General variables
	private Display display;
	public String title;
	private int width, height;

	//Current Font Size
	public int currentFontSize;
	
	//Used to access all game audio
	public static Audio gameAudio = new Audio();
	
	//Used to access all menu/game header info
	public static Header gameHeader = new Header();
	
	//Used to access all keyboard controls
	public static KeyManager keyManager = new KeyManager();
		
	//Used to access all colors
	public static ColorManager colorManager = new ColorManager();
	
	//Used to access all menu selections
	public static MenuManager menuManager = new MenuManager();
	
	//Used to access all fonts
	public static FontManager fontManager = new FontManager();
		
	//Used to access Game Camera	
	private GameCamera gameCamera;
	
	//While Running = true, game will loop
	private boolean running = false;

	//sets to true when game is loaded and ready for various things (settings file to name one)
	private static boolean loaded = false;
	
	//Thread via Runnable
	private Thread thread;
	
	//Used to display FPS.
	long timer = 0;
	int ticks = 0;
	int lastTicks = GVar.FPS;
	
	/*
	 * A way for computer to draw things to screen, using buffers
	 *    - Buffer is like a hidden computer screen, drawing behind the scenes
	 *    - Draw to first buffer, then push this to 2nd buffer, THEN push to actual screen
	 *    - Prevents flickering in game, of drawing to screen in real time
	 */
	private BufferStrategy bs;
	private Graphics g;
	
	//State objects
	private static State gameState, menuState;
	
	//Constructor
	public Game(String title, int width, int height) {
		
		//sets general variables
		this.title = title;
		this.width = width;
		this.height = height;

		//Resets GVar variables to default values
		GVar.resetGVarDefaults();
	}
	
	//Called only once to initialize all of the graphics and get everything ready for game
	private void init() {

		//Sets display for Game instance
		display = new Display(title, width, height);
		
		//Needed just so we can manage keys
		display.getFrame().addKeyListener(keyManager);
		
		//Loads all SpriteSheets to objects
		Assets.init();

		//Handler time
		handler = new Handler(this);
		
		//initializes the gameCamera at 0,0
		gameCamera = new GameCamera(handler, 0,0);
		
		//initializes States and puts game in as parameter
		gameState = new GameState(handler);
		menuState = new MenuState(handler);
		
		//Sets current state = "menuState", where we will start game (for now)
		StateManager.setCurrentState(menuState);

	}
	
	//Update everything for game
	private void tick(){

		//Give key manager current state, temporary until we get handler
		keyManager.setCurrentState(StateManager.getCurrentStateName());
		
		//Update keys
		keyManager.tick();
		
		//Change Song if in GameState (just to shake up debugging)
		if(keyManager.nextSong && StateManager.getCurrentState() == gameState)
			gameAudio.nextSong();
		
		//Decreases Time
		if(keyManager.timeDown && StateManager.getCurrentState() == gameState)
			gameHeader.adjustTime(-1);
		
		//Increases Time
		if(keyManager.timeUp && StateManager.getCurrentState() == gameState)
			gameHeader.adjustTime(1);
		
		//Decrease FPS (only in GameState when game is NOT paused and NOT in "Stop" mode)
		if((keyManager.fpsDown && StateManager.getCurrentState() == gameState) && !GVar.getPause() && !GVar.getStop()) {

			//Decreases FPS
			GVar.setFPS(GVar.FPS - 10);
			
			//Decreases speed to coincide with lower FPS
			gameAudio.setCurrentSpeed(-0.08f);
			
			//plays slower song due to speed adjust above
			gameAudio.playAudioStagingArea("MUSIC",gameAudio.getCurrentMusic());
		}
		
		//Increase FPS (only in GameState when game is NOT paused)
		if((keyManager.fpsUp && StateManager.getCurrentState() == gameState) && !GVar.getPause()) {

			//Increases speed to coincide with higher FPS ONLY if NOT in "Stop" mode and FPS < Max
			if(!GVar.getStop() && GVar.FPS < GVar.FPS_MAX)
				gameAudio.setCurrentSpeed(0.08f);
			
			//Increases FPS
			GVar.setFPS(GVar.FPS + 10);

			//plays faster song due to speed adjust above
			gameAudio.playAudioStagingArea("MUSIC",gameAudio.getCurrentMusic());
		}
		
		//Note: I'm currently okay allowing player to change volume when paused since it makes sense to be able to change volume in a menu:
		//Decrease Volume
		if(keyManager.volumeDown) {

			//Decrease Volume
			gameAudio.setCurrentVolume("ALL",-0.1f);

			//Displays current Volume if in Debug Mode
        	if(GVar.getDebug())
        		print("CurrentVolume: " + gameAudio.getCurrentVolume("MUSIC"));
			
			//plays quieter song due to speed adjust above (but only if not dead and IN gameState)
        	if(!gameHeader.getDead() && StateManager.getCurrentState() == gameState)
        		gameAudio.playAudioStagingArea("MUSIC",gameAudio.getCurrentMusic());
		}
		
		//Increase Volume
		if(keyManager.volumeUp) {

			//Increase Volume
			gameAudio.setCurrentVolume("ALL",0.1f);

			//Displays current Volume if in Debug Mode
        	if(GVar.getDebug())
    			print("CurrentVolume: " + gameAudio.getCurrentVolume("MUSIC"));

			//plays louder song due to speed adjust above (but only if not dead and IN gameState)
        	if(!gameHeader.getDead() && StateManager.getCurrentState() == gameState)
        		gameAudio.playAudioStagingArea("MUSIC",gameAudio.getCurrentMusic());
		}		


		//Decrease Game Audio Pitch
		if(keyManager.pitchDown && !GVar.getPause()) {

			//Decrease game audio pitch
			gameAudio.setCurrentPitch(-0.1f);

			//Displays current Pitch if in Debug Mode
        	if(GVar.getDebug())
        		print("CurrentPitch: " + gameAudio.getCurrentPitch());
			
			//plays new rate due to adjustment above (but only if not dead and IN gameState)
        	if(!gameHeader.getDead() && StateManager.getCurrentState() == gameState)
        		gameAudio.playAudioStagingArea("MUSIC",gameAudio.getCurrentMusic());
		}
		
		//Increase Game Audio Pitch
		if(keyManager.pitchUp && !GVar.getPause()) {

			//Decrease game audio pitch
			gameAudio.setCurrentPitch(0.1f);

			//Displays current Pitch if in Debug Mode
        	if(GVar.getDebug())
        		print("CurrentPitch: " + gameAudio.getCurrentPitch());
			
			//plays new rate due to adjustment above (but only if not dead and IN gameState)
        	if(!gameHeader.getDead() && StateManager.getCurrentState() == gameState)
        		gameAudio.playAudioStagingArea("MUSIC",gameAudio.getCurrentMusic());
		}

		//Decrease Game Audio Rate
		if(keyManager.rateDown && !GVar.getPause()) {

			//Decrease game audio rate
			gameAudio.setCurrentRate(-0.1f);

			//Displays current Rate if in Debug Mode
        	if(GVar.getDebug())
        		print("CurrentRate: " + gameAudio.getCurrentRate());
			
			//plays new rate due to adjustment above (but only if not dead and IN gameState)
        	if(!gameHeader.getDead() && StateManager.getCurrentState() == gameState)
        		gameAudio.playAudioStagingArea("MUSIC",gameAudio.getCurrentMusic());
		}
		
		//Increase Game Audio Rate
		if(keyManager.rateUp && !GVar.getPause()) {

			//Increase game audio rate
			gameAudio.setCurrentRate(0.1f);

			//Displays current Rate if in Debug Mode
        	if(GVar.getDebug())
        		print("CurrentRate: " + gameAudio.getCurrentRate());
			
			//plays new rate due to adjustment above (but only if not dead and IN gameState)
        	if(!gameHeader.getDead() && StateManager.getCurrentState() == gameState)
        		gameAudio.playAudioStagingArea("MUSIC",gameAudio.getCurrentMusic());
		}
		
		//If a state exists (not null), then tick state
		if(StateManager.getCurrentState() != null)
			StateManager.getCurrentState().tick();
		
		//If currentState = MenuState and enter is pressed, change to GameState
		if(keyManager.start && StateManager.getCurrentState() == menuState) {

			//If Options is selected, just print for now
			//Note: I do a second check that says if you cannot continue game, then to check Continue option, since index is off
			//      There likely is a better way to account for Continue being gone, but I haven't thought of it. Pretty sure
			//      We cannot update Enums on the fly. :\ 
			if(MenuManager.mapMenus.get(((MenuState)menuState).getCurrentSelection()) == EnumMenu.Options 
		   || (!GVar.getContinueGame() && MenuManager.mapMenus.get(((MenuState)menuState).getCurrentSelection()) == EnumMenu.Continue)) {
				print("Menu: Option Menu Selected");

			//Otherwise do normal menuState things (namely, starting the game :P )
			} else {

				//Sets the current Level back to start if One or Two Player is selected
				//Note: Setting level sets both the world and the level
				if(MenuManager.mapMenus.get(((MenuState)menuState).getCurrentSelection()) == EnumMenu.OnePlayer
			     ||MenuManager.mapMenus.get(((MenuState)menuState).getCurrentSelection()) == EnumMenu.TwoPlayer) {

					//Sets Continue Game to False (needed before resetting defaults)
					GVar.setContinueGame(false);

					//Sets Player Count
					GVar.setPlayerSelectCount((((MenuState)menuState).getCurrentSelection() + 1));
					print("Game: Player Count:" + GVar.getPlayerSelectCount());

					//Resets Defaults across the board
					resetDefaults();

					//Sets level to 1-1
					((GameState)gameState).setLevel(0);

				}
				
				//If continuing game, retain player position and const from settings file
				if(GVar.getContinueGame()) {
					((GameState)gameState).getPlayer().setX((float)GVar.getPlayerPositionX());
					((GameState)gameState).getPlayer().setY((float)GVar.getPlayerPositionY());
				}
				
				//Sets currentState to now be gameState
				StateManager.setCurrentState(gameState);
			}
		}

		//If currentState = GameState and esc is pressed, change to MenuState
		if(keyManager.exit && StateManager.getCurrentState() == gameState) {

			//Pauses all audio in preparation for changing states
			gameAudio.pauseAudioStagingArea("ALL");
			
			//Set ContinueGame = true (if not dead)
			if(!Game.gameHeader.getDead())
				GVar.setContinueGame(true);
			
			//Write Settings File
			Utilities.writeSettingsFile();
			
			//Resets Defaults across the board
			resetDefaults();
			
			//Resets offSet so MenuState level is back to the default positioning
			handler.getGameCamera().setxOffset(0 + Tile.TILEWIDTH);
			
			//Sets currentState to "Menu State"
			StateManager.setCurrentState(menuState);
		}
		
		//If Debug button is pressed, toggle Debug Mode on/off
		if(keyManager.debugToggle)
			GVar.toggleDebug();

		//If currentState = GameState and levelToggle is pressed, change current Level
		if(keyManager.levelToggle && StateManager.getCurrentState() == gameState)
			((GameState)gameState).toggleLevel();
			
		//If Key Manual button is pressed, toggle Key Manual Mode on/off
		if(keyManager.controlToggle)
			GVar.toggleKeyManual();
		
		//Incrementing SubSeconds to skip to supplement secondsToSkip (helps cover the remainder)
		if(StateManager.getCurrentState() == gameState)
			gameAudio.incrementSubSecondsToSkip();

		//if timer > 1 second in nanotime, reset timer varaibles and do some time-sensitive ticking
		if(timer >= 1000000000) {

			//reset timer variables
			lastTicks = ticks;
			ticks = 0;
			timer = 0;

			//If in game state, tick Game header variables  
			//Note: Placed here to be in line with once per second. Doing elsewhere means there will be fluctuations
			//      as FPS move around, so this feels like the smarter place to put it. 
			if(StateManager.getCurrentState() == gameState)
				gameHeader.tick();
			
			//If in game state, Increment SecondsToSkip
			//Note: I'm unsure if it should be here only because we're adjusting audio speed, so if it's running faster based on 
			//      FPS, I feel like we're going to be jumping around in audio incrementing this based on IRL second ticks, no?
			if(StateManager.getCurrentState() == gameState)
				gameAudio.incrementSecondsToSkip();
		}		
		
		//Reset audio variables (could make this into a more generic reset also)
		if(keyManager.audioReset) {
			
			//False because not resetting speed (since tied to FPS)
			gameAudio.manuallyResetDefaults();
			
			//plays new audio due to adjustment above (but only if not dead and IN gameState)
        	if(!gameHeader.getDead() && StateManager.getCurrentState() == gameState)
        		gameAudio.playAudioStagingArea("MUSIC",gameAudio.getCurrentMusic());
        	
        	//Displays current values if in Debug Mode
        	if(GVar.getDebug()) {
        		print("CurrentRate:   " + gameAudio.getCurrentRate());
        		print("CurrentVolume: " + gameAudio.getCurrentVolume("MUSIC"));
        		print("CurrentPitch:  " + gameAudio.getCurrentPitch());
        	}
		}
	}
	
	//Render everything for game
	private void render() {

		/*************** INITIAL SETUP (e.g. clear screen) ***************/
		
			//Get current buffer strategy of display
			bs = display.getCanvas().getBufferStrategy();
			
			//If bs doesn't exist, create one!
			//3 should be max number of buffers we ever need (may not work with >3)
			if(bs == null) {
				display.getCanvas().createBufferStrategy(3);
				return;
			}
			
			//Graphics object is like our (magical) paint brush, way of drawing
			g = bs.getDrawGraphics();
			
			//Clear screen
			g.clearRect(0, 0, width, height);
	
		/*************** END INITIAL SETUP ***************/

		/*************** DRAW HERE ***************/
		
			//If a state exists (not null), then render it
			if(StateManager.getCurrentState() != null)
				StateManager.getCurrentState().render(g);
		
		/*************** END DRAWING ***************/
		
		/*************** BEGIN DEBUG ***************/
	
			//sets font size and font
			currentFontSize = 20;
			g.setFont (GVar.getFont(GVar.defaultFont, currentFontSize));
	
			//If Debug Mode = Active, print FPS to screen
			if(GVar.getDebug())
				Utilities.drawShadowString(g, ColorManager.mapColors.get(EnumColor.LightGreen), "FPS:" + lastTicks, 3, 23, GVar.getShadowFont(currentFontSize));
			
			//sets font and font shadow a bit smaller for controls since running out of space
			currentFontSize = 16;
			g.setFont (GVar.getFont(GVar.defaultFont, currentFontSize));
			
			//If Key Manual = Active, display controls and rectangle
			if(GVar.getKeyManual()) {
				//Draw rectangle behind since a bit hard to read font
				g.setColor(ColorManager.mapColors.get(EnumColor.OtherTan));
				g.fillRect(GVar.KEY_MANUAL_RECT_X, GVar.KEY_MANUAL_RECT_Y, GVar.KEY_MANUAL_RECT_WIDTH, currentFontSize * (keyManager.getKeyManual().length + 5));
				
				//If Key Manual Mode = Active, print Controls
				for(int i = 0; i < keyManager.getKeyManual().length; i++)
					Utilities.drawShadowString(g, ColorManager.mapColors.get(EnumColor.BrightOrange), keyManager.getKeyManual()[i], GVar.GAME_WIDTH - GVar.KEY_MANUAL_POSITION_X, GVar.KEY_MANUAL_POSITION_Y + GVar.KEY_MANUAL_OFFSET_Y * i, GVar.getShadowFont(currentFontSize));
			}
			
		/*************** END DEBUG ***************/

		//Work buffer magic (presumably to transfer between buffers, ending at screen
		bs.show();
		
		//Discard graphics object properly
		g.dispose();
		
	}
	
	//Majority of our game code will be in here
	// - Required to implement Runnable. 
	// - Is called by start() method.
	public void run() {

		//initialize all of the graphics and get everything ready for game
		init();
		
		//1 billion nanoseconds within a second. So below translates to 1 per second,
		//but nanoseconds is more exact so allows for more flexibility.
		double timePerTick;
		
		double delta = 0;
		long now;

		//Returns current time of computer in nanoseconds.
		long lastTime = System.nanoTime();
				
		//Game Loop:
		// 1.) Update all variables, positions of objects, etc.
		// 2.) Render (draw) everything to the screen
		// 3.) Repeat
		while(running){

			//Below will do how much time we have before we can call tick() and render() again.
			/*
			 * Delta is difference of now/last in nano seconds, divided by time per tick
			 * This is essentially the percentage of time per tick that has passed
			 * So delta reaching 1 or more is 100% of time per tick, thus time to tick
			 * 
			 * Not sure why doing it this way instead of just doing (now - last),
			 * and then checking if delta is >= timePerTick?
			 */
			timePerTick = 1000000000 / GVar.FPS;
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;

			//If enough time has elapsed, can run tick() and render().
			if(delta >= 1) {
				tick();
				render();
				ticks++;
				delta--;
			}
		}
		
		//Just an extra check in case the above doesn't stop it from running.
		stop();
	}
	
	//"Synchronized" is keyword for whenever we're working with threads directly (starting/stopping).
	// - Prevents things from getting "messed up"
	public synchronized void start() {

		//Check if already running game loop
		if(running) {
			return;
		} else {
			
			//Set running = true. Used in game loop
			running = true;
			
			//Initialize thread and pass in Game class
			thread = new Thread(this);
			
			//start() will actually call the run() method.
			thread.start();
		}
	}
	
	//Stops thread safely
	// - Requires Try/Catch otherwise will get error
	public synchronized void stop() {
		//Check if already running game loop
		if(!running) {
			return;
		} else {

			//Set running = false. Used in game loop
			running = false;
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	//Resets Defaults across the board
	public void resetDefaults() {
		gameHeader.resetDefaults();
		GVar.resetGVarDefaults();
		gameAudio.resetDefaults();
		
		//Resets Players Position & selection (need to cast since gameState is a State object)
		((GameState)gameState).resetPlayerDefaults();
	
		//Resets Level Tile Position (need to cast since gameState is a State object)
		((GameState)gameState).resetLevelDefaults();
	}		
	
	/*************** GETTERS and SETTERS ***************/

	//Gets whether game is loaded and ready to load settings and such
	public static boolean getLoaded() {
		return loaded;
	}
	
	//Sets loaded (indicator that game is ready to load settings and such) to true
	//(Then it loads settings :P )
	public static void setLoaded(boolean tf) {

		//Only load file if it exists
		if(GVar.settingsExists())
			GVar.loadSettings("res/files/settings.txt");

		loaded = tf;
	}
	
	//Gets gameState
	public static State getGameState() {
		return gameState;
	}

	//Gets menuState
	public static State getMenuState() {
		return menuState;
	}
	
	//Gets Width of window
	public int getWidth() {
		return width;
	}
	
	//Gets Height of window
	public int getHeight() {
		return height;
	}
	
	//Gets Game Camera
	public GameCamera getGameCamera() {
		return gameCamera;
	}
	
	//Handler
	private Handler handler;
	
	//Gets Key Manager
	public KeyManager getKeyManager() {
		return keyManager;
	}
}
