package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Chain003 extends Tile {

	public Chain003(int id) {
		super(Assets.chain003, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
