package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class GreenMountain001DotsLeft extends Tile {

	public GreenMountain001DotsLeft(int id) {
		super(Assets.greenMountain001DotsLeft, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
