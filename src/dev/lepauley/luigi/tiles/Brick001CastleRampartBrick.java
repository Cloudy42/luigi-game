package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Brick001CastleRampartBrick extends Tile {

	public Brick001CastleRampartBrick(int id) {
		super(Assets.brick001CastleRampartBrick, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
