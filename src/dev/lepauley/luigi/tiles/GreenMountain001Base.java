package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class GreenMountain001Base extends Tile {

	public GreenMountain001Base(int id) {
		super(Assets.greenMountain001Base, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
