package dev.lepauley.luigi.utilities;

import java.util.HashMap;
import java.util.Map;

/*
 * Handles all menu selections in game
 */

public class MenuManager {

	//Holds all menu selections in a hashmap,
	//which we can use EnumMenu to access without needing to memorize all names
	public static Map<Integer, EnumMenu> mapMenus = new HashMap<Integer, EnumMenu>();

	//Constructor
	public MenuManager() {
		
		//Puts all colors into hashmap
		populateHashMap();
	}

	//Populate Hashmap as a convenient way to get Color in an easy to read/code way
	public void populateHashMap() {

		//Holds all Menu selections 
		mapMenus.put(0, 		EnumMenu.OnePlayer);
		mapMenus.put(1, 		EnumMenu.TwoPlayer);
		mapMenus.put(2, 		EnumMenu.Continue);
		mapMenus.put(3, 		EnumMenu.Options);
	}
	
	public int getSize() {
		return mapMenus.size();
	}
	
}
