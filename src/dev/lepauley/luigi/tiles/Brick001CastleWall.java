package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Brick001CastleWall extends Tile {

	public Brick001CastleWall(int id) {
		super(Assets.brick001CastleWall, id);
		setIsSolid(false);
	}
}