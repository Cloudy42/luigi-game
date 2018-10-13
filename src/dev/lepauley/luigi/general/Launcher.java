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
	    
	    //Starts new game with underlying display built in 
		Game game = new Game(formatter.format(date) + " - I'mma Waruigi, I'mma gonna comment mah code!!", GVar.GAME_WIDTH, GVar.GAME_HEIGHT);
		
		//Start game! Run, initialize, game loop
		game.start();
		
		//Tracks if player abruptly closes window without safely exiting and saves settings file
		//Note: code pulled from here: https://stackoverflow.com/questions/5731893/detecting-when-a-java-application-closes
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    @Override
		    public void run() {
		    	
		    	//Writes settings file
		        Utilities.writeSettingsFile();
		    }
		});
	}
}
