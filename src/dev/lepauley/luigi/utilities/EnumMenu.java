package dev.lepauley.luigi.utilities;

/*
 * Holds Menu String names in an Enum
 */

public enum EnumMenu {
	 OnePlayer		("One Player")
	,TwoPlayer		("Two Player")
	,Continue		("Continue")
	,Options		("Options");
	
	//Pulled below from: https://stackoverflow.com/questions/3978654/best-way-to-create-enum-of-strings
	private final String text;

	EnumMenu(final String text) {
		this.text = text;
	}

    @Override
    public String toString() {
        return text;
    }
}
