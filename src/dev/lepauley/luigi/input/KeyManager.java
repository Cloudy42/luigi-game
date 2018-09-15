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
	             , scaleUp, scaleDown
	             , changePlayer, debugToggle;
	
	public KeyManager() {
		keys = new boolean[256];
	}
	
	public void tick() {
		up = keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_D];
		
		//Increases and Decreased game Scales (Entities, Tiles, etc.)
		scaleDown = keys[KeyEvent.VK_COMMA];
		scaleUp = keys[KeyEvent.VK_PERIOD];		
		
		//Swap Between available players
		changePlayer = keys[KeyEvent.VK_C];
		debugToggle = keys[KeyEvent.VK_Z];
		
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

}
