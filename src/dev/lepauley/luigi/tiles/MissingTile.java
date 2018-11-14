package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class MissingTile extends Tile {

	public MissingTile(int id) {
		super(Assets.missing, id);
		setIsSolid(false);
	}
	
}
