package dev.lepauley.luigi.utilities;

/*
 * Holds Music String names in an Enum
 */


public enum EnumMusic {
     RunningAbout("RunningAbout")
	,RunningAbout_Hurry("RunningAbout(Hurry!)")
	,Underground("Underground")
	,Underground_Hurry("Underground(Hurry!)")
	,SwimmingAround("SwimmingAround")
	,SwimmingAround_Hurry("SwimmingAround(Hurry!)")
	,BowserCastle("Bowser'sCastle")
	,BowserCastle_Hurry("Bowser'sCastle(Hurry!)")
	,Invincible("Invincible")
	,Invincible_Hurry("Invincible(Hurry!)")
	,LevelComplete("LevelComplete")
	,BowserCastleComplete("Bowser'sCastleComplete")
	,YouHaveDied("YouHaveDied")
	,GameOver("GameOver")
	,GameOver_Alternative("GameOver(Alternative)")
	,IntoThePipe("IntoThePipe")
	,IntoThePipe_Hurry("IntoThePipe(Hurry!)")
	,SavedThePrincess("SavedThePrincess");
	
	//Pulled below from: https://stackoverflow.com/questions/3978654/best-way-to-create-enum-of-strings
	private final String text;

	EnumMusic(final String text) {
		this.text = text;
	}

    @Override
    public String toString() {
        return text;
    }

}
