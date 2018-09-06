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

	private static int multiplier = 1;

	//Manages how small and large entites can scale to
	public static final int MIN_SCALE = 1;
	public static final int MAX_SCALE = 5;
	
	public static int getMultiplier() {
		return multiplier;
	}
	public static void setMultiplier(int multiplier) {
		GVar.multiplier = multiplier;
	}
	
	public static void changeMultiplier(int x) {
		multiplier += x;
	}
	
}
