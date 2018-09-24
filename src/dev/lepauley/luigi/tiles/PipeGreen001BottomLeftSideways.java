package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class PipeGreen001BottomLeftSideways extends Tile {

	public PipeGreen001BottomLeftSideways(int id) {
		super(Assets.pipeGreen001BottomLeftSideways, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
