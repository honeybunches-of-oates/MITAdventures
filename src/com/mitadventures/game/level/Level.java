package com.mitadventures.game.level;

import java.util.ArrayList;
import java.util.List;

import com.mitadventures.game.characters.Entity;
import com.mitadventures.game.graphics.MessageBox;
import com.mitadventures.game.graphics.Screen;

public class Level {
	
	// Reference Variables //
	public String name;
	public Map layer1;
	public Map layer2;
	public int width;
	public int height;
	public List<Entity> entities = new ArrayList<Entity>();
	public List<MessageBox> messageBoxes = new ArrayList<MessageBox>();
	/////////////////////////
	
	// Level Constructor //
	public Level(String name, Map layer1, Map layer2) {
		this.name = name;
		this.layer1 = layer1;
		this.layer2 = layer2;
		this.width = layer1.getWidth();
		this.height = layer1.getHeight();
	}
	///////////////////////
	
	// Tick Method //
	public void tick() {
		for (Entity e : entities) {
			e.tick();
		}
	}
	
	// Render Transition Method //
	public void renderTransition(Screen screen) {
		
	}
	
	// Add Entity Method //
	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}
	///////////////////////
	
	// Add Message Box Method //
	public void addMessageBox(MessageBox messageBox) {
		this.messageBoxes.add(messageBox);
	}
	///////////////////////
}
