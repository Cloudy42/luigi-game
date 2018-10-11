package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Brick003GlistenPartial extends Tile {

	public Brick003GlistenPartial(int id) {
		super(Assets.brick003GlistenPartial, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
