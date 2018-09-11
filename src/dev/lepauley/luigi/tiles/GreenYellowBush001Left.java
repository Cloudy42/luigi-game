package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class GreenYellowBush001Left extends Tile {

	public GreenYellowBush001Left(int id) {
		super(Assets.greenYellowBush001Left, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
