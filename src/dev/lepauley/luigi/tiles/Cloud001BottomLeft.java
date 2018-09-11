package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Cloud001BottomLeft extends Tile {

	public Cloud001BottomLeft(int id) {
		super(Assets.cloud001BottomLeft, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
