package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Cloud001Bottom extends Tile {

	public Cloud001Bottom(int id) {
		super(Assets.cloud001Bottom, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
