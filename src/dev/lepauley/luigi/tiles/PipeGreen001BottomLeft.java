package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class PipeGreen001BottomLeft extends Tile {

	public PipeGreen001BottomLeft(int id) {
		super(Assets.pipeGreen001BottomLeft, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
