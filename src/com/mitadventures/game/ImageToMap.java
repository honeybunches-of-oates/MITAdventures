package com.mitadventures.game;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageToMap {

	private BufferedImage map;
	private BufferedImage map1;
	private BufferedImage spritesheet;
	private String mapPath;

	public ImageToMap(String mapPath, String spritePath) {
		try {
			this.map = ImageIO.read(ImageToMap.class.getResourceAsStream("/"
					+ mapPath + "_layer1.png"));
		} catch (IOException e) {
			System.out.println(e);
		}
		try {
			this.map1 = ImageIO.read(ImageToMap.class.getResourceAsStream("/"
					+ mapPath + "_layer2.png"));
		} catch (IOException e) {
			System.out.println(e);
		}
		try {
			this.spritesheet = ImageIO.read(ImageToMap.class
					.getResourceAsStream("/" + spritePath + ".png"));
		} catch (IOException e) {
			System.out.println(e);
		}
		this.mapPath = mapPath;
	}

	public int findTileNum(int tile_xPos, int tile_yPos, BufferedImage layer) {
		int mappixels[] = new int[16 * 16];
		int spritesheetpixels[] = new int[16 * 16];
		BufferedImage mapTile = layer.getSubimage(tile_xPos * 16,
				tile_yPos * 16, 16, 16);
		PixelGrabber mappixelgrabber = new PixelGrabber(mapTile, 0, 0, 16, 16,
				mappixels, 0, 16);
		try {
			mappixelgrabber.grabPixels();
		} catch (InterruptedException e) {
			System.out.println(e);
		}

		for (int y = 0; y < spritesheet.getHeight() / 16; y++) {
			for (int x = 0; x < spritesheet.getWidth() / 16; x++) {
				BufferedImage spritesheetTile = spritesheet.getSubimage(x * 16,
						y * 16, 16, 16);
				PixelGrabber spritesheetpixelgrabber = new PixelGrabber(
						spritesheetTile, 0, 0, 16, 16, spritesheetpixels, 0, 16);
				try {
					spritesheetpixelgrabber.grabPixels();
				} catch (InterruptedException e) {
					System.out.println(e);
				}
				boolean match = false;
				for (int i = 0; i < spritesheetpixels.length; i++) {
					if (spritesheetpixels[i] != mappixels[i])
						break;
					if (i == spritesheetpixels.length - 1)
						match = true;
				}
				if (match)
					return y * spritesheet.getWidth() / 16 + x;
			}
		}
		return -1;
	}

	public void makeMapText() {
		for (int y = 0; y < map.getHeight() / 16; y++) {
			for (int x = 0; x < map.getWidth() / 16; x++) {
				Integer tile = new Integer(findTileNum(x, y, map) + 1);
				int length = tile.toString().length();
				if (tile >= 0)
					System.out.print(tile + " ");
				else
					System.out.print((tile - 1) + " ");
				for (int i = 0; i < 3 - length; i++)
					System.out.print(" ");
			}
			Integer tile = new Integer(findTileNum(map.getWidth() / 16 - 1, y,
					map) + 1);
			int length = tile.toString().length();
			for (int i = 0; i < 3 - length; i++)
				System.out.print(" ");
			if (tile >= 0)
				System.out.print(tile);
			else
				System.out.print(tile - 1);
			System.out.print("/");
			// System.out.println();
		}
	}

	public void makeMap1Text() {
		for (int y = 0; y < map1.getHeight() / 16; y++) {
			for (int x = 0; x < map1.getWidth() / 16; x++) {
				Integer tile = new Integer(findTileNum(x, y, map1));
				int length = tile.toString().length();
				if (tile >= 0)
					System.out.print((tile + 1) + " ");
				else
					System.out.print(tile + " ");
				for (int i = 0; i < 3 - length; i++)
					System.out.print(" ");
			}
			Integer tile = new Integer(findTileNum(map1.getWidth() / 16 - 1, y,
					map1));
			int length = tile.toString().length();
			for (int i = 0; i < 3 - length; i++)
				System.out.print(" ");
			if (tile >= 0)
				System.out.print(tile + 1);
			else
				System.out.print(tile);
		System.out.print("/");
		// System.out.println();
		}
	}
	public void makeSpriteSheet() {
		int number = 0;
		List<int[]> spritesheet = new ArrayList<int[]>();
		List<BufferedImage> spritesheetimages = new ArrayList<BufferedImage>();
		int mapTilePixels1[] = new int[16 * 16];
		BufferedImage mapTile1 = map.getSubimage(0, 0, 16, 16);
		PixelGrabber mappixelgrabber1 = new PixelGrabber(mapTile1, 0, 0, 16,
				16, mapTilePixels1, 0, 16);
		try {
			mappixelgrabber1.grabPixels();
		} catch (InterruptedException e) {
			System.out.println(e);
		}
		spritesheet.add(mapTilePixels1);
		spritesheetimages.add(mapTile1);
		for (int y = 0; y < map.getHeight() / 16; y++) {
			for (int x = 0; x < map.getWidth() / 16; x++) {
				int mapTilePixels[] = new int[16 * 16];
				BufferedImage mapTile = map.getSubimage(x * 16, y * 16, 16, 16);
				PixelGrabber mappixelgrabber = new PixelGrabber(mapTile, 0, 0,
						16, 16, mapTilePixels, 0, 16);
				try {
					mappixelgrabber.grabPixels();
				} catch (InterruptedException e) {
					System.out.println(e);
				}
				boolean match = false;
				for (int i = 0; i < spritesheet.size(); i++) {
					int spritetile[] = spritesheet.get(i);
					for (int j = 0; j < spritetile.length; j++) {
						if (spritetile[j] != mapTilePixels[j])
							break;
						if (j == spritetile.length - 1)
							match = true;
					}
				}
				if (!match) {
					spritesheet.add(mapTilePixels);
					spritesheetimages.add(mapTile);
					System.out.println(x + ", " + y + " num: " + number);
					number++;
				}
			}
		}
		for (int y = 0; y < map1.getHeight() / 16; y++) {
			for (int x = 0; x < map1.getWidth() / 16; x++) {
				int mapTilePixels[] = new int[16 * 16];
				BufferedImage mapTile = map1
						.getSubimage(x * 16, y * 16, 16, 16);
				PixelGrabber mappixelgrabber = new PixelGrabber(mapTile, 0, 0,
						16, 16, mapTilePixels, 0, 16);
				try {
					mappixelgrabber.grabPixels();
				} catch (InterruptedException e) {
					System.out.println(e);
				}
				boolean match = false;
				for (int i = 0; i < spritesheet.size(); i++) {
					int spritetile[] = spritesheet.get(i);
					for (int j = 0; j < spritetile.length; j++) {
						if (spritetile[j] != mapTilePixels[j])
							break;
						if (j == spritetile.length - 1)
							match = true;
					}
				}
				if (!match) {
					spritesheet.add(mapTilePixels);
					spritesheetimages.add(mapTile);
					System.out.println(x + ", " + y + " num: " + number);
					number++;
				}
			}
		}

		int sidelength = (int) (Math.ceil(Math.sqrt(spritesheet.size())) * 16);
		BufferedImage finalImg = new BufferedImage(sidelength, sidelength,
				BufferedImage.TYPE_INT_ARGB);
		int num = 0;
		boolean stop = false;
		for (int i = 0; i < sidelength / 16; i++) {
			for (int j = 0; j < sidelength / 16; j++) {
				if (num > number)
					stop = true;
				if (!stop)
					finalImg.createGraphics().drawImage(
							spritesheetimages.get(num), j * 16, i * 16, null);
				num++;
			}
		}

		try {
			File output = new File("res/Spritesheet(" + mapPath + ").png");
			ImageIO.write(finalImg, "png", output);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) {
		ImageToMap imageToMap = new ImageToMap("Sacred_Grove_Temple",
				"Spritesheet(Sacred_Grove_Temple)");
		// imageToMap.makeSpriteSheet();
		 imageToMap.makeMapText();
		// imageToMap.makeMap1Text();
	}
}
