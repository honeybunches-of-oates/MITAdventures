package com.mitadventures.game.characters;

import com.mitadventures.game.graphics.SpriteSet;
import com.mitadventures.game.level.Level;


public abstract class Entity {
	public static int x;
	public static int y;
	public int xPos;
	public int yPos;
	public int xPos1;
	public int yPos1;
	public int spriteStage = 0;
	public boolean canMoveDown = true;
	public boolean canMoveUp = true;
	public boolean canMoveLeft = true;
	public boolean canMoveRight = true;
	public boolean canMove = true;
	public int direction = 3;
	protected Level level;
	private String message = "no message";
	public SpriteSet spriteSet;

	public Entity(Level level, int x, int  y) {
		init(level);
	}

	public final void init(Level level) {
		this.level = level;
	}

    // Set Direction Method //
    public void setDirection(String dir) {
    	if (dir == "up")
    		direction = 1;
    	else if (dir == "left")
    		direction = 2;
    	else if (dir == "down")
    		direction = 3;
    	else if (dir == "right")
    		direction = 4;
    }
    //////////////////////////
	
	// Set Message Method //
	public void setMessage(String msg) {
		this.message = msg;
	}
	////////////////////////
	
	// Get Message Method //
	public String getMessage() {
		return message;
	}
	////////////////////////
	
	// Get Original X Coordinate Method //
	public int getOriginalX() {
		return xPos1;
	}
	// ////////////////////////////////////

	// Get Original Y Coordinate Method //
	public int getOriginalY() {
		return yPos1;
	}
	///////////////////////////////////////

	// Set Current X Coordinate Method //
	public void setCurrentX(int xPos) {
		this.xPos = xPos;
	}
	//////////////////////////////////////

	// Set Current Y Coordinate Method //
	public void setCurrentY(int yPos) {
		this.yPos = yPos;
	}

	// ///////////////////////////////////

	// Get Current X Coordinate Method //
	public int getCurrentX() {
		return xPos;
	}

	// ///////////////////////////////////

	// Get Current Y Coordinate Method //
	public int getCurrentY() {
		return yPos;
	}

	// ///////////////////////////////////

	// Get Original X Tile Method //
	public int getOriginalXTile() {
		return xPos1 * 16;
	}

	// //////////////////////////////

	// Get Original Y Tile Method //
	public int getOriginalYTile() {
		return yPos1 * 16;
	}

	// //////////////////////////////

	// Set Current X Tile Method //
	public void setCurrentXTile(int xTilePos) {
		this.xPos = xTilePos << 4;
	}

	// /////////////////////////////

	// Set Current Y Tile Method //
	public void setCurrentYTile(int yTilePos) {
		this.yPos = yTilePos * 16;
	}

	// /////////////////////////////

	// Get Current X Tile Method //
	public int getCurrentXTile() {
		return xPos >> 4;
	}

	// /////////////////////////////

	// Get Current Y Tile Method //
	public int getCurrentYTile() {
		return (yPos + 8) >> 4;
	}

	// /////////////////////////////
	
	public abstract void tick();
}
