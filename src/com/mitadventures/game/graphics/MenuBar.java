package com.mitadventures.game.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import com.mitadventures.game.Game;

public class MenuBar {

	private String[] options;
	//numOptions cannot exceed ____
	private int numOptions;
	public int height;
	public int width;
	private static String chars = "!\"$&,-.0123456789:;=?' " + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
			+ "abcdefghijklmnopqrstuvwxyz";
	private List<BufferedImage> font;
	private int selected;
	
	public MenuBar(String[] options, List<BufferedImage> font) {
		this.options = options;
		this.numOptions = options.length;
		this.font = font;
		setDimensions();
		this.selected = 0;
	}
	
	public void setDimensions() {
		height = numOptions * 48 - 24;
		
		int maxWidth = 0;
		for (int j = 0; j < numOptions; j++) {
			int width = 0;
			for (int i = 0; i < options[j].length(); i++) {
				char character = options[j].charAt(i);
				for (int ind = 0; ind < chars.length(); ind++) {
					if (character == chars.charAt(ind)) {
						width += font.get(ind).getWidth() * 2;
						break;
					}
				}
			}
			maxWidth = Math.max(width, maxWidth);
		}
		this.width = maxWidth;
	}
	
	public void selectUp() {
		if (selected > 0)
			selected--;
	}
	
	public void selectDown() {
		if (selected < numOptions - 1)
			selected++;
	}
	
	public int getSelected() {
		return selected;
	}
	
	public String[] getOptions() {
		return options;
	}
}
