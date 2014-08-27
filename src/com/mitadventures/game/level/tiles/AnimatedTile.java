package com.mitadventures.game.level.tiles;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;


public class AnimatedTile {

	private List<Image> tiles = new ArrayList<Image>();
	private int index;
	private int tileid;
	private int numstages;
	private int stage;
		// given in quarterseconds
	private int stageduration;
	
	public AnimatedTile() {
		stage = 0;
	}
	
	public void stageCheck(int quarterseconds) {
		if (quarterseconds % stageduration == 0) {
			stage++;
			if (stage >= numstages)
				stage = 0;
		}
	}
	
	public static boolean isAnimated(List<AnimatedTile> animationsheet, int index) {
		return false;
	}
	
	public Image getImage() {
		return tiles.get(stage);
	}
}
