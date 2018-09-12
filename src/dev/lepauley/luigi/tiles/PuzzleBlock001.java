package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class PuzzleBlock001 extends Tile {

	public PuzzleBlock001(int id) {
		super(Assets.puzzleBlock001, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}