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
	
	//Check if key has literally just been pressed (remains true for a tick)
	//   or if key cannot currently be pressed (meaning it has been pressed, but logic hasn't
	//   registered that key was released, meaning it can't return true for being pressed again)
	//See tick method for more
	private boolean[] justPressed, cantPress;
	
	//Specific keys we're using
	public boolean /*DIRECTIONS*/ 
						up, down, left, right
				   /*STATE PROGRESSION*/
						, start, exit
				   /*DECREMENT/INCREMENT*/
						, fpsDown, fpsUp
						, timeDown, timeUp
						, volumeDown, volumeUp
						, pitchDown, pitchUp
						, rateDown, rateUp
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
			                     ,"[7/8] Pitch--/++"
			                     ,"[9/0] Rate--/++"
			                     ,"[</>] Scale--/++"
			                     ,"[C] Change Player"
			                    };
		
	//Constructor that creates base key array
	//And other arrays of same length, for controlling key press timing
	public KeyManager() {
		keys = new boolean[256];
		justPressed = new boolean[keys.length];
		cantPress = new boolean[keys.length];
	}
	
	//Test method for new justPressed / cantPress code
	public boolean keyJustPressed(int keyCode){
		if(keyCode < 0 || keyCode >= keys.length)
				return false;
		return justPressed[keyCode];
	}
	
	//Updates and gets key presses
	public void tick() {
		/*
		 * 1) If cantPress, meaning key was held for at least one tick already, 
		 * 		but it actually isn't currently being pressed, then it can be pressed 
		 * 		again, so reset cantPress, so key can be pressed again
		 * 
		 * 2) Don't press again until key is released. And reset justPressed since 
		 * 		it has already been pressed for one tick, thus no longer "Just" pressed
		 * 
		 * 3) If we ARE able to press a key and key is currently being pressed, 
		 * 		i.e. just been pressed, then set justPressed to true
		 */
		
		//Cycle through all keys
		for(int i = 0; i < keys.length; i++) {
			if(cantPress[i] && !keys[i]) {			//1
				cantPress[i] = false;
			}
			else if(justPressed[i]) {				//2
				cantPress[i] = true;
				justPressed[i] = false;
			}
			if(!cantPress[i] && keys[i]) {			//3
				justPressed[i] = true;
			}
		}
		
		//Test code for ensuring above code about key just being pressed
		//Eventually tie to debug mode, ideally?
		//Get print out of i to actually translate to actual key?
		for(int i = 0; i < keys.length; i++) {
			if(keyJustPressed(i))
				System.out.println("Key code " + i + " pressed.");
		}
		
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
		//Note: We're adjusting game audio speed in tandem with this
		fpsDown = keys[KeyEvent.VK_1];
		fpsUp = keys[KeyEvent.VK_2];		
		
		//Decreases and Increases game header Time
		timeDown = keys[KeyEvent.VK_3];
		timeUp = keys[KeyEvent.VK_4];		
		
		//Decreases and Increases game audio Volume
		volumeDown = keys[KeyEvent.VK_5];
		volumeUp = keys[KeyEvent.VK_6];		
		
		//Decreases and Increases game audio Pitch
		pitchDown = keys[KeyEvent.VK_7];
		pitchUp = keys[KeyEvent.VK_8];		
				
		//Decreases and Increases game audio Rate
		rateDown = keys[KeyEvent.VK_9];
		rateUp = keys[KeyEvent.VK_0];		
		
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
		//In case key pressed is index is -1 or higher than current index, 256, just return out
		if(e.getKeyCode() < 0 || e.getKeyCode() > keys.length)
			return;
		
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
