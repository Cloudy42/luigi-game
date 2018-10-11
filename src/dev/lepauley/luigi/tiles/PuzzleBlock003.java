package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class PuzzleBlock003 extends Tile {

	public PuzzleBlock003(int id) {
		super(Assets.puzzleBlock003, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
