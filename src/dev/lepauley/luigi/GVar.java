package dev.lepauley.luigi;

/*
 * Class of global variables to use throughout code:
 * 	- multiplier
 * 		Adjust size of all assets (e.g. tiles, entities, act like a zoom essentially)
 * 	- <placeholder variable name>
 * 		<PH description>
 */

/*
 * Locations that multiplier variable is used:
 * 1. 
 * 2. 
 */
public class GVar {

	//Manages how small and large entites can scale to
	public static final int MIN_SCALE = 1;
	public static final int MAX_SCALE = 5;
	
	//Multiplier for speed/scale/etc.
	private static int multiplier = 1;

	//Denotes whether debug mode is active or not
	private static boolean debug = true;

	/*************** GETTERS and SETTERS ***************/
	
	public static int getMultiplier() {
		return multiplier;
	}

	public static void setMultiplier(int multiplier) {
		GVar.multiplier = multiplier;
	}
	
	public static void changeMultiplier(int x) {
		multiplier += x;
	}

	public static boolean getDebug() {
		return debug;
	}
	public static void toggleDebug() {
		if(debug) 
			debug = false;
		else
			debug = true;
	}

	
	
}
