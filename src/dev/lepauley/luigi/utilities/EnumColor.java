package dev.lepauley.luigi.utilities;

/*
 * Holds Color String names in an Enum
 */

public enum EnumColor {
	 LightGreen		("Light Green")
	,LightPurple	("Light Purple")
	,BrightGreen	("Bright Green")
	,BrightOrange	("Bright Orange")
	,BrightPurple	("Bright Purple")
	,PrimaryRed		("Primary Red")
	,PrimaryWhite	("Primary White")
	,PrimaryBlack	("Primary Black")
	,OtherTan		("Other Tan")
	,OtherDarkGrey	("Other Dark Grey");
	
	//Pulled below from: https://stackoverflow.com/questions/3978654/best-way-to-create-enum-of-strings
	private final String text;

	EnumColor(final String text) {
		this.text = text;
	}

    @Override
    public String toString() {
        return text;
    }
}
