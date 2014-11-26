package com.mitadventures.game.graphics;

import java.awt.Image;

public abstract class SpriteSet {
	
	protected String name;
	public Image[][] sprites;

	public SpriteSet(String name, int directions, int stages) {
		this.name = name;
		sprites = new Image[directions][stages];
	}
	
	public abstract void loadSpriteSet();
}
