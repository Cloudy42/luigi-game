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
	             , fpsDown, fpsUp
	             , timeDown, timeUp
	             , volumeDown, volumeUp
	             , rateDown, rateUp
	             , scaleDown, scaleUp
	             , changePlayer, nextSong
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
			                    ,"[N] Next Song"
			                    ,"[1/2] FPS--/++"
			                    ,"[3/4] Time--/++"
			                    ,"[5/6] Volume--/++"
			                    ,"[7/8] Rate--/++"
			                    ,"[</>] Scale--/++"
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
		
		//Increases and Decreased game FPS
		fpsDown = keys[KeyEvent.VK_1];
		fpsUp = keys[KeyEvent.VK_2];		
		
		//Increases and Decreased game Time
		timeDown = keys[KeyEvent.VK_3];
		timeUp = keys[KeyEvent.VK_4];		
		
		//Increases and Decreased game Volume
		volumeDown = keys[KeyEvent.VK_5];
		volumeUp = keys[KeyEvent.VK_6];		
		
		//Increases and Decreased game audio speed/rate
		rateDown = keys[KeyEvent.VK_7];
		rateUp = keys[KeyEvent.VK_8];		
		
		//Increases and Decreased game Scales (Entities, Tiles, etc.)
		scaleDown = keys[KeyEvent.VK_COMMA];
		scaleUp = keys[KeyEvent.VK_PERIOD];		
		
		//Swap Between available players
		changePlayer = keys[KeyEvent.VK_C];
		
		//Plays Next song
		nextSong = keys[KeyEvent.VK_N];
		
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
