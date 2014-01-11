/* Actor Class
 * Abstract Class, extended by Player and NPC
 */

package com.mitadventures.game.characters;

import com.mitadventures.game.level.Level;

public abstract class Actor extends Entity {
	
	// Reference Variables //
	protected Level level;
	/////////////////////////
	
	// Actor Constructor //
    public Actor(int x, int y, Level level) {
    	super(level, x, y);
    	this.xPos = x;
    	this.yPos = y;
    	this.xPos1 = x;
    	this.yPos1 = y;
    }
    ///////////////////////
    
    // Get Coordinate Methods //
    public int getX() {
    	return xPos;
    }
    
    public int getY() {
    	return yPos;
    }
    
    public int getOriginalX() {
    	return xPos1;
    }
    
    public int getOriginalY() {
    	return yPos1;
    }
    ////////////////////////////
    
    // Face Direction Method //
    public void faceDirection(int xa, int ya) {
    	if (xa > 0) {
    		setDirection("right");
    	} else if (xa < 0) {
    		setDirection("left");
    	} else if (ya > 0) {
    		setDirection("down");
    	} else if (ya < 0) {
    		setDirection("up");
    	}
    }
    ///////////////////////////
}