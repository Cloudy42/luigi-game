package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class GreenMountain001DotsRight extends Tile {

	public GreenMountain001DotsRight(int id) {
		super(Assets.greenMountain001DotsRight, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
