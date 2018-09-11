package dev.lepauley.luigi.states;

import java.awt.Color;
import java.awt.Graphics;

import dev.lepauley.luigi.Game;
import dev.lepauley.luigi.entities.creatures.Creature;
import dev.lepauley.luigi.entities.creatures.Player;
import dev.lepauley.luigi.levels.Level;

/*
 * Where actual game play is at
 */
public class GameState extends State {

	private Player player;
	private Level level;
	
	public GameState(Game game) {
		super(game);
		player = new Player(game,Creature.DEFAULT_CREATURE_WIDTH,Creature.DEFAULT_CREATURE_HEIGHT);
		level = new Level("res/levels/1-1.txt");
	}
	
	//Must tick level before player do to proper layer positioning
	@Override
	public void tick() {
		level.tick();
		player.tick();
	}

	//Must render level before player do to proper layer positioning
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
		
		level.render(g);
		player.render(g);
	}

}
