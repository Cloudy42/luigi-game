package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class GreenYellowBush001Middle extends Tile {

	public GreenYellowBush001Middle(int id) {
		super(Assets.greenYellowBush001Middle, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
