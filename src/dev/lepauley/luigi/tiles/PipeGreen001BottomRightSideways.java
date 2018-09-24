package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class PipeGreen001BottomRightSideways extends Tile {

	public PipeGreen001BottomRightSideways(int id) {
		super(Assets.pipeGreen001BottomRightSideways, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
