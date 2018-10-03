package dev.lepauley.luigi.utilities;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/*
 * Handles all colors in game
 */

public class ColorManager {

	//Holds all colors in a hashmap,
	//which we can use EnumColor to access without needing to memorize all names
	public static Map<EnumColor, Color> mapColors = new HashMap<EnumColor, Color>();

	//Constructor
	public ColorManager() {
		
		//Puts all colors into hashmap
		populateHashMap();
	}

	//Populate Hashmap as a convenient way to get Color in an easy to read/code way
	public void populateHashMap() {

		//Holds all Colors
		mapColors.put(EnumColor.BrightOrange,		new Color(214, 139,  19));
		mapColors.put(EnumColor.BrightGreen, 		new Color(116, 244,  66));
		mapColors.put(EnumColor.BrightPurple, 		new Color(147,  33, 239));
		mapColors.put(EnumColor.PrimaryRed, 		new Color(229,  45,  25));
		mapColors.put(EnumColor.PrimaryWhite, 		new Color(255, 255, 255));
		mapColors.put(EnumColor.PrimaryBlack, 		new Color(  0,   0,   0));
	}
	
}
