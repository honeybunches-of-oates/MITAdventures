package com.mitadventures.game.level.tiles;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.mitadventures.game.Game;

public abstract class Tile {
	
	public static String findSolidTileIDs(BufferedImage solidTileSheet, List<BufferedImage> tileSet) {
		List<Integer> solidTileIDs_dynamic = new ArrayList<Integer>();
		int solidpixels[] = new int[16 * 16];
		int spritesheetpixels[] = new int[16 * 16];
		for (int y_solid = 0; y_solid < solidTileSheet.getHeight() / 16; y_solid++) {
			for (int x_solid = 0; x_solid < solidTileSheet.getWidth() / 16; x_solid++) {
				BufferedImage solidTile = ((BufferedImage) solidTileSheet).getSubimage(x_solid *16, y_solid * 16, 16, 16);
				PixelGrabber solidpixelgrabber = new PixelGrabber(solidTile, 0, 0, 16, 16, solidpixels, 0, 16);
				try {
					solidpixelgrabber.grabPixels();
				} catch (InterruptedException e) {
					System.out.println(e);
				}
				boolean finished = false;
				for (int index = 0; index < tileSet.size(); index++) {
					if (finished)
						break;
					PixelGrabber spritesheetpixelgrabber = new PixelGrabber(tileSet.get(index), 0, 0, 16, 16, spritesheetpixels, 0, 16);
					try {
						spritesheetpixelgrabber.grabPixels();
					} catch (InterruptedException e) {
						System.out.println(e);
					}
					boolean match = false;
					for (int i = 0; i < spritesheetpixels.length; i++) {
						if (spritesheetpixels[i] != solidpixels[i])
							break;
						if (i == spritesheetpixels.length - 1)
							match = true;
					}
					if (match) {
						solidTileIDs_dynamic.add(new Integer(index + 1));
						finished = true;
						break;
					}
				}
			}
		}
		int[] solidTileIDs_static = new int[solidTileIDs_dynamic.size()];
		for(int i = 0; i < solidTileIDs_static.length; i++)
			solidTileIDs_static[i] = solidTileIDs_dynamic.get(i).intValue();
		System.out.println(solidTileIDs_dynamic);
		return solidTileIDs_dynamic.toString();
	}
	
	public static int[] getList(String solidTileText) {
		int length = 0;
		for (int i = 1; i < solidTileText.length() - 1; i++) {
				if (solidTileText.charAt(i) == ',')
					length++;
		}
		solidTileText = solidTileText.substring(1, solidTileText.length() - 1);
		int[] solidTilesList = new int[length + 1];
		for (int i = 0; i < length + 1; i++) {
			int splitpoint = solidTileText.indexOf(' ');
			if (splitpoint == -1)
				splitpoint = solidTileText.length() + 1;
			String solidTileID = solidTileText.substring(0, splitpoint - 1);
			for (int j = 0; j < solidTileID.length(); j++)
				solidTilesList[i] += (solidTileID.charAt(solidTileID.length() - j - 1) - 48) * Math.pow(10, j);
			if (i != length)
				solidTileText = solidTileText.substring(splitpoint + 1);
		}
		return solidTilesList;
	}

	public static boolean getSolidity(int tileid, int[] solidTiles) {
		boolean solidity = false;
		for (int i = 0; i < solidTiles.length; i++) {
			if (tileid == solidTiles[i]) {
				solidity = true;
				break;
			}
		}
		return solidity;
	}

	public static boolean upBoundary(int tileid) {
		boolean boundary;
		switch (tileid) {
		case 29:
			boundary = true;
			break;
		default:
			boundary = false;
		}
		return boundary;
	}

	public static boolean downBoundary(int tileid) {
		boolean boundary;
		switch (tileid) {
		case 29:
			boundary = true;
			break;
		default:
			boundary = false;
		}
		return boundary;
	}

	public static boolean leftBoundary(int tileid) {
		boolean boundary;
		switch (tileid) {
		case 29:
			boundary = true;
			break;
		default:
			boundary = false;
		}
		return boundary;
	}

	public static boolean rightBoundary(int tileid) {
		boolean boundary;
		switch (tileid) {
		case 29:
			boundary = true;
			break;
		default:
			boundary = false;
		}
		return boundary;
	}
}
