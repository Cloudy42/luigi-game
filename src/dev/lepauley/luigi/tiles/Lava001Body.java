package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Lava001Body extends Tile {

	public Lava001Body(int id) {
		super(Assets.lava001Body, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
