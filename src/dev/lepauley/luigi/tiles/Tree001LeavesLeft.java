package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Tree001LeavesLeft extends Tile {

	public Tree001LeavesLeft(int id) {
		super(Assets.tree001LeavesLeft, id);
	}

	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
