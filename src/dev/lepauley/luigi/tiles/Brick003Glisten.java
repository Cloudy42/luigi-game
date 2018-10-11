package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Brick003Glisten extends Tile {

	public Brick003Glisten(int id) {
		super(Assets.brick003Glisten, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
