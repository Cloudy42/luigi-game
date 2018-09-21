package dev.lepauley.luigi.utilities;

/*
 * Holds SFX String names in an Enum
 */


public enum EnumSFX {
	 OneUp("1-Up")
    ,BowserFalls("Bowser Falls")
	,BowserFire("Bowser Fire")
	,BreakBlock("Break Block")
	,Bump("Bump")
	,Coin("Coin")
	,Fireball("Fireball")
	,Fireworks("Fireworks")
	,Flagpole("Flagpole")
	,GameOver("Game Over")
	,JumpSmall("Jump (Small)")
	,JumpBig("Jump (Big)")
	,Kick("Kick")
	,LuigiDie("Luigi Die")
	,Pause("Pause")
	,Pipe("Pipe")
	,PowerUp("Power Up")
	,StageClear("Stage Clear")
	,Stomp("Stomp")
	,Vine("Vine")
	,Warning("Warning")
	,WorldClear("World Clear");
	
	//Pulled below from: https://stackoverflow.com/questions/3978654/best-way-to-create-enum-of-strings
	//I ignored toString() override since .name() works just fine for our needs
	//I honestly don't understand why it needs the input parameter, but whatever, keeping for now
	private final String text;

	EnumSFX(final String text) {
		this.text = text;
	}

    @Override
    public String toString() {
        return text;
    }
}
