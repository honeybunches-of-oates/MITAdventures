package com.mitadventures.game.graphics;

import java.io.IOException;

import javax.imageio.ImageIO;

public class PlayerSpriteSet extends SpriteSet{
	
	public String activity;

	public PlayerSpriteSet(String name, String activity) {
		super(name, 4, 3);
		if(activity == "" || activity == null)
			this.activity = "";
		else
			this.activity = "_" + activity;
		loadSpriteSet();
	}
	
	public void loadSpriteSet() {
		if(activity == null)  {
			this.activity = "";
		}
		try {
			sprites[0][0] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Up" + activity + ".png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[0][1] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Up1" + activity + ".png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[0][2] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Up2" + activity + ".png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[1][0] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Left" + activity + ".png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[1][1] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Left1" + activity + ".png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[1][2] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Left2" + activity + ".png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[2][0] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Down" + activity + ".png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[2][1] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Down1" + activity + ".png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[2][2] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Down2" + activity + ".png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[3][0] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Right" + activity + ".png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[3][1] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Right1" + activity + ".png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			sprites[3][2] = ImageIO.read(PlayerSpriteSet.class.getResourceAsStream("/Sprites/" + name + "_Right2" + activity + ".png"));
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
