package dev.lepauley.luigi.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
 * Handles keyboard input
 * Keys pressed, released, etc.
 */
public class KeyManager implements KeyListener {

	/*
	 * Array to determine true (pressed) or false (not pressed) of every key 
	 * Location in array determined by key code of key (see getKeyCode method calls)
	 */
	private boolean[] keys;
	
	//Specific keys we're using
	public boolean up, down, left, right
				 , start, exit
	             , scaleUp, scaleDown
	             , changePlayer
	             , debugToggle, scrollingToggle, pauseToggle, keyManualToggle;

	//Used for displaying controls in game
	private String[] keyManual = {"[A] Left"
			                    ,"[D] Right"
			                    ,"[W] Up"
			                    ,"[S] Down"
			                    ,"[Enter] Start"
			                    ,"[Esc] Exit"
			                    ,"[P] Pause"
			                    ,"[D] Debug"
			                    ,"[K] Controls"
			                    ,"[<] Scale Down"
			                    ,"[>] Scale Up"
			                    ,"[C] Change Player"
			                    };
		
	public KeyManager() {
		keys = new boolean[256];
	}
	
	public void tick() {
		up = keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_D];

		//Helps Enter/Exit States (Variable depennding on what state you're in)
		start = keys[KeyEvent.VK_ENTER];
		exit = keys[KeyEvent.VK_ESCAPE];
		
		//Increases and Decreased game Scales (Entities, Tiles, etc.)
		scaleDown = keys[KeyEvent.VK_COMMA];
		scaleUp = keys[KeyEvent.VK_PERIOD];		
		
		//Swap Between available players
		changePlayer = keys[KeyEvent.VK_C];
		
		//Toggles Debug Display
		debugToggle = keys[KeyEvent.VK_Z];
		
		//Toggles Pause (which should stop all ticking)
		pauseToggle = keys[KeyEvent.VK_P];
		
		//Toggles Key Manual Display
		keyManualToggle = keys[KeyEvent.VK_K];
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// Whenever key is pressed
		keys[e.getKeyCode()] = true;
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Whenever key is released
		keys[e.getKeyCode()] = false;
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// Used in far future, not right now
		
	}
	
	/*************** GETTERS and SETTERS ***************/
	public String[] getKeyManual() {
		return keyManual;
	}
	

}
