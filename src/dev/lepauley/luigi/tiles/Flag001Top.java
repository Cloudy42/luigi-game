package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Flag001Top extends Tile {

	public Flag001Top(int id) {
		super(Assets.flag001Top, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
