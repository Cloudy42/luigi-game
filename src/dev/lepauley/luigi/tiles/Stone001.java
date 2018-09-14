package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Stone001 extends Tile {

	public Stone001(int id) {
		super(Assets.stone001, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
