package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class BG003PaddedWall extends Tile {

	public BG003PaddedWall(int id) {
		super(Assets.bg003PaddedWall, id);
		setIsSolid(true);
	}
	
}
