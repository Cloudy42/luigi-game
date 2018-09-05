package dev.lepauley.luigi.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import dev.lepauley.luigi.Game;

/*
 * Menu/starting screen (before game begins)
 */
public class MenuState extends State {

	//Defaut font
	Font myFont = new Font ("Lucida Sans Unicode", 1, 70);
	
	public MenuState(Game game) {
		super(game);
		this.game = game;
		
	}
	
	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		//Test images
		g.setColor(Color.orange);
		g.fillRect(0, 0, 650, 450);
		
		g.setFont (myFont);
		g.setColor(Color.black);
		g.drawString("Start game?", 150, 200);
		
		myFont = new Font ("Lucida Sans Unicode", 1, 45);
		g.drawString("Yes", 250, 300);
		g.drawString("No", 255, 350);

	}

}
