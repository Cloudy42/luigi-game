package dev.lepauley.luigi.utilities;

import java.awt.Color;
import java.awt.Graphics;
import java.io.*;

import dev.lepauley.luigi.general.Game;

/*
 * Contains helper functions that assist us anywhere in game
 */

public class Utilities {

	//Combines various variables and writes to a file
	public static void writeSettingsFile() {

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("res/files/settings.txt"));
			
			//Add Highscore:
			bw = appendBufferedWriter(bw, "HighScore", Game.gameHeader.getHighScore());
			
			//Add Sound Settings
			bw = appendBufferedWriter(bw, "CurrentVolume(SFX)", Game.gameAudio.getCurrentVolume("SFX"));
			bw = appendBufferedWriter(bw, "CurrentVolume(MUSIC)", Game.gameAudio.getCurrentVolume("MUSIC"));
			bw = appendBufferedWriter(bw, "CurrentRate", Game.gameAudio.getCurrentRate());
			bw = appendBufferedWriter(bw, "CurrentPitch", Game.gameAudio.getCurrentPitch());

			//Close BufferedWriter
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static BufferedWriter appendBufferedWriter(BufferedWriter bw, String field, float valueF) throws IOException {

		//Converts Float > string
		String valueS = Float.toString(valueF);

		//Writes field and value and a new Line to separate/help with readability
		bw.write(field + " : " + valueS + " \n"); 
		
		return bw;
	}
	
	//Parses input file and creates and returns a long string
	public static String loadFileAsString(String path) {
		
		//StringBuilder allows us to add characters to a string easily
		StringBuilder builder = new StringBuilder();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			
			//The current line we're working with
			String line;
			
			//While we haven't reached the end of the file
			while((line = br.readLine()) != null)

				//Append new line to the line
				builder.append(line + "\n");

			br.close();
		}catch(IOException e) {
			e.printStackTrace();
		}

		//Converts everything we just appended into a string and returns it
		return builder.toString();
	}
	
	//Takes in a string and will convert each number to the integer value
	public static int parseInt(String number) {
		try {
			return Integer.parseInt(number);

		//If you try to supply a value that isn't a number, it will print contents to stack trace
		}catch(NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//Takes in a string and will convert each number to the float value
	public static float parseFloat(String number) {
		try {
			float f = Float.parseFloat(number);
			return f;

		//If you try to supply a value that isn't a number, it will print contents to stack trace
		}catch(NumberFormatException e) {
			e.printStackTrace();
			return 0f;
		}
	}
	
	//Takes a String and creates a shadow string following by a top layer String using the default text color
	public static void drawShadowString(Graphics g, String s, int x, int y, int shadowOffset) {

		//Sets color to black for the shadow
		g.setColor(Color.black);

		//Loop to extend shadow (based on font size from input)
		for(int i = 1; i <= shadowOffset; i++)
			g.drawString(s, x + i, y + i);

		//Sets color to default color and draws string
		g.setColor(ColorManager.mapColors.get(EnumColor.DefaultColor));
		g.drawString(s, x, y);
	}

	//Allows user to specify the foreground color when creating shadow string (otherwise it's default text color)
	public static void drawShadowString(Graphics g, Color color, String s, int x, int y, int shadowOffset) {

		//Sets color to black for the shadow
		g.setColor(Color.black);

		//Loop to extend shadow (based on font size from input)
		for(int i = 1; i <= shadowOffset; i++)
			g.drawString(s, x + i, y + i);

		//Sets color to default color and draws string
		g.setColor(color);
		g.drawString(s, x, y);
	}
	
	// Print with a newline without typing System.out. prefix:
	public static void print(Object obj) {
		System.out.println(obj);
	}
	
	// Print a newline by itself without typing System.out. prefix:
	public static void print() {
		System.out.println();
	}
	
	// Print with no line break without typing System.out. prefix:
	public static void printnb(Object obj) {
		System.out.print(obj);
	}
	
}
