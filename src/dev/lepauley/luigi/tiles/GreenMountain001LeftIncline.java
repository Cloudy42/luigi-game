package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class GreenMountain001LeftIncline extends Tile {

	public GreenMountain001LeftIncline(int id) {
		super(Assets.greenMountain001LeftIncline, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
