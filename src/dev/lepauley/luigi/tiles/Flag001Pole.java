package dev.lepauley.luigi.tiles;

import dev.lepauley.luigi.gfx.Assets;

public class Flag001Pole extends Tile {

	public Flag001Pole(int id) {
		super(Assets.flag001Pole, id);
		setIsSolid(false);
	}
}