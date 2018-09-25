package dev.lepauley.luigi.utilities;

/*
 * Holds Pause Message String names in an Enum
 */


public enum EnumPause {
     PAUSED("PAUSED")
	,TIMESUP("TIME UP!")
	,STOP("")
	,RESUME("RESUME");
	
	//Pulled below from: https://stackoverflow.com/questions/3978654/best-way-to-create-enum-of-strings
	//I ignored toString() override since .name() works just fine for our needs
	//I honestly don't understand why it needs the input parameter, but whatever, keeping for now
	private final String text;

	EnumPause(final String text) {
		this.text = text;
	}

    @Override
    public String toString() {
        return text;
    }

}
