package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Lava001Waves extends Tile {

	public Lava001Waves(int id) {
		super(Assets.lava001Waves, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
