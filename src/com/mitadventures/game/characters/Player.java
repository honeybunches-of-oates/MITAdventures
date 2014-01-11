/*
 * Player Class
 * Creates a player that uses the Controller class to move around the map
 */

package com.mitadventures.game.characters;

import com.mitadventures.game.Game;
import com.mitadventures.game.graphics.Screen;
import com.mitadventures.game.level.tiles.Tile;
import com.mitadventures.game.Controller;
import com.mitadventures.game.level.Level;

public class Player extends Actor {

	// Player Reference Variables //
	private Controller controller;
	public boolean isWalking = false;
	public int numSteps = 0;
	public int spriteStage = 0;
	public long lastprocessed;
	public Game game;
	private int stride = 0;

	////////////////////////////////

	// Player Constructor //
	public Player(Game game, Level level, int x, int y, Controller controller) {
		super(x, y, level);
		this.controller = controller;
		this.game = game;
	}

	////////////////////////

	// Setting Sprite Method //
	public void setSprite(int num) {
		int sprite;
		// standing still
		if (num == 0)
			sprite = 0;
		// stage one of animation
		else if (num <= 11)
			if (stride == 0)
				sprite = 1;
			else
				sprite = 2;
		// stage two of animation
		else
			sprite = 0;
		spriteStage = sprite;
	}
	///////////////////////////

	// Tick Method //
	public void tick() {
		if (super.canMove) {
			if (numSteps == 0)
				crashTest();
			if (isWalking == false) {
				int xa = 0;
				int ya = 0;

				if (controller.up.isPressed()) {
					if (System.currentTimeMillis()
							- controller.lastPressProcessed > 80) {
						ya--;
						isWalking = true;
					} else
						ya--;
				} else if (controller.down.isPressed()) {
					if (System.currentTimeMillis()
							- controller.lastPressProcessed > 80) {
						ya++;
						isWalking = true;
					} else
						ya++;
				} else if (controller.left.isPressed()) {
					if (System.currentTimeMillis()
							- controller.lastPressProcessed > 80) {
						xa--;
						isWalking = true;
					} else
						xa--;
				} else if (controller.right.isPressed()) {
					if (System.currentTimeMillis()
							- controller.lastPressProcessed > 80) {
						xa++;
						isWalking = true;
					} else
						xa++;
				}

				faceDirection(xa, ya);

			} else if (isWalking) {
				if (numSteps < 16) {
					numSteps++;
					setSprite(numSteps);
					switch (super.direction) {
					case 1:
						if (super.canMoveUp)
							yPos--;
						break;
					case 2:
						if (super.canMoveLeft)
							xPos--;
						break;
					case 3:
						if (super.canMoveDown)
							yPos++;
						break;
					case 4:
						if (super.canMoveRight)
							xPos++;
						break;
					}
				} else {
					isWalking = false;
					numSteps = 0;
					game.warpTests();
					game.grassTest();
					stride += 1 - (stride * 2);
				}
				setSprite(numSteps);
			}
		}
	}

	/////////////////

	// Crash Test Method //
	public void crashTest() {
		super.canMoveDown = true;
		super.canMoveUp = true;
		super.canMoveLeft = true;
		super.canMoveRight = true;

		int xTilePos = getCurrentXTile();
		int yTilePos = getCurrentYTile();
		int tileid = game.layer1.getTile(xTilePos, yTilePos);
		
		for (Entity e : game.level.entities) {
			if (Tile.getSolidity(game.layer1.getTile(xTilePos, yTilePos - 1), game)
					|| Tile.upBoundary(tileid)
					|| (e.getCurrentXTile() == xTilePos 
					& e.getCurrentYTile() == yTilePos - 1))
				super.canMoveUp = false;
			if (Tile.getSolidity(game.layer1.getTile(xTilePos - 1, yTilePos), game)
					|| Tile.leftBoundary(tileid)
					|| (e.getCurrentXTile() == xTilePos - 1 
					& e.getCurrentYTile() == yTilePos))
				super.canMoveLeft = false;
			if (Tile.getSolidity(game.layer1.getTile(xTilePos, yTilePos + 1), game)
					|| Tile.downBoundary(tileid)
					|| (e.getCurrentXTile() == xTilePos 
					& e.getCurrentYTile() == yTilePos + 1))
				super.canMoveDown = false;
			if (Tile.getSolidity(game.layer1.getTile(xTilePos + 1, yTilePos), game)
					|| Tile.rightBoundary(tileid)
					|| (e.getCurrentXTile() == xTilePos + 1
					& e.getCurrentYTile() == yTilePos))
				super.canMoveRight = false;
		}
		
		if (Tile.getSolidity(game.layer2.getTile(xTilePos, yTilePos - 1), game)
				|| Tile.upBoundary(tileid))
			super.canMoveUp = false;
		if (Tile.getSolidity(game.layer2.getTile(xTilePos - 1, yTilePos), game)
				|| Tile.leftBoundary(tileid))
			super.canMoveLeft = false;
		if (Tile.getSolidity(game.layer2.getTile(xTilePos, yTilePos + 1), game)
				|| Tile.downBoundary(tileid))
			super.canMoveDown = false;
		if (Tile.getSolidity(game.layer2.getTile(xTilePos + 1, yTilePos), game)
				|| Tile.rightBoundary(tileid))
			super.canMoveRight = false;

	}
	///////////////////////

	public void render(Screen screen) {
		
	}
}
