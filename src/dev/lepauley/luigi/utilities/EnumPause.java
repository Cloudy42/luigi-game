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
	private final String text;

	EnumPause(final String text) {
		this.text = text;
	}

    @Override
    public String toString() {
        return text;
    }

}
