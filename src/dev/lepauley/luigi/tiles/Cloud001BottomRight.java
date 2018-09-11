package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Cloud001BottomRight extends Tile {

	public Cloud001BottomRight(int id) {
		super(Assets.cloud001BottomRight, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
