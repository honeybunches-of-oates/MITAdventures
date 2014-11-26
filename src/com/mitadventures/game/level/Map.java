 /* Map Class
 * Retrieves the width, height, and tile id of a map
 * Can alter tile numbers in a map object
 * 
 * Map format:
 * -tiles are given a number
 * -tiles must be separated by one or more spaces
 * -rows of tiles must be separated by a forward slash
 * -maps must have equal widths between rows, any unused spaces shall be labeled with the void tile on your spritesheet
 * -standard map notation should look like this:
 * 		 ("1 9 10 5 17 123 3/"
 * 		+ "5 6 3  7 4  23  5/"
 * 		+ "1 9 10 5 17 123 3/"
 * 		+ "5 6 3  7 4  23  5/")
 * -negative numbers are invisible, 0 is black
 */

package com.mitadventures.game.level;

public class Map {

	// Reference Variables //
	public String map;
	/////////////////////////
	
	// Map Constructor //
	public Map(String map) {
		this.map = map;
	}
	/////////////////////
	
	// Get Width Method //
	public int getWidth() {
		int width = 0;
		int index = 0;
		char thisChar;
		char nextChar;
		do {
		thisChar = map.charAt(index);
		nextChar = map.charAt(index + 1);
		index++;
		if (thisChar == ' ') {
			width++;
			if (nextChar == ' ')
				width--; 
		}
		} while (thisChar != '/');
		width++;
		return width;
	}
	//////////////////////
	
	// Get Actual Width Method //
	public int getActualWidth() {
		return map.indexOf("/", 0);
	}
	/////////////////////////////
	
	// Get Height Method //
	public int getHeight() {
		int height = 0;
		int index = 0;
		char thisChar;
		do {
		thisChar = map.charAt(index);
		index++;
		if (thisChar == '/')
			height++;
		} while (index < map.length());
		return height;
	}
	///////////////////////
	
	// Get Tile ID Method //
	public int getTile(int xTilePos, int yTilePos) {
		
		int index1 = yTilePos * (this.getActualWidth() + 1);
		int index2 = 0;
		int tile = 0;
		char thisChar = map.charAt(index1);
		char nextChar = map.charAt(index1 + 1);
		boolean negative = false;
		
		while (index2 < xTilePos) {
			thisChar = map.charAt(index1);
			nextChar = map.charAt(index1 + 1);
			index1++;
			if (thisChar == ' ') {
				index2++;
				if (nextChar == ' ' || nextChar == '/')
					index2--;
			}
		}
		
		if (thisChar == '-') {
			index1++;
			negative = true;
		}
		
		int numDigits = Math.min(map.indexOf(' ', index1), map.indexOf('/', index1)) - index1;
		if (map.indexOf(' ', index1) == -1) {
			numDigits = map.indexOf('/', index1) - index1;
		}
		
		for (int i = numDigits; i > 0; i--) {
			thisChar = map.charAt(index1);
			tile += (thisChar - 48) * Math.pow(10, i - 1);
			index1++;
		}
		
		if (negative)
			tile = tile * -1;
		
		return tile;
	}
	////////////////////////
	
	public static int getTile(int xPos, int yPos, Map layer) {
		return layer.getTile(xPos, yPos);
	}
	
	//Note: Make all replacement tiles have the same number of digits!
	
	// Replace Tile Method //
	public void replaceTile(int xTilePos, int yTilePos, int tile) {
		String replacement = "" + tile;
		
		int index1 = yTilePos * (this.getActualWidth() + 1);
		int index2 = 0;
		int index3;
		int tileIndex;
		char thisChar = map.charAt(index1);
		char nextChar = map.charAt(index1 + 1);
		
		while (index2 < xTilePos) {
			thisChar = map.charAt(index1);
			nextChar = map.charAt(index1 + 1);
			index1++;
			if (thisChar == ' ') {
				index2++;
				if (nextChar == ' ' || nextChar == '/')
					index2--;
			}
		}
		
		tileIndex = index1;
		
		index3 = Math.min(map.indexOf(' ', index1), map.indexOf('/', index1)) - index1;
		if (map.indexOf(' ', index1) == -1) {
			index3 = map.indexOf('/', index1) - index1;
		}
		
		String segmentAfter = map.substring(tileIndex, map.length());
		String segmentBefore = map.substring(0, tileIndex - 1);
		String segmentAfter2 = segmentAfter.substring(index3 + 1, segmentAfter.length());
		segmentAfter = replacement + " " + segmentAfter2;
		this.map = segmentBefore + " " + segmentAfter;
	}
	/////////////////////////
}

