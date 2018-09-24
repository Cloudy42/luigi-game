package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class PipeGreen001TopLeftSideways extends Tile {

	public PipeGreen001TopLeftSideways(int id) {
		super(Assets.pipeGreen001TopLeftSideways, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
