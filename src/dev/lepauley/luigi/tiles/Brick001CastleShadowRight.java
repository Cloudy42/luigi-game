package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Brick001CastleShadowRight extends Tile {

	public Brick001CastleShadowRight(int id) {
		super(Assets.brick001CastleShadowRight, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
