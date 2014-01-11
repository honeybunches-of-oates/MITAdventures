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
	public boolean renderMessageBox = false;

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
	

	public TileUnit renderLayer(int xPos, int yPos, int tile) {
		xPos -= xOffset;
		yPos -= yOffset;
		
		this.tile = tile;
		
		if (tile <= 0)
			return new TileUnit(xPos, yPos, sheet.image.getSubimage(0, 0, 16, 16));
		
		int xTile = (tile - 1)  % (sheet.width / 16);
		int yTile = (tile - 1) / (sheet.width / 16);
		
		TileUnit tile1 = new TileUnit(xPos, yPos, sheet.image.getSubimage(xTile << 4, yTile << 4, 16, 16));
		
		return tile1;
	}
		
		// Render Message Box //
		public void renderMessageBox(boolean render) {
			renderMessageBox = render;	
		}
		////////////////////////
}

