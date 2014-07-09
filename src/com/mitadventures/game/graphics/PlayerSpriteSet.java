package com.mitadventures.game.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PlayerSpriteSet extends SpriteSet{

	public PlayerSpriteSet(String name) {
		super(name, 4, 3);
	}

	public void loadSpriteSet() {
		try {
			sprites[0][0] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Up.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[0][1] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Up1.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[0][2] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Up2.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[1][0] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Left.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[1][1] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Left1.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[1][2] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Left2.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[2][0] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Down.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[2][1] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Down1.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[2][2] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Down2.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[3][0] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Right.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[3][1] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Right1.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[3][2] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Right2.png"));
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
