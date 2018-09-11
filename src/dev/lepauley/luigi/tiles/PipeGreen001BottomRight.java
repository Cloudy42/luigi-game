package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class PipeGreen001BottomRight extends Tile {

	public PipeGreen001BottomRight(int id) {
		super(Assets.pipeGreen001BottomRight, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
