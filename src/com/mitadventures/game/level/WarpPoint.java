package com.mitadventures.game.level;

import com.mitadventures.game.Game;

public class WarpPoint {
	
	public int level1;
	public int level2;
	public int xTile1;
	public int yTile1;
	public int xTile2;
	public int yTile2;
	public String playerDir;
	public boolean warp = false;
	
	
	public Game game;

	public WarpPoint(Game game, int level1, int xTile1, int yTile1, int level2, int xTile2, int yTile2, String playerDir) {
		this.level1 = level1;
		this.level2 = level2;
		this.xTile1 = xTile1;
		this.yTile1 = yTile1;
		this.xTile2 = xTile2;
		this.yTile2 = yTile2;
		this.playerDir = playerDir;
		this.game = game;
	}
	
	// Warp Test //
		public void warpTest() {
			if (game.levelNum == level1) {
				if (game.player.getCurrentXTile() == xTile1 & game.player.getCurrentYTile() == yTile1) {
					warp = true;
				}
			}
		}
	/////////////////////
}
