package dev.lepauley.luigi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import dev.lepauley.luigi.display.Display;

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

	//While Running = true, game will loop
	private boolean running = false;
	
	//Thread via Runnable
	private Thread thread;
	
	
	/*
	 * A way for computer to draw things to screen, using buffers
	 *    - Buffer is like a hidden computer screen, drawing behind the scenes
	 *    - Draw to first buffer, then push this to 2nd buffer, THEN push to actual screen
	 *    - Prevents flickering in game, of drawing to screen in real time
	 */
	//Haha.. bs
	private BufferStrategy bs;
	private Graphics g;
	
	public Game(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
	}
	
	//Called only once to initialize all of the graphics and get everything ready for game
	private void init() {
		//Sets display for Game instance
		display = new Display(title, width, height);
	}
	
	//Update everything for game
	private void tick() {
		
	}
	
	//Render everything for game
	private void render() {
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
		
		//Draw Here!
		g.setColor(Color.red);
		g.fillRect(-20, -20, 75, 90);
		
		g.setColor(Color.blue);
		g.fillRect(100, 100, 75, 90);
		//End Drawing!
		
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
		
		//Game Loop:
		// 1.) Update all variables, positions of objects, etc.
		// 2.) Render (draw) everything to the screen
		// 3.) Repeat
		while(running){
			tick();
			render();
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
	
	
	
	
}
