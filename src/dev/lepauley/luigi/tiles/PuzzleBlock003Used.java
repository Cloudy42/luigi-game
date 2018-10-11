package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class PuzzleBlock003Used extends Tile {

	public PuzzleBlock003Used(int id) {
		super(Assets.puzzleBlock003Used, id);
	}
	
	@Override
	//Checks whether player can go through tile or not
	public boolean isSolid() {
		return true;
	}
	
}
