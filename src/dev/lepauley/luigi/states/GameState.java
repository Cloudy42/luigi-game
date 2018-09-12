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
		level = new Level("res/levels/1-1.txt");
		player = new Player(game, level.getSpawnX(), level.getSpawnY());
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
		level.render(g);
		player.render(g);
	}

}
