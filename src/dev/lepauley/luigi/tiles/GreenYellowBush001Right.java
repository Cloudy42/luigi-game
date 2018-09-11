package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class GreenYellowBush001Right extends Tile {

	public GreenYellowBush001Right(int id) {
		super(Assets.greenYellowBush001Right, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
