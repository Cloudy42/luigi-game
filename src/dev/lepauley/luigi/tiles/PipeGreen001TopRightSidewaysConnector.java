package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class PipeGreen001TopRightSidewaysConnector extends Tile {

	public PipeGreen001TopRightSidewaysConnector(int id) {
		super(Assets.pipeGreen001TopRightSidewaysConnector, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
