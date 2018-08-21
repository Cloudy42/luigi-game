package dev.lepauley.luigi.display;

import javax.swing.JFrame;

/*
 * Display window for game
 */

public class Display {
	
	//How we display window
	private JFrame frame;
	
	//Window title
	private String title;
	
	//Width and height of window, in pixels
	private int width, height;
	
	//Constructor
	public Display(String title, int width, int height) {
		//Initialize class variables
		this.title = title;
		this.width = width;
		this.height = height;
		createDisplay();
	}
	
	private void createDisplay() {
		//Set window attributes
		frame = new JFrame(title);
		frame.setSize(width, height);
		
		//Close window AND close down game/program
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Not allowing resizing
		frame.setResizable(false);

		//When window first pops, do so at center
		frame.setLocationRelativeTo(null);
		
		//By default not visible, so making it visible
		frame.setVisible(true);
	}
	
}
