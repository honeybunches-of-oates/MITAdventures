package com.mitadventures.game.level.tiles;

import com.mitadventures.game.Game;

public abstract class Tile {

	// {2, 3, 4, 5, 7, 9, 10, 11, 12, 14, 15, 16,
	// 17, 21, 22, 23, 33, 34, 35, 39, 40, 41, 44, 46,47, 48, 49, 50, 52,
	// 54, 55, 57, 60, 61, 62, 70, 71, 75, 78, 81, 82, 83, 84, 86, 91, 92,
	// 94, 98, 100, 101, 102, 103, 104, 105, 106, 107, 108, 110, 111, 115,
	// 116, 117, 121, 122, 124, 126, 127, 128, 129, 130, 131}

	public static boolean getSolidity(int tileid, Game game) {
		boolean solidity = false;
		if (game.levelNum == 1) {
			int solidTiles[] = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
					12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
					27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 46,
					47, 48, 49, 50, 53, 54, 55, 63, 64, 71, 78, 79, 80, 81, 82,
					94, 95, 96, 97, 105, 106, 107, 109, 112, 113, 114, 115,
					116, 117, 118, 119, 120, 122, 123, 124, 125, 126, 127, 128,
					129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140,
					141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152,
					153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164,
					165, 166, 167, 168, 169, 171, 172, 173, 174, 176, 177, 178,
					179, 180, 181, 182, 184, 185, 186, 187, 188, 191, 192, 194,
					195, 196, 200, 202, 203, 210, 211, 213, 217, 259, 263, 267,
					268, 269, 270, 271, 272, 274, 276, 277, 278, 279, 280, 281,
					282, 283, 284, 285, 286, 287, 288, 303};
			for (int i = 0; i < solidTiles.length; i++) {
				if (tileid == solidTiles[i]) {
					solidity = true;
					break;
				}
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
