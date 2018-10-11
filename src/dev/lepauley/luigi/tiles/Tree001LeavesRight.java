package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Tree001LeavesRight extends Tile {

	public Tree001LeavesRight(int id) {
		super(Assets.tree001LeavesRight, id);
	}

	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
