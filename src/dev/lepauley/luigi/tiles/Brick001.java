package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Brick001 extends Tile {

	public Brick001(int id) {
		super(Assets.brick001, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
