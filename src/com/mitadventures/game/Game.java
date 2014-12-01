package com.mitadventures.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.mitadventures.game.characters.Entity;
import com.mitadventures.game.characters.NPC;
import com.mitadventures.game.characters.Player;
import com.mitadventures.game.graphics.Fonts;
import com.mitadventures.game.graphics.MenuBar;
import com.mitadventures.game.graphics.MessageBox;
import com.mitadventures.game.graphics.Screen;
import com.mitadventures.game.graphics.SpriteSheet;
import com.mitadventures.game.level.Level;
import com.mitadventures.game.level.Map;
import com.mitadventures.game.level.WarpPoint;
import com.mitadventures.game.level.tiles.AnimatedTile;
import com.mitadventures.game.level.tiles.Tile;

public class Game extends Canvas implements Runnable {

	// Game Variables //
	private static final long serialVersionUID = 1L;

		// Size of Game Window
	public static final int WIDTH = 208;
	public static final int HEIGHT = 176;
	public static final int SCALE = 3; 
	public static final String NAME = "The Legend of Zelda: Twilight Princess";

	private JFrame frame;

		// Refers to running program
	public boolean running = false;
	public int tickCount = 0;
	private Timer timer;
	
		// Game Images
	private Image titlescreen;
	private Image startbar;
	private Image messageBox;
	private Image startBox_middle;
	private Image startBox_bottom;
	private Image startBox_top;
	private Image startBox_right;
	private Image startBox_left;
	private Image startBox_bottomleft;
	private Image startBox_topleft;
	private Image startBox_bottomright;
	private Image startBox_topright;
	private Image selector;
	private Image continueArrow;
	private BufferedImage animationSheet;
	private BufferedImage solidTileSheet;
	
		// Game States
	private boolean atTitle = true;
	private boolean playing = false;
	private boolean start_is_visible = false;
	private boolean atStartMenu = false;
	
		// Game Objects
	public Screen screen;
	public Player player;
	public Controller controller;
	private Fonts fonts;
	private int quarterseconds;
	private int tilestage;
	
		// Level Objects
	public Level level;
	public int levelNum = 1;
	public Map layer1;
	public Map layer2;
	public List<WarpPoint> warpPoints = new ArrayList<WarpPoint>();
	
		// Animation tiles *WORK IN PROGRESS*
	private boolean tilechange;
	private int xTilePosGrass = 0;
	private int yTilePosGrass = 0;
	
		// Actions	
	public boolean messageBoxOpen = false;
	public boolean menuOpen = false;
	private int entityIdentity;
	private boolean freeNPC;
	
		// Array for individual tiles from spritesheet
	private List<BufferedImage> tileset = new ArrayList<BufferedImage>();
	
	private List<AnimatedTile> animationSet = new ArrayList<AnimatedTile>();
	public int[] solidTilesList;
	
		// Array for individual alphanumerical characters from font sheet
	private List<BufferedImage> font = new ArrayList<BufferedImage>();
	
	private BufferedImage fontsheet;
	
		// NPC List
	private List<NPC> NPCs = new ArrayList<NPC>();
	
		// Menus
	private MenuBar startMenu;
	private MenuBar pauseMenu;
	private MenuBar yesNo;
	/////////////
	
	// Game Constructor //
	public Game() {
		super();
			// Sets up JFrame
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		frame = new JFrame(NAME);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		frame.add(this, BorderLayout.CENTER);
		frame.pack();

		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	//////////////////////
	
	// Run Method //
	public void run() {
			// Implements tick function, will report frames per second and ticks per second to console
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;

		int ticks = 0;
		int frames = 0;
		// resets every minute
		quarterseconds = 0;
		
		long lastTimer = System.currentTimeMillis();
		long lastTimer2 = System.currentTimeMillis();
		double delta = 0;
		
			// Initializes game data
		initStart();

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;

			while (delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}
				//Thread.sleep: slows down fps
			try {
				Thread.sleep(7);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (shouldRender) {
				frames++;
				render();
			}
			
			if (System.currentTimeMillis() - lastTimer2 >= 250) {
				lastTimer2 += 250;
				quarterseconds++;
				if(quarterseconds % 2 == 0) {
					tilestage++;
				}
					if(tilestage > 2)
						tilestage = 0;
				if (quarterseconds > 63)
					quarterseconds = 0;
			}
			
			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				System.out.println(frames + " fps, " + ticks + " tps, " + timer.getTime());
				frames = 0;
				ticks = 0;
				if (start_is_visible)
					start_is_visible = false;
				else if (!start_is_visible)
					start_is_visible = true;
				timer.tick();
			}
			
			if (atTitle & controller.start.is_hit) {
				controller.start.is_hit = false;
				atStartMenu = true;
				start_is_visible = false;
			}
			
			if (atTitle & atStartMenu)
				tickStartMenu();
		}
	}
	////////////////
	
	// Tick Method //
	public void tick() {
			// Ticks game/level
		tickCount++;
		if (playing)
			level.tick();
			// Checks for actions
		controller.check();
	}
	/////////////////
	
	// Initialize Start Method //
	public void initStart() {
		loadImages();
		controller = new Controller(this);
		startMenu = new MenuBar(new String[] {"Continue", "New Game"}, font);
		pauseMenu = new MenuBar(new String[] {"Controls", "Stats", "Save", "Save and Quit", "Continue"}, font);
		fonts = new Fonts();
		timer = new Timer();
	}
	/////////////////////////////
	
	// Initialize Method //
	public void init() {
			// Retrieves level map data
		createLevel(levelNum);
			// Creates and inserts player into map at tile (12, 31) *coordinates are multiplied by 16, the size of each tile*
		player = new Player(this, level, 12 * 16, 31 * 16, controller, "Link");
		level.addEntity(player);
			// Loads all sprite, game, and map images
	}
	///////////////////////
	
	// Initialize when Loading Game Method //
	public void initLoad() {
		init();
		loadGame();
			// Almost same as init() method, but used for loading saved game files, not starting new game files
		createLevel(levelNum);
		player = new Player(this, level, player.xPos1, player.yPos1, controller, "Link");
		level.addEntity(player);
	}
	/////////////////////////////////////////
	
	// Load Images Method //
	public void loadImages() {
			// Loading all images from res folder
		try {
		    titlescreen = ImageIO.read(Game.class.getResourceAsStream("/logo.png"));
		} catch (IOException e) {
			System.out.println(e);
		} try {
		    startbar = ImageIO.read(Game.class.getResourceAsStream("/start.png"));
		} catch (IOException e) {
			System.out.println(e);
		} try {
		    messageBox = ImageIO.read(Game.class.getResourceAsStream("/MessageBox1.png"));
		} catch (IOException e) {
			System.out.println(e);
		} try {
		    startBox_middle = ImageIO.read(Game.class.getResourceAsStream("/MessageBox2(middle).png"));
		} catch (IOException e) {
			System.out.println(e);
		} try {
		    startBox_bottom = ImageIO.read(Game.class.getResourceAsStream("/MessageBox2(bottom).png"));
		} catch (IOException e) {
			System.out.println(e);
		} try {
		    startBox_top = ImageIO.read(Game.class.getResourceAsStream("/MessageBox2(top).png"));
		} catch (IOException e) {
			System.out.println(e);
		} try {
		    startBox_right = ImageIO.read(Game.class.getResourceAsStream("/MessageBox2(right).png"));
		} catch (IOException e) {
			System.out.println(e);
		} try {
		    startBox_left = ImageIO.read(Game.class.getResourceAsStream("/MessageBox2(left).png"));
		} catch (IOException e) {
			System.out.println(e);
		} try {
		    startBox_bottomleft = ImageIO.read(Game.class.getResourceAsStream("/MessageBox2(bottomleft).png"));
		} catch (IOException e) {
			System.out.println(e);
		} try {
		    startBox_topleft = ImageIO.read(Game.class.getResourceAsStream("/MessageBox2(topleft).png"));
		} catch (IOException e) {
			System.out.println(e);
		} try {
		    startBox_bottomright = ImageIO.read(Game.class.getResourceAsStream("/MessageBox2(bottomright).png"));
		} catch (IOException e) {
			System.out.println(e);
		} try {
		    startBox_topright = ImageIO.read(Game.class.getResourceAsStream("/MessageBox2(topright).png"));
		} catch (IOException e) {
			System.out.println(e);
		} try {
		    startbar = ImageIO.read(Game.class.getResourceAsStream("/start.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
		    selector = ImageIO.read(Game.class.getResourceAsStream("/selarrow.png"));
		} catch (IOException e) {
			System.out.println(e);
		} try {
			continueArrow = ImageIO.read(Game.class.getResourceAsStream("/contarrow.png"));
		} catch (IOException e) {
			System.out.println(e);
		} try {
			fontsheet = ImageIO.read(Game.class.getResourceAsStream("/MacChicago.png"));
		} catch (IOException e) {
			System.out.println(e);
		} try {
			animationSheet = ImageIO.read(Game.class.getResourceAsStream("/animationsheet.png"));
		} catch (IOException e) {
			System.out.println(e);
		} try {
			solidTileSheet = ImageIO.read(Game.class.getResourceAsStream("/SolidTiles.png"));
		} catch (IOException e) {
			System.out.println(e);
		}
		
			// Loading fontsheet, String sizes indicates pixel widths of characters as they appear on the fontsheet
		String sizes = "43784648588888888448814 88888888689899888878889888 88788788478498888776889888";
		int index = 0;
		int row = 0;
		
		for (int i = 0; i < sizes.length(); i++) {
			if (sizes.charAt(i) == ' ') {
				index = 0;
				row++;
			} else {
				int size = ((int) sizes.charAt(i)) - 48;
				font.add(fontsheet.getSubimage(index, row * 16, size, 16));
				index += size;
			}
		}
		loadAnimationTiles();
	}
	////////////////////////
	
	// Add WarpPoint Method //
	public void addWarpPoint(WarpPoint warpPoint) {
			// For adding a warp point to the map's warp point array
		this.warpPoints.add(warpPoint);
	}
	//////////////////////////
	
	// Warp Test all Warp Points Method //
	public void warpTests() {
			// Checks to see if player is at a warp point
		for (WarpPoint w : warpPoints)
			w.warpTest();
			// Warps player to corresponding warp point
		for (int i = 0; i < warpPoints.size(); i++) {
			if (warpPoints.get(i).warp) {
				this.levelNum = warpPoints.get(i).level2;
				createLevel(levelNum);
			    player.setCurrentXTile(warpPoints.get(i).xTile2);
				player.setCurrentYTile(warpPoints.get(i).yTile2);
				player.setDirection(warpPoints.get(i).playerDir);
				warpPoints.get(i).warp = false;
				player.isWalking = true;
				level.addEntity(player);
			}
		}
	}
	//////////////////////////////////////
	
	// Grass Test Method //
	public void grassTest() {
		if (tilechange & (xTilePosGrass != player.getCurrentXTile() || yTilePosGrass != player.getCurrentYTile())) {
			layer1.replaceTile(xTilePosGrass, yTilePosGrass, 38);
			tilechange = false;
		}
		
		this.xTilePosGrass = player.getCurrentXTile();
		this.yTilePosGrass = player.getCurrentYTile();
		if (layer1.getTile(xTilePosGrass, yTilePosGrass) == 38) {
			layer1.replaceTile(xTilePosGrass, yTilePosGrass, 39);
			this.tilechange = true;
		}
	}
	///////////////////////
	
	// Set Animation Set Tiles //
	public void loadAnimationTiles() {
		for (int y = 0; y < animationSheet.getHeight() / 16; y++) {
			List<BufferedImage> list = new ArrayList<BufferedImage>();
			for (int x = 0; x < animationSheet.getWidth() / 16; x++) {
				list.add(animationSheet.getSubimage(16 * x, 16 * y, 16, 16));
			}
			animationSet.add(new AnimatedTile(list, animationSheet.getWidth() / 16));
		}
	}
	/////////////////////////////
	
	// Load SpriteSheet Method //
	public void loadSpriteSheet(BufferedImage spritesheet) {
			// Loading all tiles from spritesheet to array
		tileset.clear();
		for (int y = 0; y < spritesheet.getHeight() / 16; y++) {
			for (int x = 0; x < spritesheet.getWidth() / 16; x++) {
				tileset.add(spritesheet.getSubimage(x * 16, y * 16, 16, 16));
			}
		}
	}
	/////////////////////////////
	
	// Create Levels Method //
	public void createLevel(int levelNum) {
		NPCs.clear();
		String name = "";
			// Decides which level player is currently in
		switch (levelNum) {
			/* Each case creates two layers, a base layer and a top layer (rendered over the player), 
			 * the layers are a set of strings with numbers corresponding to a tile value from a spritesheet, 
			 * rows are indicated with slashes, see class Map and ImageToMap for details.
			 * All other level specific objects are added here
			 */
		case 1: name = "Sacred_Grove_Temple";
				setupMap(name);
			// Message Boxes
				MessageBox sign1 = new MessageBox(14, 42, "If you were to call someone, and their phone were off, then they would not answer the phone. A stone in the hand kills two birds in the bush. Don't take the blue acid! Man, this is one cool message box system.");
				level.addMessageBox(sign1);
			// NPCs
				NPC genericNPC = new NPC(this, level, 14, 34, "Don't take the blue acid!", "Link");
				NPCs.add(genericNPC);
				level.addEntity(genericNPC);
			// Warp Points
				WarpPoint warp1 = new WarpPoint(this, 1, 16, 42, 2, 20, 20, "down");
				addWarpPoint(warp1);
			break;
					
		case 2: name = "Ordon_Village_Main";
				setupMap(name);
			// Message Boxes
				MessageBox sign2 = new MessageBox(12, 7, "Hello World!");
				level.addMessageBox(sign2);
				
			// NPCs
				NPC genericNPC2 = new NPC(this, level, 26, 18, "Don't take the blue acid!", "Link");
				NPCs.add(genericNPC2);
				level.addEntity(genericNPC2);
			break;
		}
		//loadSpriteSheet();
		setAnimatedTiles(name);
	}
	//////////////////////////
	
	// Setup Map Method //
	private void setupMap(String name) {
		String mapText1 = "";
		String mapText2 = "";
		String solidTileText = "";
		try {
			File file = new File("/Users/Richard/Documents/workspace/MITAdventures/res/Spritesheet(" + name + ")_mapText.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			try {
				mapText1 = br.readLine();
				mapText2 = br.readLine();
				solidTileText = br.readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
			layer1 = new Map(mapText1);
			layer2 = new Map(mapText2);
			level = new Level(name, layer1, layer2);
		   	screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/Spritesheet(" + name + ").png"));
		   	solidTilesList = Tile.getList(solidTileText);
	}
	//////////////////////
	
	// Start Method //
	public synchronized void start() {
			// Starts Game
		running = true;
		new Thread(this).start();

	}
	//////////////////
	
	// Stop Method //
	public synchronized void stop() {
			// Stops Game
		running = false;
	}
	/////////////////
	
	// Render Function of Pause Menu //
	public void renderPauseMenu(Graphics g) {
		tickPauseMenu();
		if (controller.start.is_hit || menuOpen) {
			controller.start.is_hit = false;
			menuOpen = true;
			renderMenuBox(pauseMenu, g);
			player.canMove = false;
		}
	}
	///////////////////////////////////
	
	// Tick Pause Menu //
	public void tickPauseMenu() {
		if (controller.start.is_hit & menuOpen) {
			controller.start.is_hit = false;
			menuOpen = false;
			player.canMove = true;
			fonts.resetBoxIndex();
		}
		if (controller.a_button.is_hit & menuOpen) {
			controller.a_button.is_hit = false;
			switch  (pauseMenu.getSelected()) {
				case 0:
					break;
				case 1:
					break;
				case 2: saveGame();
						System.out.println("Saved.");
					break;
				case 3: saveGame();
						System.out.println("Saved.");
						stop();
						this.frame.removeAll();
					break;
				case 4: menuOpen = false;
						player.canMove = true;
					break;
					
			}	
			fonts.resetBoxIndex();
		}
	}
	/////////////////////
	
	// Tick Start Menu //
	public void tickStartMenu() {
		if (controller.a_button.is_hit & atStartMenu) {
			controller.a_button.is_hit = false;
			switch  (startMenu.getSelected()) {
				case 0: initLoad();
					break;
				case 1: init();
					break;
			}
			playing = true;
			atTitle = false;
			timer.start();
			fonts.resetBoxIndex();
		}
	}
	/////////////////////
	
	// Render Menu Box Parts Method //
	public void renderBox(int width, int height, Graphics g) {
		g.drawImage(startBox_middle, (WIDTH * SCALE - width) / 2 - 10, (HEIGHT * SCALE - height) / 2 - 8, width + 20, height + 20, null);
		g.drawImage(startBox_top, (WIDTH * SCALE - width) / 2 - 10, (HEIGHT * SCALE - height) / 2 - 8, width + 20, 8, null);
		g.drawImage(startBox_bottom, (WIDTH * SCALE - width) / 2 - 10, (HEIGHT * SCALE - height) / 2 + height + 4, width + 20, 8, null);
		g.drawImage(startBox_left, (WIDTH * SCALE - width) / 2 - 10, (HEIGHT * SCALE - height) / 2 - 8, 8, height + 20, null);
		g.drawImage(startBox_right, (WIDTH * SCALE - width) / 2 + width + 8, (HEIGHT * SCALE - height) / 2 - 8, 8, height + 20, null);
		g.drawImage(startBox_topleft, (WIDTH * SCALE - width) / 2 - 10, (HEIGHT * SCALE - height) / 2 - 8, 8, 8, null);
		g.drawImage(startBox_bottomleft, (WIDTH * SCALE - width) / 2 - 10, (HEIGHT * SCALE - height) / 2 + height + 4, 8, 8, null);
		g.drawImage(startBox_topright, (WIDTH * SCALE - width) / 2 + width + 8, (HEIGHT * SCALE - height) / 2 - 8, 8, 8, null);
		g.drawImage(startBox_bottomright, (WIDTH * SCALE - width) / 2 + width + 8, (HEIGHT * SCALE - height) / 2 + height + 4, 8, 8, null);
	}
	//////////////////////////////////
	
	// Render Menu Box Method //
	public void renderMenuBox(MenuBar menu, Graphics g) {
		renderBox(menu.width, menu.height, g);
		for (int i = 0; i < menu.getOptions().length; i++)
			fonts.render(menu.getOptions()[i], screen, (WIDTH * SCALE - menu.width) / 2, (HEIGHT * SCALE - menu.height) / 2 + (48 * i), WIDTH, 1, g, font);
		g.drawImage(selector, (WIDTH * SCALE - menu.width) / 2 - 18, (HEIGHT * SCALE - menu.height) / 2 + 4 + (48 * menu.getSelected()), 16, 16, null);
		if (controller.up.is_hit) {
			controller.up.is_hit = false;
			menu.selectUp();
		}
		if (controller.down.is_hit) {
			controller.down.is_hit = false;
			menu.selectDown();
		}
	}
	////////////////////////////
	
	// Render Message Box Method //
	public void renderMessageBox(Graphics g) {
			// Direction of box booleans
		boolean above = false;
		boolean below = false;
		boolean toRight = false;
		boolean toLeft = false;
		int index = 0;
		if (player.numSteps == 0) {
			if (controller.a_button.is_hit & messageBoxOpen) {
				controller.a_button.is_hit = false;
				if (fonts.messageComplete()) {
					messageBoxOpen = false;
					player.canMove = true;
					this.freeNPC = true;
					fonts.resetBoxIndex();
				} else {
					fonts.nextBoxIndex();
				}
			}
			for (MessageBox m : level.messageBoxes) {
				if (player.direction == 1 & m.getYPos() + 1 == player.getCurrentYTile() & m.getXPos() == player.getCurrentXTile())
					above = true;
				if (player.direction == 3 & m.getYPos() - 1 == player.getCurrentYTile() & m.getXPos() == player.getCurrentXTile())
					below = true;
				if (player.direction == 4 & m.getYPos() == player.getCurrentYTile() & m.getXPos() + 1 == player.getCurrentXTile())
					toRight = true;
				if (player.direction == 2 & m.getYPos() == player.getCurrentYTile() & m.getXPos() - 1 == player.getCurrentXTile())
					toLeft = true;
				if ((above || below || toRight || toLeft) & (controller.a_button.is_hit || messageBoxOpen)) {
						g.drawImage(messageBox, 0, 384, getWidth(), getHeight() - 384, null);
						fonts.render(m.getMessage(), screen, 16, 132 * SCALE, WIDTH, 4, g, font);
						messageBoxOpen = true;
						controller.a_button.is_hit = false;
						player.canMove = false;
				} else {
					for (NPC npc : NPCs) {
						if (npc.numSteps == 0) {
							index++;
							if (player.direction == 1 & player.getCurrentYTile() - 1 == npc.getCurrentYTile() & player.getCurrentXTile() == npc.getCurrentXTile())
								above = true;
							if (player.direction == 3 & player.getCurrentYTile() + 1 == npc.getCurrentYTile() & player.getCurrentXTile() == npc.getCurrentXTile())
								below = true;
							if (player.direction == 4 & player.getCurrentYTile() == npc.getCurrentYTile() & player.getCurrentXTile() + 1 == npc.getCurrentXTile())
								toRight = true;
							if (player.direction == 2 & player.getCurrentYTile() == npc.getCurrentYTile() & player.getCurrentXTile() - 1 == npc.getCurrentXTile())
								toLeft = true;
							if ((above || below || toRight || toLeft) & (controller.a_button.is_hit || messageBoxOpen)) {
									g.drawImage(messageBox, 0, 384, getWidth(), getHeight() - 384, null);
									fonts.render(npc.getMessage(), screen, 16, 132 * SCALE, WIDTH, 4, g, font);
									messageBoxOpen = true;
									controller.a_button.is_hit = false;
									player.canMove = false;
									if (above)
										npc.setDirection("down");
									if (below)
										npc.setDirection("up");
									if (toRight)
										npc.setDirection("left");
									if (toLeft)
										npc.setDirection("right");
									npc.canMove = false;
									this.entityIdentity = index;
							}
							if (freeNPC & entityIdentity == index) {
								npc.canMove = true;
								npc.crashTest();
								freeNPC = false;
							}
						}
					}
				}
			}
		}
		if (!fonts.messageComplete() & messageBoxOpen) {
			int y_shift = 0;
			switch ((quarterseconds / 2) % 4) {
				case 0: y_shift = 0;
					break;
				case 1: y_shift = -3;
					break;
				case 2: y_shift = 0;
					break;
				case 3: y_shift = 3;
			}
			g.drawImage(continueArrow, WIDTH * SCALE - 30, HEIGHT * SCALE - 24 + y_shift, 16, 16, null);
		}
	}
	///////////////////////////////
	
	// OnScreen Check //
	public boolean onScreen(Entity e) {
		if (player.xPos + (WIDTH / 2) + 16 > e.xPos & player.xPos - (WIDTH / 2) - 16 < e.xPos & player.yPos + (HEIGHT / 2) + 32 > e.yPos & player.yPos - (HEIGHT / 2) - 32 < e.yPos)
			return true;
		else
			return false;
	}
	////////////////////
	
	// Overlap Check Sort //
	public void overlapCheckSort() {
		boolean sorted;
		do {
			sorted = true;	
			for (int i = 0; i < level.entities.size() - 1; i++) {
				if (onScreen(level.entities.get(i))) {		
						if (level.entities.get(i).yPos > level.entities.get(i + 1).yPos) {
							Entity temp = level.entities.get(i);
							level.entities.set(i, level.entities.get(i + 1));
							level.entities.set(i + 1, temp);	
							sorted = false;
						}	
				}	
			}
		} while (!sorted);
	}
	////////////////////////
	
	// Render Characters //
	public void renderCharacters(Graphics g) {
		overlapCheckSort();
		for (Entity e : level.entities) {
			if (onScreen(e)) {
				g.drawImage(e.spriteSets.get(e.getStage()).sprites[e.direction - 1][e.spriteStage], (6 * 16 + (e.xPos - player.xPos)) * SCALE + 3, (4 * 16 + (e.yPos - player.yPos)) * SCALE + 12, 14 * SCALE, 28 * SCALE, null);
			}
		}		
	}
	///////////////////////
	
	// Render Layer Method //
	public void renderLayer(Screen screen, int xOffset, int yOffset, Map layer, Graphics g) {
			// Creates boundaries for player movement
		if (xOffset > ((level.width << 4) - screen.width))
			xOffset = ((level.width << 4) - screen.width);
		if (xOffset < 0)
			xOffset = 0;
		if (yOffset > ((level.height << 4) - screen.height))
			yOffset = ((level.height << 4) - screen.height);
		if (yOffset < 0)
			yOffset = 0;
			
			// Adjusts screen so player is in the center
		screen.setOffset(xOffset, yOffset);
		
			// Renders the segment of the map surrounding the player and one tile above, to the right, below and to the left of the screen so movement appears smooth
		for (int y = 0; y < level.height; y++) {
			for (int x = 0; x < level.width; x++) {
					// Checks that map tile is readable
				if (Map.getTile(x, y, layer) >= 0) {
					if (x > (player.xPos / 16) - (screen.width / 32) - 1 && y > (player.yPos / 16) - (screen.height / 32) - 2 && x < (player.xPos / 16) + (screen.width / 32) + 2 && y < (player.yPos / 16) + (screen.height / 32) + 2) {
						if (!AnimatedTile.isAnimated(animationSet, Map.getTile(x, y, layer)))
							g.drawImage(tileset.get(Map.getTile(x, y, layer) - 1), (x * 16 - xOffset)  * SCALE, (y * 16 - yOffset) * SCALE, 16 * SCALE, 16 * SCALE, null);
						else {
							g.drawImage(animationSet.get(AnimatedTile.getIndex(animationSet, Map.getTile(x, y, layer))).getImage(tilestage), (x * 16 - xOffset)  * SCALE, (y * 16 - yOffset) * SCALE, 16 * SCALE, 16 * SCALE, null);
						}
					}
				}
			}
		}
	}
	/////////////////////////
	
	
	// Set Animated Tile Sheet Method //
	public void setAnimatedTiles(String name) {
		int animationpixels[] = new int[16 * 16];
		int spritesheetpixels[] = new int[16 * 16];
		BufferedImage spritesheet = null;
		try {
		    spritesheet = ImageIO.read(Game.class.getResourceAsStream("/Spritesheet(" + name + ").png"));
		} catch (IOException e) {
			System.out.println(e);
		}
		loadSpriteSheet(spritesheet);
		for (int setnum = 0; setnum < animationSheet.getHeight() / 16; setnum++) {
			BufferedImage animationTile = ((BufferedImage) animationSheet).getSubimage(0, setnum * 16, 16, 16);
			PixelGrabber animpixelgrabber = new PixelGrabber(animationTile, 0, 0, 16, 16, animationpixels, 0, 16);
			try {
				animpixelgrabber.grabPixels();
			} catch (InterruptedException e) {
				System.out.println(e);
			}
			boolean finished = false;
			for (int y = 0; y < spritesheet.getHeight() / 16; y++) {
				if (finished)
					break;
				for (int x = 0; x < spritesheet.getWidth() / 16; x++) {
					BufferedImage spritesheetTile = spritesheet.getSubimage(x * 16, y * 16, 16, 16);
					PixelGrabber spritesheetpixelgrabber = new PixelGrabber(spritesheetTile, 0, 0, 16, 16, spritesheetpixels, 0, 16);
					try {
						spritesheetpixelgrabber.grabPixels();
					} catch (InterruptedException e) {
						System.out.println(e);
					}
					boolean match = false;
					for (int i = 0; i < spritesheetpixels.length; i++) {
						if (spritesheetpixels[i] != animationpixels[i])
							break;
						if (i == spritesheetpixels.length - 1)
							match = true;
					}
					if (match) {
						System.out.println("match " + (y * spritesheet.getWidth() / 16 + x + 1) + " " + setnum);
						animationSet.get(setnum).setTileId(y * spritesheet.getWidth() / 16 + x + 1);
						finished = true;
						break;
					} else {
						animationSet.get(setnum).setTileId(-2);
					}
				}
			}
		}
		for (AnimatedTile anim : animationSet) {
			System.out.print(anim.getTileId());
		}
		System.out.println();
	}
	////////////////////////////////////
	
	// Main Render Method //
	public void render() {
			// Set up buffer strategy, uses three image buffers
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
			// Sets up graphics based on buffer strategy
		Graphics g = bs.getDrawGraphics();
		
		if (playing & !atTitle) {		
				// Sets up the offset for where the screen starts relative to the player in the context of the map
			int xOffset = player.xPos + 8 - (screen.width / 2);
			int yOffset = player.yPos + 8 - (screen.height / 2);
				// Renders Layer 1, then the player, then Layer 2, then messages if there any
			renderLayer(screen, xOffset, yOffset, layer1, g);
			renderCharacters(g);
			renderLayer(screen, xOffset, yOffset, layer2, g);
			renderMessageBox(g);
		}
			
			// Creates a border
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		
			// Renders title screen if at title
		if (atTitle) {
			g.drawImage(titlescreen, 0, 0, getWidth(), getHeight(), null);
				// Start bar will flash
			if (start_is_visible)
				g.drawImage(startbar, 15, 300, getWidth(), 12, null);
			if (atStartMenu)
				renderMenuBox(startMenu, g);
		}
		
		if (!atTitle)
			renderPauseMenu(g);
		
		g.dispose();
		bs.show();
			// Synchronize with the display refresh rate.
		Toolkit.getDefaultToolkit().sync();
		
	}
	////////////////////////

	// Save Method //
	public void saveGame() {
		BufferedWriter bufferedWriter = null;
		try {
				// Deletes old save file
			File oldsave = new File("profile.sav");
			oldsave.delete();
				// Creates new save file
			File newsave = new File("profile.sav");
			newsave.createNewFile();
				// Stores level number and player position (will include more when game states are added)
			bufferedWriter = new BufferedWriter(new FileWriter("profile.sav"));
			bufferedWriter.write("" + levelNum);
			bufferedWriter.newLine();
			bufferedWriter.write("" + player.xPos);
			bufferedWriter.newLine();
			bufferedWriter.write("" + player.yPos);
			bufferedWriter.newLine();
			bufferedWriter.write(timer.getTime());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedWriter != null) {
					bufferedWriter.flush();
					bufferedWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/////////////////
	
	// Load Method //
	public void loadGame() {
			// Loads most recent save file
		File file = new File("profile.sav");
		BufferedReader br = null;
		try {
				// Buffered reader will set instance variables levelNum and player position to save file's record
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			this.levelNum = Integer.parseInt(br.readLine());
			player.xPos1 = Integer.parseInt(br.readLine());
			player.yPos1 = Integer.parseInt(br.readLine());
			timer = Timer.parseTimer(br.readLine());
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/////////////////
	
	// Main Method //
	public static void main(String[] args) {
			// Sets up and starts a new game
		Game game = new Game();
			game.start();
	}
	/////////////////
}