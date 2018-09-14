package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Flag001Flag extends Tile {

	public Flag001Flag(int id) {
		super(Assets.flag001Flag, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
