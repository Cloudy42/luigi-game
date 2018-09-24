package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class PipeGreen001BottomRightSidewaysConnector extends Tile {

	public PipeGreen001BottomRightSidewaysConnector(int id) {
		super(Assets.pipeGreen001BottomRightSidewaysConnector, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
