package dev.lepauley.luigi.utilities;

/*
 * Holds Font String names in an Enum
 */

public enum EnumFont {
	 Arial				("Arial")
	,SansSerif 			("Sans Serif")
	,LucidaSansUnicode	("Lucida Sans Unicode")
	,ComicSans			("Comic Sans");
	
	//Pulled below from: https://stackoverflow.com/questions/3978654/best-way-to-create-enum-of-strings
	private final String text;

	EnumFont(final String text) {
		this.text = text;
	}

    @Override
    public String toString() {
        return text;
    }
}
