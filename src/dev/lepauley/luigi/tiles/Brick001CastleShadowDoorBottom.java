package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Brick001CastleShadowDoorBottom extends Tile {

	public Brick001CastleShadowDoorBottom(int id) {
		super(Assets.brick001CastleShadowDoorBottom, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
