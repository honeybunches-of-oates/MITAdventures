package com.mitadventures.game.graphics;

import java.awt.Image;

public abstract class SpriteSet {
	
	protected String name;
	public Image[][] sprites;

	public SpriteSet(String name) {
		this.name = name;
		loadSpriteSet();
	}
	
	public abstract void loadSpriteSet();
}
