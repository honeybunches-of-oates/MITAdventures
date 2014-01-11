package com.mitadventures.game.level.tiles;

import java.util.ArrayList;
import java.util.List;


public class AnimatedTile {

	private List<Integer> tiles = new ArrayList<Integer>();
	private int index = 0;
	
	public AnimatedTile(int initialTileId, int timeCycle) {
		tiles.add(initialTileId);
	}
	
	public void addTile(int tileId) {
		tiles.add(tileId);
	}
	
	public int nextTile() {
		int tile = tiles.get(index);
		index++;
		if (index == tiles.size())
			index = 0;
		return tile;
	}
}
