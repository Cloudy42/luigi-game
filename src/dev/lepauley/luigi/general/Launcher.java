package dev.lepauley.luigi.general;

import java.text.SimpleDateFormat;
import java.util.Date;

import dev.lepauley.luigi.utilities.Utilities;

/*
 * Responsible for starting game
 */

public class Launcher {

	public static void main(String[] args){
		
	    //Used for getting current Date (from here: https://www.javatpoint.com/java-get-current-date)
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy @ hh:mm:ss a");  
	    Date date = new Date();  
	    
	    //Loads settings from Settings File
	    //Note: I'm not sure where to put this quite yet. Pretty sure it needs to be in Game but after things load but that means
	    //I need to stop some classes from doing things right away (such as Audio playing, etc.). So not as easy as it first appeared.
	    GVar.loadSettings("res/files/settings.txt");

	    //Starts new game with underlying display built in 
		Game game = new Game(formatter.format(date) + " - I'mma Waruigi, I'mma gonna comment mah code!!", GVar.GAME_WIDTH, GVar.GAME_HEIGHT);
		
		Utilities.writeSettingsFile();
		//Start game! Run, initialize, game loop
		game.start();
	}
}
