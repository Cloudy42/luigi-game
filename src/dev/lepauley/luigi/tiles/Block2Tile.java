package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Block2Tile extends Tile {

	public Block2Tile(int id) {
		super(Assets.block2, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
