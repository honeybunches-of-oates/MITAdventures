package com.mitadventures.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
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

public class Game extends Canvas implements Runnable {

	// Game Variables //
	private static final long serialVersionUID = 1L;

		// Size of Game Window
	public static final int WIDTH = 208;
	public static final int HEIGHT = 176;
	public static final int SCALE = 3;
	public static final String NAME = "MIT Adventures: Tim vs. The Bad Hacker";

	private JFrame frame;

		// Refers to running program
	public boolean running = false;
	public int tickCount = 0;
	
		// Game Images
	private Image titlescreen;
	private Image startbar;
	private Image messageBox;
	
		// Game States
	private boolean atTitle = true;
	private boolean playing = false;
	private boolean start_is_visible = false;
	
		// Game Objects
	public Screen screen;
	public Player player;
	public Controller controller;
	private Fonts fonts;
	
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
	
	private Image selector;
	
		// Array with images for 4 directions and 5 stages per direction
	private Image wolfMidnaSprites[][] = new Image[4][5];
	
		// Array for individual tiles from spritesheet
	private List<BufferedImage> tileset = new ArrayList<BufferedImage>();
	
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

		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		
			// Initializes game data
		init();

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
			
			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				System.out.println(frames + " fps, " + ticks + " tps");
				frames = 0;
				ticks = 0;
				if (start_is_visible)
					start_is_visible = false;
				else if (!start_is_visible)
					start_is_visible = true;
			}
			
			if (atTitle & controller.start.is_hit) {
				atTitle = false;
				playing = true;
				controller.start.is_hit = false;
			}
				
			/*
			 * SAVE METHOD, NOT BEING USED AT THE MOMENT
			if (start_is_hit) {
				saveGame();
				start_is_hit = false;
			}
			
			if (select_is_hit) {
				stop();
				this.frame.removeAll();
				saveGame();
			}
			
			if (b_is_hit) {
				loadGame();
				init_load();
				System.out.println("loading");
				b_is_hit = false;
			}
			*/
		}
	}
	////////////////
	
	// Tick Method //
	public void tick() {
			// Ticks game/level
		tickCount++;
		level.tick();
			// Checks for actions
		controller.check();
	}
	/////////////////
	
	// Initialize Method //
	public void init() {
			// Retrieves level map data
		createLevel(levelNum);
		controller = new Controller(this);
			// Creates and inserts player into map at tile (12, 31) *coordinates are multiplied by 16, the size of each tile*
		player = new Player(this, level, 12 * 16, 31 * 16, controller, "Link");
		level.addEntity(player);
			// Loads all sprite, game, and map images
		loadImages();
		fonts = new Fonts();
		pauseMenu = new MenuBar(new String[] {"Controls", "Stats", "Save", "Save and Quit", "Continue"}, font);
		startMenu = new MenuBar(new String[] {"New Game", "Load Game"}, font);
	}
	///////////////////////
	
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
		    messageBox = ImageIO.read(Game.class.getResourceAsStream("/MessageBox.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
		    startbar = ImageIO.read(Game.class.getResourceAsStream("/start.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
		    selector = ImageIO.read(Game.class.getResourceAsStream("/selarrow.png"));
		} catch (IOException e) {
			System.out.println(e);
		} try {
			fontsheet = ImageIO.read(Game.class.getResourceAsStream("/MacChicago.png"));
		} catch (IOException e) {
			System.out.println(e);
		}
		
			// Loading all tiles from spritesheet to array
		BufferedImage spritesheet = screen.sheet.image;
		
		for (int y = 0; y < spritesheet.getHeight() / 16; y++) {
			for (int x = 0; x < spritesheet.getWidth() / 16; x++) {
				tileset.add(spritesheet.getSubimage(x * 16, y * 16, 16, 16));
			}
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
	}
	
	// Initialize when Loading Game Method //
	public void init_load() {
			// Almost same as init() method, but used for loading saved game files, not starting new game files
		screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/Sprite_Sheet.png"));
		createLevel(levelNum);
		controller = new Controller(this);
		player = new Player(this, level, player.xPos1, player.yPos1, controller, "Link");
		level.addEntity(player);
		loadImages();
		fonts = new Fonts();
		pauseMenu = new MenuBar(new String[] {"Controls", "Stats", "Save", "Save and Quit", "Continue"}, font);
		startMenu = new MenuBar(new String[] {"Load Game", "New Game"}, font);
	}
	//////////////////////
	
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
			    player.setCurrentXTile(warpPoints.get(i).xTile2);
				player.setCurrentYTile(warpPoints.get(i).yTile2);
				player.setDirection(warpPoints.get(i).playerDir);
				warpPoints.get(i).warp = false;
				player.isWalking = true;
				createLevel(levelNum);
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
	
	// Animate Tiles Method //
	public void animateTiles() {
			// *TO BE COMPLETED*
	}
	//////////////////////////
	
	// Create Levels Method //
	public void createLevel(int levelNum) {
		NPCs.clear();
		String name;
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
			break;
					
		case 2: name = "Ordon_Village";
				setupMap(name);
			// Message Boxes
				MessageBox sign2 = new MessageBox(12, 7, "Hello World!");
				level.addMessageBox(sign2);
				
			// NPCs
				NPC genericNPC2 = new NPC(this, level, 16 * 16, 9 * 16 - 8, "Don't take the blue acid!", "Link");
				NPCs.add(genericNPC2);
				level.addEntity(genericNPC2);
			break;
		}
	}
	//////////////////////////
	
	// Setup Map Method //
	private void setupMap(String name) {
		String mapText1 = "";
		String mapText2 = "";
		try {
			File file = new File("/Users/Richard/Documents/workspace/MITAdventures/res/Spritesheet(" + name + ")_mapText.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			try {
				mapText1 = br.readLine();
				mapText2 = br.readLine();
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
	
	// Render and Tick Function of Pause Menu //
	public void renderPauseMenu(Graphics g) {
		if (controller.start.is_hit & menuOpen) {
			controller.start.is_hit = false;
			menuOpen = false;
			player.canMove = true;
		}
		if (controller.start.is_hit || menuOpen) {
			controller.start.is_hit = false;
			menuOpen = true;
			renderMenuBox(pauseMenu, g);
		}
	}
	////////////////////////////////////////////
	
	// Render Menu Box Method //
	public void renderMenuBox(MenuBar menu, Graphics g) {
		g.drawImage(messageBox, (WIDTH - (menu.width / 3)) / 2 * 3, (HEIGHT - menu.height) / 2 * 3 , menu.width, menu.height * 3, null);
		System.out.println("X: " + ((WIDTH - menu.width) / 2) + " Y: " + ((HEIGHT - menu.height) / 2));
		System.out.println(menu.width);
		for (int i = 0; i < menu.getOptions().length; i++)
			fonts.render(menu.getOptions()[i], screen, (WIDTH - menu.width / 3) / 2 + 39, (HEIGHT - menu.height) / 2 + (16 * i) + 5, WIDTH, 1, g, font);
		menuOpen = true;
		player.canMove = false;
		g.drawImage(selector, (WIDTH - menu.width / 3) / 2 * 3 - 16, (HEIGHT - menu.height) / 2 * 3 + (48 * menu.getSelected()) + 18, 16, 16, null);
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
						fonts.render(m.getMessage(), screen, 16, 132, WIDTH, 4, g, font);
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
									fonts.render(npc.getMessage(), screen, 16, 132, WIDTH, 4, g, font);
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
					controller.a_button.is_hit = false;
					}
				}
			}
		}
	}
	///////////////////////////////
	
	// Render Characters //
	public void renderCharacters(Graphics g) {
		for (Entity e : level.entities) {
			int dir = e.direction - 1;
			int stage  = e.spriteStage;
			g.drawImage(e.spriteSet.sprites[dir][stage], 6 * 16 * SCALE + 3, 4 * 16 * SCALE + 12, 14 * SCALE, 28 * SCALE, null);
		}		
	}
	
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
						g.drawImage(tileset.get(Map.getTile(x, y, layer) - 1), (x * 16 - xOffset)  * SCALE, (y * 16 - yOffset) * SCALE, 16 * SCALE, 16 * SCALE, null);
					}
				}
			}
		}
	}
	/////////////////////////////
	
	// Render Method //
	public void render() {
			// Set up buffer strategy, uses three image buffers
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
			// Sets up graphics based on buffer strategy
		Graphics g = bs.getDrawGraphics();
		
			// Sets up the offset for where the screen starts relative to the player in the context of the map
		int xOffset = player.xPos + 8 - (screen.width / 2);
		int yOffset = player.yPos + 8 - (screen.height / 2);
		
		if (playing) {
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
		}
		
		if (!atTitle)
			renderPauseMenu(g);
		
		g.dispose();
		bs.show();
			// Synchronize with the display refresh rate.
		Toolkit.getDefaultToolkit().sync();
		
	}
	///////////////////

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
