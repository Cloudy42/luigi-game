package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Block1Tile extends Tile {

	public Block1Tile(int id) {
		super(Assets.block1, id);
	}

	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
