package dev.lepauley.luigi.utilities;

/*
 * Holds Music String names in an Enum
 */


public enum EnumMusic {
     RunningAround("Running Around")
	,RunningAround_Hurry("Running Around (Hurry!)")
	,Underground("Underground")
	,Underground_Hurry("Underground (Hurry!)")
	,SwimmingAround("Swimming Around")
	,SwimmingAround_Hurry("Swimming Around (Hurry!)")
	,BowserCastle("Bowser's Castle")
	,BowserCastle_Hurry("Bowser's Castle (Hurry!)")
	,Invincible("Invincible")
	,Invincible_Hurry("Invincible (Hurry!)")
	,LevelComplete("Level Complete")
	,BowserCastleComplete("Bowser's Castle Complete")
	,YouHaveDied("You Have Died")
	,GameOver("Game Over")
	,GameOver_Alternative("Game Over (Alternative)")
	,IntoThePipe("Into The Pipe")
	,IntoThePipe_Hurry("Into The Pipe (Hurry!)")
	,SavedThePrincess("Saved The Princess");
	
	//Pulled below from: https://stackoverflow.com/questions/3978654/best-way-to-create-enum-of-strings
	//I ignored toString() override since .name() works just fine for our needs
	//I honestly don't understand why it needs the input parameter, but whatever, keeping for now
	private final String text;

	EnumMusic(final String text) {
		this.text = text;
	}

    @Override
    public String toString() {
        return text;
    }

}
