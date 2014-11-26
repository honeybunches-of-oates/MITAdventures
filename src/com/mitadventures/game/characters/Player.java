/*
 * Player Class
 * Creates a player that uses the Controller class to move around the map
 */

package com.mitadventures.game.characters;

import com.mitadventures.game.Controller;
import com.mitadventures.game.Game;
import com.mitadventures.game.graphics.PlayerSpriteSet;
import com.mitadventures.game.graphics.SpriteSet;
import com.mitadventures.game.level.Level;
import com.mitadventures.game.level.tiles.Tile;

public class Player extends Actor {

	// Player Reference Variables //
	public String name;
	private String type;
	private Controller controller;
	public boolean isWalking = false;
	public int numSteps = 0;
	public long lastprocessed;
	public Game game;
	private int stride = 0;
	public int health;
	public int yButtonItem = 0;
	public int xButtonItem = 0;
	public boolean yButtonItemInUse = false;
	public boolean xButtonItemInUse = false;
	public boolean isSwimming = false;
	public boolean isClimbing = false;
	public boolean isJumping = false;
	public boolean isPuddling = false;
	public boolean wolfState = false;
	public boolean midnaState = false;
	public boolean isFighting = false;
	public boolean isWhistling = false;
	public boolean isAttacking = false;
	public SpriteSet swimming;
	public SpriteSet puddling;
	public SpriteSet climbing;
	public SpriteSet fighting;
	public SpriteSet jumping;
	public SpriteSet jumpingSwordAttack;
	public SpriteSet somersault;
	public SpriteSet finishAttack;
	public SpriteSet swordAttack;
	public SpriteSet shieldDefense;
	public SpriteSet holdingitem;
	public SpriteSet death;
	public SpriteSet deathByLava;
	public SpriteSet deathByPit;
	public SpriteSet injury;
	public SpriteSet wolfDeathWithoutMidna;
	public SpriteSet wolfDeathWithMidna;
	public SpriteSet wolfDeathByLavaWithoutMidna;
	public SpriteSet wolfDeathByLavaWithMidna;
	public SpriteSet wolfInjuryWithoutMidna;
	public SpriteSet wolfInjuryWithMidna;
	public SpriteSet wolfLongJumpWithMidna;
	public SpriteSet wolfWithoutMidna;
	public SpriteSet wolfWithMidna;
	public SpriteSet wolfAttackWithoutMidna;
	public SpriteSet wolfAttackWithMidna;
	public SpriteSet wolfJumpAttackWithoutMidna;
	public SpriteSet wolfJumpAttackWithMidna;
	public SpriteSet wolfSpinAttack;
	public SpriteSet wolfCircle;
	public SpriteSet wolfHowlWithoutMidna;
	public SpriteSet wolfHowlWithMidna;
	public SpriteSet wolfDigWithoutMidna;
	public SpriteSet wolfDigWithMidna;
	public SpriteSet wolfSwimmingWithMidna;
	public SpriteSet wolfSwimmingWithoutMidna;
	public SpriteSet firingBow;
	public SpriteSet firingSlingShot;
	public SpriteSet whistling;
	public SpriteSet throwingBoomerang;
	public SpriteSet spinner;
	public SpriteSet spinnerAttack;
	public SpriteSet clawshot;
	public SpriteSet doubleclawshot;
	public SpriteSet ironBoots;
	public SpriteSet flippingWithIronBoots;
	public SpriteSet sidewaysWithIronBoots;
	public SpriteSet upsideDownWithIronBoots;
	public SpriteSet fishing;
	public SpriteSet ballAndChain;
	public SpriteSet swingingBallAndChain;
	public SpriteSet flyingBird;
	public SpriteSet twilightBird;
	////////////////////////////////

	// Player Constructor //
	public Player(Game game, Level level, int x, int y, Controller controller, String type) {
		super(x, y, level, type);
		this.type = type;
		this.controller = controller;
		this.game = game;
		loadSpriteSets();
		spriteSets.add(swimming);
		/**
		spriteSets.add(puddling);
		spriteSets.add(climbing);
		spriteSets.add(fighting);
		spriteSets.add(jumping);
		spriteSets.add(jumpingSwordAttack);
		spriteSets.add(somersault);
		spriteSets.add(finishAttack);
		spriteSets.add(swordAttack);
		spriteSets.add(shieldDefense);
		spriteSets.add(holdingitem);
		spriteSets.add(death);
		spriteSets.add(deathByLava);
		spriteSets.add(deathByPit);
		spriteSets.add(injury);
		spriteSets.add(firingBow);
		spriteSets.add(firingSlingShot);
		spriteSets.add(whistling);
		spriteSets.add(throwingBoomerang);
		spriteSets.add(spinner);
		spriteSets.add(spinnerAttack);
		spriteSets.add(clawshot);
		spriteSets.add(doubleclawshot);
		spriteSets.add(ironBoots);
		**/
	}
	////////////////////////
	
	public void loadSpriteSets() {
		swimming = loadPlayerSpriteSet("swim");
	}
	
	public PlayerSpriteSet loadPlayerSpriteSet(String activity) {
		return new PlayerSpriteSet(type, activity);
	}
	
	public int getStage() {
		return 1;
	}

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
			if (Tile.getSolidity(game.layer1.getTile(xTilePos, yTilePos - 1), game.solidTilesList)
					|| Tile.upBoundary(tileid)
					|| (e.getCurrentXTile() == xTilePos 
					& e.getCurrentYTile() == yTilePos - 1))
				super.canMoveUp = false;
			if (Tile.getSolidity(game.layer1.getTile(xTilePos - 1, yTilePos), game.solidTilesList)
					|| Tile.leftBoundary(tileid)
					|| (e.getCurrentXTile() == xTilePos - 1 
					& e.getCurrentYTile() == yTilePos))
				super.canMoveLeft = false;
			if (Tile.getSolidity(game.layer1.getTile(xTilePos, yTilePos + 1), game.solidTilesList)
					|| Tile.downBoundary(tileid)
					|| (e.getCurrentXTile() == xTilePos 
					& e.getCurrentYTile() == yTilePos + 1))
				super.canMoveDown = false;
			if (Tile.getSolidity(game.layer1.getTile(xTilePos + 1, yTilePos), game.solidTilesList)
					|| Tile.rightBoundary(tileid)
					|| (e.getCurrentXTile() == xTilePos + 1
					& e.getCurrentYTile() == yTilePos))
				super.canMoveRight = false;
		}
		
		if (Tile.getSolidity(game.layer2.getTile(xTilePos, yTilePos - 1), game.solidTilesList)
				|| Tile.upBoundary(tileid))
			super.canMoveUp = false;
		if (Tile.getSolidity(game.layer2.getTile(xTilePos - 1, yTilePos), game.solidTilesList)
				|| Tile.leftBoundary(tileid))
			super.canMoveLeft = false;
		if (Tile.getSolidity(game.layer2.getTile(xTilePos, yTilePos + 1), game.solidTilesList)
				|| Tile.downBoundary(tileid))
			super.canMoveDown = false;
		if (Tile.getSolidity(game.layer2.getTile(xTilePos + 1, yTilePos), game.solidTilesList)
				|| Tile.rightBoundary(tileid))
			super.canMoveRight = false;

	}
	///////////////////////
}
