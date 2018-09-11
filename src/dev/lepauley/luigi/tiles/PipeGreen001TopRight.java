package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class PipeGreen001TopRight extends Tile {

	public PipeGreen001TopRight(int id) {
		super(Assets.pipeGreen001TopRight, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
