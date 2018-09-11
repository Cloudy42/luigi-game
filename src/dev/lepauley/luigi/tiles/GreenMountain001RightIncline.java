package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class GreenMountain001RightIncline extends Tile {

	public GreenMountain001RightIncline(int id) {
		super(Assets.greenMountain001RightIncline, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
