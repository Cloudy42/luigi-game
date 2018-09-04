package dev.lepauley.luigi.states;

import java.awt.Color;
import java.awt.Graphics;

import dev.lepauley.luigi.entities.creatures.Player;
import dev.lepauley.luigi.gfx.Assets;

/*
 * Where actual game play is at
 */
public class GameState extends State {

	private Player player;
	
	public GameState() {
		player = new Player(100,100);
	}
	
	@Override
	public void tick() {
		player.tick();
	}

	@Override
	public void render(Graphics g) {
		//Test images (Fake pipe):
		g.setColor(Color.GREEN);
		g.fillRect(80, 200, 140, 30);
		g.fillRect(90, 220, 120, 150);
		g.setColor(Color.BLACK);
		g.fillRect(90, 230, 120, 2);
		g.fillRect(110, 230, 15, 140);
		g.fillRect(150, 230, 20, 140);
		g.fillRect(190, 230, 10, 140);
		
		player.render(g);
	}

}
