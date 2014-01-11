package com.mitadventures.game.graphics;

public class MessageBox {

	private int xPos;
	private int yPos;
	private String message;
	
	public MessageBox(int xPos, int yPos, String message) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.message = message;
	}
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public String getMessage() {
		return message;
	}
}
