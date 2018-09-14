package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Brick001CastleShadowDoorTop extends Tile {

	public Brick001CastleShadowDoorTop(int id) {
		super(Assets.brick001CastleShadowDoorTop, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
