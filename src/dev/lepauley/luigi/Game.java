package dev.lepauley.luigi;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import dev.lepauley.luigi.audio.Audio;
import dev.lepauley.luigi.display.Display;
import dev.lepauley.luigi.gfx.Assets;
import dev.lepauley.luigi.gfx.Header;
import dev.lepauley.luigi.input.KeyManager;
import dev.lepauley.luigi.states.GameState;
import dev.lepauley.luigi.states.MenuState;
import dev.lepauley.luigi.states.State;
import dev.lepauley.luigi.states.StateManager;
import dev.lepauley.luigi.utilities.EnumMusic;
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

	private Display display;
	public String title;
	public int width, height;

	//Font Info
	public int currentFontSize;
	
	//Used to access all game audio
	public static Audio gameAudio = new Audio();
	
	//Used to house all header info
	public static Header gameHeader = new Header();
	
	//While Running = true, game will loop
	private boolean running = false;
	
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
	private State gameState, menuState;
	
	//Input
	private KeyManager keyManager;
	
	public Game(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		keyManager = new KeyManager();
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
		
		gameState = new GameState(this);
		menuState = new MenuState(this);
		StateManager.setCurrentState(menuState);
		//StateManager.setCurrentState(gameState);
	}
	
	//Update everything for game
	private void tick() {
		//Update keys
		keyManager.tick();
		
		//If a state exists (not null), then tick it
		if(StateManager.getCurrentState() != null) {
			StateManager.getCurrentState().tick();
		}
		
		//If MenuState and enter is pressed, change to GameState
		if(keyManager.start && StateManager.getCurrentState() == menuState) {
			StateManager.setCurrentState(gameState);
			Game.gameAudio.pauseAudio("sfx");
			Game.gameAudio.playAudio("music", EnumMusic.RunningAround.toString());
		}
		//If GameState and esc is pressed, change to MenuState
		if(keyManager.exit && StateManager.getCurrentState() == gameState) {

			//Reset Defaults:
			gameHeader.resetDefaults();
			GVar.resetGVarDefaults();
			
			//Resets Players Position & selection:
			((GameState)gameState).resetPlayerDefaults();

			//Resets Level Tile Position:
			((GameState)gameState).resetLevelDefaults();
			
			//pauseAudio MAY be optional here since closing anyways. Just felt safer.
			Game.gameAudio.pauseAudio("all");
			Game.gameAudio.closeAudio("all");

			StateManager.setCurrentState(menuState);
		}
		
		//If Debug button is pressed, toggle Debug Mode on/off
		if(keyManager.debugToggle)
			GVar.toggleDebug();

		//If Key Manual button is pressed, toggle Key Manual Mode on/off
		if(keyManager.keyManualToggle)
			GVar.toggleKeyManual();

		//I am moving it from the render() method 
		//to the tick() method sicne we're not drawing to console any longer, and even then, it's not really a 
		//render so makes sense to not be in render().
		if(timer > 1000000000) {
			//System.out.println("Ticks and Frames: " + ticks);
			lastTicks = ticks;
			ticks = 0;
			timer = 0;
			//Moved here to be in line with once per second. Doing elsewhere means there will be fluctuations
			//as FPS move around, so this feels like the smarter place to put it. 
			if(StateManager.getCurrentStateName() == "GameState")
				gameHeader.tick();
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
		
		/*************** DRAW HERE ***************/
		//If a state exists (not null), then render it
		if(StateManager.getCurrentState() != null) {
			StateManager.getCurrentState().render(g);
		}
		
		/*************** END DRAWING ***************/
		/*************** BEGIN DEBUG ***************/
		//FPS on screen
		currentFontSize = 20;
		g.setFont (GVar.setFont(GVar.fontA, currentFontSize));

		//If Debug Mode = Active, print FPS
		if(GVar.getDebug()) {
			Utilities.drawShadowString(g, "FPS:" + lastTicks, 3, 23, GVar.getShadowFont(currentFontSize));
		}
		
		//If Key Manual Mode = Active, print Controls
		if(GVar.getKeyManual()) {
			for(int i = 0; i < keyManager.getKeyManual().length; i++) {
				Utilities.drawShadowString(g, keyManager.getKeyManual()[i], GVar.GAME_WIDTH - GVar.KEY_MANUAL_POSITION_X, 23 + GVar.KEY_MANUAL_OFFSET_Y * i, GVar.getShadowFont(currentFontSize));
			}
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
		init();
		
		//How many times per second we want the tick() and render() methods to run.
		int fps = GVar.FPS;

		//1 billion nanoseconds within a second. So below translates to 1 per second,
		//but nanoseconds is more exact so allows for more flexibility.
		double timePerTick = 1000000000 / fps;
		
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
		}
		else {
			//Used in game loop
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
		}
		else {
			running = false;
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*************** GETTERS and SETTERS ***************/
	public KeyManager getKeyManager() {
		return keyManager;
	}
}
