package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Bridge003 extends Tile {

	public Bridge003(int id) {
		super(Assets.bridge003, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
