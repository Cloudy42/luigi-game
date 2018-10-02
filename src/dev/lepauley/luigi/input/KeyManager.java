package dev.lepauley.luigi.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
 * Handles keyboard input
 * Keys pressed, released, etc.
 */
public class KeyManager implements KeyListener {

	 //Array to determine true (pressed) or false (not pressed) for every key 
	 //Location in array determined by key code of key (see getKeyCode method calls)
	private boolean[] keys;
	
	//Specific keys we're using
	public boolean /*DIRECTIONS*/ 
						up, down, left, right
				   /*STATE PROGRESSION*/
						, start, exit
				   /*DECREMENT/INCREMENT*/
						, fpsDown, fpsUp
						, timeDown, timeUp
						, volumeDown, volumeUp
						, scaleDown, scaleUp
				   /*CYCLE SELECTIONS*/
						, changePlayer, nextSong
				   /*TOGGLE MODES*/
						, debugToggle, scrollToggle, scrollDirection, pauseToggle, keyManualToggle;

	//Used for displaying controls in game in one easy to loop array
	private String[] keyManual = {"[A] Left"
			                     ,"[D] Right"
			                     ,"[W] Up"
			                     ,"[S] Down"
			                     ,"[Enter] Start"
			                     ,"[Esc] Exit"
			                     ,"[P] Pause Toggle"
			                     ,"[X] Scroll Toggle"
			                     ,"[V] Scroll Direction"
			                     ,"[D] Debug Toggle"
			                     ,"[K] Control Toggle"
			                     ,"[N] Next Song"
			                     ,"[1/2] FPS--/++"
			                     ,"[3/4] Time--/++"
			                     ,"[5/6] Volume--/++"
			                     ,"[</>] Scale--/++"
			                     ,"[C] Change Player"
			                    };
		
	//Constructor that creates base key array
	public KeyManager() {
		keys = new boolean[256];
	}
	
	//Updates and gets key presses
	public void tick() {
		//*DIRECTION*/
		//Navigate menus/ move player
		up = keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_D];

		//*STATE PROGRESSION*/
		//Helps Enter/Exit States (Variable depennding on what state you're in)
		start = keys[KeyEvent.VK_ENTER];
		exit = keys[KeyEvent.VK_ESCAPE];
		
		//*DECREMENT/INCREMENT*/
		//Decreases and Increases game FPS
		fpsDown = keys[KeyEvent.VK_1];
		fpsUp = keys[KeyEvent.VK_2];		
		
		//Decreases and Increases game Time
		timeDown = keys[KeyEvent.VK_3];
		timeUp = keys[KeyEvent.VK_4];		
		
		//Decreases and Increases game Volume
		volumeDown = keys[KeyEvent.VK_5];
		volumeUp = keys[KeyEvent.VK_6];		
		
		//Decreases and Increases game Scale (Entities, Tiles, etc.)
		scaleDown = keys[KeyEvent.VK_COMMA];
		scaleUp = keys[KeyEvent.VK_PERIOD];		
		
		//*CYCLE SELECTIONS*/
		//Swap Between available players
		changePlayer = keys[KeyEvent.VK_C];
		
		//Plays Next song
		nextSong = keys[KeyEvent.VK_N];
		
		//*TOGGLE MODES*/
		//Toggles Debug Display
		debugToggle = keys[KeyEvent.VK_Z];
		
		//Toggles Level Scrolling (temporary)
		scrollToggle = keys[KeyEvent.VK_X];

		//Toggles Level Scrolling Direction (temporary)
		scrollDirection = keys[KeyEvent.VK_V];

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

	//Gets array of controls (used to display on screen)
	public String[] getKeyManual() {
		return keyManual;
	}
	
}
