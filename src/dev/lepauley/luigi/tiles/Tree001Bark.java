package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Tree001Bark extends Tile {

	public Tree001Bark(int id) {
		super(Assets.tree001Bark, id);
	}

	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
