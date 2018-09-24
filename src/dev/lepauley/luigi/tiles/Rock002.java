package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Rock002 extends Tile {

	public Rock002(int id) {
		super(Assets.rock002, id);
	}

	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
