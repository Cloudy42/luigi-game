package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class AxeLever003 extends Tile {

	public AxeLever003(int id) {
		super(Assets.axeLever003, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
