package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Stone002 extends Tile {

	public Stone002(int id) {
		super(Assets.stone002, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
