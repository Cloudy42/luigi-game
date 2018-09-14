package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Brick001CastleWall extends Tile {

	public Brick001CastleWall(int id) {
		super(Assets.brick001CastleWall, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
