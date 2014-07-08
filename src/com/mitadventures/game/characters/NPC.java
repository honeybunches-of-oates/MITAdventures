package com.mitadventures.game.characters;

import com.mitadventures.game.Game;
import com.mitadventures.game.graphics.Screen;
import com.mitadventures.game.level.tiles.Tile;
import com.mitadventures.game.level.Level;

public class NPC extends Actor {

	// Reference Variables //
	public boolean isWalking = false;
	public int numSteps = 0;
	public long lastprocessed;
	public Game game;
	private int stride = 0;
	/////////////////////////

	// NPC Constructor //
	public NPC(Game game, Level level, int xPos, int yPos, String msg, String type) {
		super(xPos * 16, yPos * 16 - 8, level, type);
		this.game = game;
		super.setMessage(msg);
	}
	/////////////////////

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
				sprite = 3;
		// stage two of animation
		else
			sprite = 2;
		spriteStage = sprite;
	}
	///////////////////////////

	// Tick Method //
	public void tick() {
		if (canMove) {
			if (numSteps == 0)
				crashTest();
			if (isWalking == false) {
				int xa = 0;
				int ya = 0;

				int rand = (int) (Math.random() * 300);
				if (rand <= 1 & canMoveUp) {
					if (rand == 1)
						isWalking = true;
					ya--;
				} else if (rand <= 3 & canMoveDown) {
					if (rand == 3)
						isWalking = true;
					ya++;
				} else if (rand <= 5 & canMoveLeft) {
					if (rand == 5)
						isWalking = true;
					xa--;
				} else if (rand <= 7 & canMoveRight) {
					if (rand == 7)
						isWalking = true;
					xa++;
				}

				faceDirection(xa, ya);

			} else if (isWalking) {
				if (numSteps < 16) {
					numSteps++;
					setSprite(numSteps);
					switch (direction) {
					case 1: yPos--;
						break;
					case 2: xPos--;
						break;
					case 3: yPos++;
						break;
					case 4: xPos++;
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
		canMoveDown = true;
		canMoveUp = true;
		canMoveLeft = true;
		canMoveRight = true;

		int xTilePos = getCurrentXTile();
		int yTilePos = getCurrentYTile();
		int tileid = game.layer1.getTile(xTilePos, yTilePos);

		if (Tile.getSolidity(game.layer1.getTile(xTilePos, yTilePos - 1), game)
				|| Tile.upBoundary(tileid)
				|| (game.player.getCurrentXTile() == xTilePos & game.player
						.getCurrentYTile() == yTilePos - 1))
			canMoveUp = false;
		if (Tile.getSolidity(game.layer1.getTile(xTilePos - 1, yTilePos), game)
				|| Tile.leftBoundary(tileid)
				|| (game.player.getCurrentXTile() == xTilePos - 1 & game.player
						.getCurrentYTile() == yTilePos))
			canMoveLeft = false;
		if (Tile.getSolidity(game.layer1.getTile(xTilePos, yTilePos + 1), game)
				|| Tile.downBoundary(tileid)
				|| (game.player.getCurrentXTile() == xTilePos & game.player
						.getCurrentYTile() == yTilePos + 1))
			canMoveDown = false;
		if (Tile.getSolidity(game.layer1.getTile(xTilePos + 1, yTilePos), game)
				|| Tile.rightBoundary(tileid)
				|| (game.player.getCurrentXTile() == xTilePos + 1 & game.player
						.getCurrentYTile() == yTilePos))
			canMoveRight = false;
		if (Tile.getSolidity(game.layer2.getTile(xTilePos, yTilePos - 1), game)
				|| Tile.upBoundary(tileid))
			canMoveUp = false;
		if (Tile.getSolidity(game.layer2.getTile(xTilePos - 1, yTilePos), game)
				|| Tile.leftBoundary(tileid))
			canMoveLeft = false;
		if (Tile.getSolidity(game.layer2.getTile(xTilePos, yTilePos + 1), game)
				|| Tile.downBoundary(tileid))
			canMoveDown = false;
		if (Tile.getSolidity(game.layer2.getTile(xTilePos + 1, yTilePos), game)
				|| Tile.rightBoundary(tileid))
			canMoveRight = false;
	}
	///////////////////////
}
