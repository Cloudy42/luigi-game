package dev.lepauley.luigi.utilities;

/*
 * Holds SFX String names in an Enum
 */


public enum EnumSFX {
	 OneUp("1-Up")
    ,BowserFalls("BowserFalls")
	,BowserFire("BowserFire")
	,BreakBlock("BreakBlock")
	,Bump("Bump")
	,Coin("Coin")
	,Fireball("Fireball")
	,Fireworks("Fireworks")
	,Flagpole("Flagpole")
	,GameOver("GameOver")
	,JumpSmall("Jump(Small)")
	,JumpBig("Jump(Big)")
	,Kick("Kick")
	,LuigiDie("LuigiDie")
	,Pause("Pause")
	,Pipe("Pipe")
	,PowerUp("PowerUp")
	,StageClear("StageClear")
	,Stomp("Stomp")
	,Vine("Vine")
	,Warning("Warning")
	,WorldClear("WorldClear");
	
	//Pulled below from: https://stackoverflow.com/questions/3978654/best-way-to-create-enum-of-strings
	private final String text;

	EnumSFX(final String text) {
		this.text = text;
	}

    @Override
    public String toString() {
        return text;
    }
}
