package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class PipeGreen001TopLeft extends Tile {

	public PipeGreen001TopLeft(int id) {
		super(Assets.pipeGreen001TopLeft, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}