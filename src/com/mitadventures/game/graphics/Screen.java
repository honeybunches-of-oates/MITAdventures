package com.mitadventures.game.graphics;


public class Screen {

	// Reference Variables //
	public int[] pixels;

	public int width;
	public int height;
	public int xPos = 0;
	public int yPos = 0;
	public int xOffset;
	public int yOffset;
	public int tile;

	public SpriteSheet sheet;
	/////////////////////////
	
	// Screen Constructor //
	public Screen(int width, int height, SpriteSheet sheet) {
		this.width = width;
		this.height = height;
		this.sheet = sheet;

		pixels = new int[width * height];
	}
	////////////////////////
	
	// Set Offset Method //
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	///////////////////////
}

