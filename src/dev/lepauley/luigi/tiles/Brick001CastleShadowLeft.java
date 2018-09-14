package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Brick001CastleShadowLeft extends Tile {

	public Brick001CastleShadowLeft(int id) {
		super(Assets.brick001CastleShadowLeft, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
