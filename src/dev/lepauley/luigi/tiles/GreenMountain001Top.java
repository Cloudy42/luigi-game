package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class GreenMountain001Top extends Tile {

	public GreenMountain001Top(int id) {
		super(Assets.greenMountain001Top, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
