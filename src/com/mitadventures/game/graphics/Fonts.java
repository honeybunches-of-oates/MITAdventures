package com.mitadventures.game.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

public class Fonts {

	public static String chars = "!\"$&,-.0123456789:;=?'    " + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
			+ "abcdefghijklmnopqrstuvwxyz";
	private int index = 0;
	private int linesize = 22;

	public void render(String msg, Screen screen, int x, int y, Graphics g, List<BufferedImage> font) {
		if (msg == null)
				return;
		int messageLength = msg.length();
		int i = 0;
		
		do {
			String line = getLine(msg);
			for (int chari = 0; chari < line.length(); chari++) {
				char character = line.charAt(chari);
				for (int ind = 0; ind < chars.length(); ind++) {
					if (character == chars.charAt(ind)) {
						g.drawImage(font.get(ind), (x + chari * 8 + 16) * 3, (y + i * 16) * 3, 8 * 3, 16 * 3, null);
						break;
					}
				}
			}
			i++;
			msg = msg.substring(line.length());
		}
		while (index < messageLength - 1);
		resetIndex();
	}
	
	public String getLine(String msg) {
		if (msg.length() > linesize) {
			if (msg.charAt(linesize - 1) == ' ') {
				String line = msg.substring(0, linesize - 1);
				this.index += linesize;
				return line;
			} else {
				for (int i = linesize - 2; i > 0; i--) {
					if (msg.charAt(i) == ' ') {
						String line = msg.substring(0, i);
						this.index += i + 1;
						return line;
					}
				}
			}
		} else {
			index += msg.length();
			return msg;
		}
		return "";
	}
		
	public void setIndex(int index) {
		this.index = index; 
	}
	
	public void resetIndex() {
		this.index = 0;
	}

}