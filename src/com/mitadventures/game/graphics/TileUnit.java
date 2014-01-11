package com.mitadventures.game.graphics;

import java.awt.image.BufferedImage;

public class TileUnit {

	public int xPos;
	public int yPos;
	public BufferedImage image;
	
	public TileUnit(int xPos, int yPos, BufferedImage image) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.image = image;
	}
}
