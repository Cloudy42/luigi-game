package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Tree001LeavesMid extends Tile {

	public Tree001LeavesMid(int id) {
		super(Assets.tree001LeavesMid, id);
	}

	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
