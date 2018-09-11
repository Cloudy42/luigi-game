package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Cloud001TopLeft extends Tile {

	public Cloud001TopLeft(int id) {
		super(Assets.cloud001TopLeft, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
