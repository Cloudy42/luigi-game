package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Brick001CastleRampartTransparent extends Tile {

	public Brick001CastleRampartTransparent(int id) {
		super(Assets.brick001CastleRampartTransparent, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
