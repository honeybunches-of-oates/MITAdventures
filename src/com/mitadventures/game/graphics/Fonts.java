package com.mitadventures.game.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

public class Fonts {

	public static String chars = "!\"$&,-.0123456789:;=?' " + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
			+ "abcdefghijklmnopqrstuvwxyz";
	private int index = 0;
	private int boxIndex = 1;
	private boolean messageComplete = false;

	public void render(String msg, Screen screen, int x, int y, int maxWidth, int maxHeight, Graphics g, List<BufferedImage> font) {
		if (msg == null)
				return;
		int messageLength = msg.length();
		int i = 0;
		int xPosIndex = 0;
		
		do {
			String segment = msg.substring(index);
			String line = getLine(segment, font, (maxWidth - 20) * 3);
			if (i < boxIndex * maxHeight & i >= (boxIndex - 1) * maxHeight) {			
				for (int chari = 0; chari < line.length(); chari++) {
					char character = line.charAt(chari);
					for (int ind = 0; ind < chars.length(); ind++) {
						if (character == chars.charAt(ind)) {
							int width = font.get(ind).getWidth();
							int height = 16;
	 						g.drawImage(font.get(ind), (xPosIndex + x) * 2, (y + (i % 4) * 10) * 3, width * 2, height * 2, null);
	 						xPosIndex += width;
	 						if (index >= messageLength - 1) {
	 							messageComplete = true;
	 						}
							break;         
						}
					}
				}
				xPosIndex = 0;
			}
			i++;
		}
		while (index < messageLength - 1);
		resetIndex();
	}
	
	public String getLine(String msg, List<BufferedImage> font, int maxWidth) {
		int linesize = 0;
		int i = 0;
		for (i = 0; i < msg.length(); i++) {
			if (linesize >= maxWidth) {
				for (int ind = i; ind > 0; ind--) {
					i = ind;
					char character = msg.charAt(i);
					if (character == ' ') {
						String line = msg.substring(0, i);
						index += i + 1;
						return line;
					}
				}
			}
			char character = msg.charAt(i);
			for (int ind = 0; ind < chars.length(); ind++) {
				if (character == chars.charAt(ind)) {
					linesize += font.get(ind).getWidth() * 2;
					break;
				}
			}
		}
		String line = msg.substring(0, i);
		index += i + 1;
		return line;
	}
	
	public void nextBoxIndex() {
		boxIndex++;
	}
	
	public void resetBoxIndex() {
		boxIndex = 1;
		messageComplete = false;
	}
		
	public void setIndex(int index) {
		this.index = index; 
	}
	
	public void resetIndex() {
		this.index = 0;
	}
	
	public boolean messageComplete() {
		return messageComplete;
	}

}