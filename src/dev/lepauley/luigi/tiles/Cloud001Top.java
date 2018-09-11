package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Cloud001Top extends Tile {

	public Cloud001Top(int id) {
		super(Assets.cloud001Top, id);
	}

	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
