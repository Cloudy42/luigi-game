package dev.lepauley.luigi.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import dev.lepauley.luigi.Game;

/*
 * Pause screen
 */
public class PauseState extends State {

	//Defaut font
	Font myFont = new Font ("Lucida Sans Unicode", 1, 70);
	
	public PauseState(Game game) {
		super(game,"PauseState");
		this.game = game;
		
	}
	
	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		//Test images
		g.setColor(Color.red);
		g.fillRect(0, 0, 650, 450);
		
		g.setFont (myFont);
		g.setColor(Color.black);
		g.drawString("P A U S E", 150, 200);
		g.drawString("Y O ! ! !", 250, 300);

	}

}
