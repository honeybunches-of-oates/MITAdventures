package com.mitadventures.game.level.tiles;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class AnimatedTile {

	private List<BufferedImage> tiles = new ArrayList<BufferedImage>();
	private int tileid;
	private int numstages;
	public int stage;
		// given in quarterseconds
	private int stageduration = 2;
	
	public AnimatedTile(List<BufferedImage> tiles, int numStages) {
		stage = 0;
		this.tiles = tiles;
		this.numstages = numStages;
	}
	
	public void setTileId(int id) {
		tileid = id;
	}
	
	public int getTileId() {
		return tileid;
	}
	
	public static boolean isAnimated(List<AnimatedTile> animationsheet, int index) {
		for (int i = 0; i < animationsheet.size(); i++) {
			if (animationsheet.get(i).getTileId() == index)
				return true;
		}
		return false;
	}
	
	public static int getIndex(List<AnimatedTile> animationsheet, int initindex) {
		for (int i = 0; i < animationsheet.size(); i++) {
			if (animationsheet.get(i).getTileId() == initindex) {
				return i;
			}
		}
		System.out.println(-1);
		return -1;
	}
	
	public Image getImage(int tilestage) {
		return tiles.get(tilestage);
	}
}
