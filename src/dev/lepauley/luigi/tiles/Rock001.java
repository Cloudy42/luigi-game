package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Rock001 extends Tile {

	public Rock001(int id) {
		super(Assets.rock001, id);
	}

	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
