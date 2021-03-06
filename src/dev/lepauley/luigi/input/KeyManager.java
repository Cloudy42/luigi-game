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
	private boolean[] throttlePress, justPressed, cantPress;
	
	//Specific keys we're using
	public boolean /*DIRECTIONS*/ 
						up, down, left, right
				   /*ACTIONS*/
						, jump
				   /*STATE PROGRESSION*/
						, start, exit
				   /*DECREMENT/INCREMENT*/
						, fpsDown, fpsUp
						, timeDown, timeUp
						, volumeDown, volumeUp
						, pitchDown, pitchUp
						, rateDown, rateUp
						, scaleDown, scaleUp
						, audioReset
				   /*CYCLE SELECTIONS*/
						, changePlayer, nextSong
				   /*TOGGLE MODES*/
						, debugToggle, levelToggle, controlToggle;

	//Used for displaying controls in game in one easy to loop array
	private String[] keyManual = {"[A] Left"
			                     ,"[D] Right"
			                     ,"[W] Up"
			                     ,"[S] Down"
			                     ,"[Space] Jump"
			                     ,"[Enter] Start/Pause"
			                     ,"[Esc] Exit"
			                     ,"[Z] Debug Toggle"
			                     ,"[L] Level Toggle"
			                     ,"[K] Control Toggle"
			                     ,"[N] Next Song"
			                     ,"[R] Audio Reset"
			                     ,"[1/2] FPS--/++"
			                     ,"[3/4] Time--/++"
			                     ,"[5/6] Volume--/++"
			                     ,"[7/8] Rate--/++"
			                     ,"[9/0] Pitch--/++"
			                     ,"[</>] Scale--/++"
			                     ,"[C] Change Player"
			                    };
	//Temp holder for current state name until we get handler?
	String currentState = null;
	
	//Used to throttle keys
	int throttleCount = 0;
	final int THROTTLE_CAP = 5;
	
	//Constructor that creates base key array
	//And other arrays of same length, for controlling key press timing
	public KeyManager() {
		keys = new boolean[256];
		throttlePress = new boolean[keys.length];
		justPressed = new boolean[keys.length];
		cantPress = new boolean[keys.length];
	}
	
	//Test method for new justPressed / cantPress code
	public boolean keyJustPressed(int keyCode){
		if(keyCode < 0 || keyCode >= keys.length)
			return false;
		return justPressed[keyCode];
	}
	
	//method for throttling keys
	public boolean throttlePress(int keyCode){
		if(keyCode < 0 || keyCode >= keys.length)
			return false;
		return throttlePress[keyCode];
	}
	
	//Updates and gets key presses
	public void tick() {

		//Increment the Throttle to see if we can click that key again
		throttleCount++;
		
		//Cycle through all keys
		//Note that the steps go backwards based on how the data is read, 
		//     otherwise it would be cleared before the program ever got to use the result
		for(int i = 0; i < keys.length; i++) {
			
			//------------
			//JustPressed:\
			//------------------------------------------------------------------------
			// Step C
			//   If cantPress, meaning key was held for at least one tick already, 
			// 		but it actually isn't currently being pressed, then it can be pressed 
			// 		again, so reset cantPress, so key can be pressed again
			if(cantPress[i] && !keys[i]) {			
				cantPress[i] = false;
			}
			
			// Step B
			//   Don't press again until key is released. And reset justPressed since 
			// 		it has already been pressed for one tick, thus no longer "Just" pressed
			else if(justPressed[i]) {				
				cantPress[i] = true;
				justPressed[i] = false;
			}
			
			// Step A
			//   If we ARE able to press a key and key is currently being pressed, 
			// 		i.e. just been pressed, then set justPressed to true
			if(!cantPress[i] && keys[i]) {			
				justPressed[i] = true;
			}
			
			//--------------
			//ThrottlePress:\
			//--------------------------------------------------------------------------
			// Step C
			//   Accounts for a glitch that was happening when releasing the throttle it would keep executing even though you let go. Resolves issue.
			if(throttlePress[i] && !keys[i]) {
				throttlePress[i] = false;
			}


			// Step B
			//   If throttle has passed the check below, reset both the key and the throttleKeys value
			if(throttlePress[i] && keys[i]) {
				throttleCount = 0;
				throttlePress[i] = false;
			}
			
			// Step A
			//   If throttle is Pressed and throttleKeys > the limit, set ThrottlePress = true
			if(!throttlePress[i] && keys[i] && throttleCount > THROTTLE_CAP) { 
				throttlePress[i] = true;
			}

		}
		
		//Test code for ensuring above code about key just being pressed
		//Tie into key debug mode, separate from debug mode since a lot
		//with all key presses when trying to debug and watch console for issues.
		//Get print out of i to actually translate to actual key?
		for(int i = 0; i < keys.length; i++) {
			//if(keyJustPressed(i))
				//System.out.println("Key code " + i + " pressed.");
		}
		
		//This syntax style may be useful somewhere, the up != up, which flips between true and false
		/*if(keyJustPressed(KeyEvent.VK_W))
			up = !up;*/
		
		//*DIRECTION*/
		//Navigate menus/ move player
		if(currentState == "GameState") {
			up = keys[KeyEvent.VK_W];
			down = keys[KeyEvent.VK_S];
			left = keys[KeyEvent.VK_A];
			right = keys[KeyEvent.VK_D];
			jump = keys[KeyEvent.VK_SPACE];
		}
		
		if(currentState =="MenuState") {
			up = keyJustPressed(KeyEvent.VK_W);
			down = keyJustPressed(KeyEvent.VK_S);
			left = keyJustPressed(KeyEvent.VK_A);
			right = keyJustPressed(KeyEvent.VK_D);
		}

		//*STATE PROGRESSION*/
		//Helps Enter/Exit States (Variable depending on what state you're in)
		//Start also pauses game in GameState
		start = keyJustPressed(KeyEvent.VK_ENTER);
		exit = keyJustPressed(KeyEvent.VK_ESCAPE);
		
		//*DECREMENT/INCREMENT*/
		//Decreases and Increases game FPS
		//Note: We're adjusting game audio speed in tandem with this
		fpsDown = throttlePress(KeyEvent.VK_1);
		fpsUp = throttlePress(KeyEvent.VK_2);		
		
		//Decreases and Increases game header Time
		timeDown = keys[KeyEvent.VK_3];
		timeUp = keys[KeyEvent.VK_4];
		
		//Decreases and Increases game audio Volume
		volumeDown = throttlePress(KeyEvent.VK_5);
		volumeUp = throttlePress(KeyEvent.VK_6);		
		
		//Decreases and Increases game audio Rate
		rateDown = throttlePress(KeyEvent.VK_7);
		rateUp = throttlePress(KeyEvent.VK_8);		
		
		//Decreases and Increases game audio Pitch
		pitchDown = throttlePress(KeyEvent.VK_9);
		pitchUp = throttlePress(KeyEvent.VK_0);		
				
		//Decreases and Increases game Scale (Entities, Tiles, etc.)
		scaleDown = keyJustPressed(KeyEvent.VK_COMMA);
		scaleUp = keyJustPressed(KeyEvent.VK_PERIOD);		
		
		//Manually reset audio variables
		audioReset = keyJustPressed(KeyEvent.VK_R);
		
		//*CYCLE SELECTIONS*/
		//Swap Between available players
		changePlayer = keyJustPressed(KeyEvent.VK_C);
		
		//Plays Next song
		nextSong = keyJustPressed(KeyEvent.VK_N);
		
		//*TOGGLE MODES*/
		//Toggles Debug Display
		debugToggle = keyJustPressed(KeyEvent.VK_Z);
		
		//Toggles Currrent Level
		levelToggle = keyJustPressed(KeyEvent.VK_L);
		
		//Toggles Controls  Display
		controlToggle = keyJustPressed(KeyEvent.VK_K);
		
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
	
	//Temp until we get handler, hold current state name for use in keys being one tick per press or not
	public void setCurrentState(String s) {
		currentState = s;
	}
}
