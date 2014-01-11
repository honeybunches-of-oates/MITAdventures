package com.mitadventures.game.graphics;

public class Fonts {

	public static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ      "
			+ "0123456789.,:;'\"!?$#@           ";
	private int index = 0;

	// @ = MALE CHAR
	// # = FEMALE CHAR

	public void render(String msg, Screen screen, int x, int y) {
		if (msg == null)
				return;
		msg = msg.toUpperCase();
		
		String line = msg;
		
		for (int i = 0; i < msg.length() / 17 + 1; i++) {
			if (msg.length() >= 17)
				line = getLine(msg);
			for (int i1 = line.length() - 1; i1 >= 0; i1--) {
				int charIndex1 = chars.indexOf(line.charAt(i1));
				if (charIndex1 >= 0)
					screen.renderText(x + (i1 * 8) + 1, y + 1 + i * 10,
							charIndex1 + 30 * 32 + 1, ColorPalette.get(333, -1, -1, -1));
			}
			for (int i2 = line.length() - 1; i2 >= 0; i2--) {
				int charIndex2 = chars.indexOf(line.charAt(i2));
				if (charIndex2 >= 0)
					screen.renderText(x + (i2 * 8), y + i * 10, charIndex2 + 30 * 32 + 1,
							ColorPalette.get(0, -1, -1, -1));
			}
		}
		resetIndex();
	}
	
	public String getLine(String msg) {
		if (msg.charAt(17) == ' ') {
			System.out.println("17");
			String piece = msg.substring(index, index + 17);
			this.index += 17;
			return piece;
		} else 
			for (int i = 17; i > 0; i--) {
				if (msg.charAt(i - 1) == ' ') {
					String piece = "";
					if (index + i - 1 <= msg.length())
						piece = msg.substring(index, index + i - 1);
					if (index + i - 1 > msg.length())
						piece = msg.substring(index, msg.length());
					this.index += i;
					return piece;
				}
			}
		return msg;
	}
	public void setIndex(int index) {
		this.index = index; 
	}
	
	public void resetIndex() {
		this.index = 0;
	}

}