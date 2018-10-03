package dev.lepauley.luigi.utilities;

import java.util.HashMap;
import java.util.Map;

/*
 * Handles all fonts in game
 */

public class FontManager {

	//Holds all fonts in a hashmap,
	//which we can use EnumFont to access without needing to memorize all names
	public static Map<EnumFont, String> mapFonts = new HashMap<EnumFont, String>();

	//Constructor
	public FontManager() {
		
		//Puts all colors into hashmap
		populateHashMap();
	}

	//Populate Hashmap as a convenient way to get Color in an easy to read/code way
	public void populateHashMap() {

		//Holds all Colors
		mapFonts.put(EnumFont.Arial,					"Arial");
		mapFonts.put(EnumFont.LucidaSansUnicode,		"Lucida Sans Unicode");
		mapFonts.put(EnumFont.ComicSans,				"Comic Sans");
		mapFonts.put(EnumFont.SansSerif,				"Sans Serif");
		 
	}
	
}
