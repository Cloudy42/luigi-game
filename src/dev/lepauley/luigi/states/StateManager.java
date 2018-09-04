package dev.lepauley.luigi.states;

/*
 * Class to manage all states
 * Codenmore had this code contained in State class
 * 
 * Auto generated getters/setters, so method names use "CurrentState"
 * instead of just "state" as in Codenmore video
 */

public class StateManager {

	//Holds whatever current state of game is
	private static State currentState = null;

	//In case we ever need or want to know current state
	public static State getCurrentState() {
		return currentState;
	}

	//Set current state (e.g. actual method to switch between states)
	public static void setCurrentState(State currentState) {
		StateManager.currentState = currentState;
	}
	
	
	
}
