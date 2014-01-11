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

import com.mitadventures.game.characters.NPC;
import com.mitadventures.game.characters.Player;
import com.mitadventures.game.graphics.MessageBox;
import com.mitadventures.game.graphics.Screen;
import com.mitadventures.game.graphics.SpriteSheet;
import com.mitadventures.game.level.Map;
import com.mitadventures.game.level.WarpPoint;
import com.mitadventures.game.Controller;
import com.mitadventures.game.graphics.Fonts;
import com.mitadventures.game.level.Level;

public class Game extends Canvas implements Runnable {

	// Game Variables //
	private static final long serialVersionUID = 1L;

		// Size of Game Window
	public static final int WIDTH = 208;
	public static final int HEIGHT = 176;
	public static final int SCALE = 3;
	public static final String NAME = "Game";

	private JFrame frame;

		// Refers to running program
	public boolean running = false;
	public int tickCount = 0;
	
		// Game Images
	private Image titlescreen;
	private Image startbar;
	private Image messageBox;
	
	private boolean atTitle = true;
	private boolean playing = false;
	private boolean start_is_visible = false;
	
	
	public Screen screen;
	public Player player;
	public Controller controller;
	
	public Level level;
	public int levelNum = 1;
	public Map layer1;
	public Map layer2;
	
	public List<WarpPoint> warpPoints = new ArrayList<WarpPoint>();
	
	private boolean tilechange;
	private int xTilePosGrass = 0;
	private int yTilePosGrass = 0;
	
	public boolean a_is_hit = false;
	public boolean b_is_hit = false;
	public boolean start_is_hit = false;
	public boolean select_is_hit = false;
	
	public boolean a_is_released = true;
	public boolean b_is_released = true;
	public boolean start_is_released = true;
	public boolean select_is_released = true;
	
	public boolean messageBoxOpen = false;
	
	private int entityIdentity;
	private boolean freeNPC;
	
	private List<NPC> NPCs = new ArrayList<NPC>();
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
			// Sets up Tick function, will report ticks per second
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;

		int ticks = 0;
		int frames = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		
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
			
			if (atTitle & start_is_hit) {
				atTitle = false;
				playing = true;
				start_is_hit = false;
			}
				
			/*
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
		tickCount++;
		level.tick();
		actionHandler();
	}
	/////////////////
	
	// Initialize Method //
	public void init() {
		createLevel(levelNum);
		controller = new Controller(this);
		player = new Player(this, level, 12 * 16, 31 * 16, controller);
		level.addEntity(player);
		//addWarpPoint(new WarpPoint(this, 1, 10, 6, 2, 10, 5, "down"));
		//addWarpPoint(new WarpPoint(this, 2, 10, 5, 1, 10, 6, "down"));
		loadImages();
	}
	///////////////////////
	
	private Image linkDown;
	private Image linkDown1;
	private Image linkDown2;
	private Image linkLeft;
	private Image linkLeft1;
	private Image linkLeft2;
	private Image linkUp;
	private Image linkUp1;
	private Image linkUp2;
	private Image linkRight;
	private Image linkRight1;
	private Image linkRight2;
	
	private Image linkSprites[][] = new Image[4][3];
	
	private Image wolfMidnaDown;
	private Image wolfMidnaDown1;
	private Image wolfMidnaDown2;
	private Image wolfMidnaLeft;
	private Image wolfMidnaLeft1;
	private Image wolfMidnaLeft2;
	private Image wolfMidnaLeft3;
	private Image wolfMidnaLeft4;
	private Image wolfMidnaUp;
	private Image wolfMidnaUp1;
	private Image wolfMidnaUp2;
	private Image wolfMidnaRight;
	private Image wolfMidnaRight1;
	private Image wolfMidnaRight2;
	private Image wolfMidnaRight3;
	private Image wolfMidnaRight4;
	
	private Image wolfMidnaSprites[][] = new Image[4][5];
	
	private List<BufferedImage> tileset = new ArrayList<BufferedImage>();
	
	// Load Images Method //
	public void loadImages() {
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
		    linkDown = ImageIO.read(Game.class.getResourceAsStream("/Sprites/Link_Down.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			linkDown1 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/Link_Down1.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			linkDown2 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/Link_Down2.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
		    linkUp = ImageIO.read(Game.class.getResourceAsStream("/Sprites/Link_Up.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			linkUp1 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/Link_Up1.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			linkUp2 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/Link_Up2.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
		    linkLeft = ImageIO.read(Game.class.getResourceAsStream("/Sprites/Link_Left.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			linkLeft1 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/Link_Left1.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			linkLeft2 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/Link_Left2.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
		    linkRight = ImageIO.read(Game.class.getResourceAsStream("/Sprites/Link_Right.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			linkRight1 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/Link_Right1.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			linkRight2 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/Link_Right2.png"));
		} catch (IOException e) {
			System.out.println(e);
		} try {
			wolfMidnaDown = ImageIO.read(Game.class.getResourceAsStream("/Sprites/WolfMidna_Down.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			wolfMidnaDown1 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/WolfMidna_Down1.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			wolfMidnaDown2 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/WolfMidna_Down2.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			wolfMidnaLeft = ImageIO.read(Game.class.getResourceAsStream("/Sprites/WolfMidna_Left.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			wolfMidnaLeft1 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/WolfMidna_Left1.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			wolfMidnaLeft2 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/WolfMidna_Left2.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			wolfMidnaLeft3 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/WolfMidna_Left3.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			wolfMidnaLeft4 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/WolfMidna_Left4.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			wolfMidnaUp = ImageIO.read(Game.class.getResourceAsStream("/Sprites/WolfMidna_Up.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			wolfMidnaUp1 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/WolfMidna_Up1.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			wolfMidnaUp2 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/WolfMidna_Up2.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			wolfMidnaRight = ImageIO.read(Game.class.getResourceAsStream("/Sprites/WolfMidna_Right.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			wolfMidnaRight1 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/WolfMidna_Right1.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			wolfMidnaRight2 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/WolfMidna_Right2.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			wolfMidnaRight3 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/WolfMidna_Right3.png"));
		} catch (IOException e) {
			System.out.println(e);
		}  try {
			wolfMidnaRight4 = ImageIO.read(Game.class.getResourceAsStream("/Sprites/WolfMidna_Right4.png"));
		} catch (IOException e) {
			System.out.println(e);
		}
		
		BufferedImage spritesheet = screen.sheet.image;
		
		for (int y = 0; y < spritesheet.getHeight() / 16; y++) {
			for (int x = 0; x < spritesheet.getWidth() / 16; x++) {
				tileset.add(spritesheet.getSubimage(x * 16, y * 16, 16, 16));
			}
		}
		
		linkSprites[0][0] = linkUp;
		linkSprites[0][1] = linkUp1;
		linkSprites[0][2] = linkUp2;
		linkSprites[1][0] = linkLeft;
		linkSprites[1][1] = linkLeft1;
		linkSprites[1][2] = linkLeft2;
		linkSprites[2][0] = linkDown;
		linkSprites[2][1] = linkDown1;
		linkSprites[2][2] = linkDown2;
		linkSprites[3][0] = linkRight;
		linkSprites[3][1] = linkRight1;
		linkSprites[3][2] = linkRight2;
		
		wolfMidnaSprites[0][0] = wolfMidnaUp;
		wolfMidnaSprites[0][1] = wolfMidnaUp1;
		wolfMidnaSprites[0][2] = wolfMidnaUp2;
		wolfMidnaSprites[0][3] = wolfMidnaUp1;
		wolfMidnaSprites[0][4] = wolfMidnaUp2;
		wolfMidnaSprites[1][0] = wolfMidnaLeft;
		wolfMidnaSprites[1][1] = wolfMidnaLeft1;
		wolfMidnaSprites[1][2] = wolfMidnaLeft2;
		wolfMidnaSprites[1][3] = wolfMidnaLeft3;
		wolfMidnaSprites[1][4] = wolfMidnaLeft4;
		wolfMidnaSprites[2][0] = wolfMidnaDown;
		wolfMidnaSprites[2][1] = wolfMidnaDown1;
		wolfMidnaSprites[2][2] = wolfMidnaDown2;
		wolfMidnaSprites[2][3] = wolfMidnaDown1;
		wolfMidnaSprites[2][4] = wolfMidnaDown2;
		wolfMidnaSprites[3][0] = wolfMidnaRight;
		wolfMidnaSprites[3][1] = wolfMidnaRight1;
		wolfMidnaSprites[3][2] = wolfMidnaRight2;
		wolfMidnaSprites[3][3] = wolfMidnaRight3;
		wolfMidnaSprites[3][4] = wolfMidnaRight4;	
	}
	
	// Initialize when Loading Game Method //
	public void init_load() {
		screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/Sprite_Sheet.png"));
		createLevel(levelNum);
		controller = new Controller(this);
		player = new Player(this, level, player.xPos1, player.yPos1, controller);
		level.addEntity(player);
		//addWarpPoint(new WarpPoint(this, 1, 5, 1, 2, 10, 5, "down"));
		//addWarpPoint(new WarpPoint(this, 2, 10, 5, 1, 5, 1, "down"));
		loadImages();
	}
	//////////////////////
	
	// Add WarpPoint Method //
	public void addWarpPoint(WarpPoint warpPoint) {
		this.warpPoints.add(warpPoint);
	}
	//////////////////////////
	
	// Warp Test all Warp Points Method //
	public void warpTests() {
		for (WarpPoint w : warpPoints)
			w.warpTest();
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
		
	}
	//////////////////////////
	
	// Create Levels Method //
	public void createLevel(int levelNum) {
		switch (levelNum) {
		//Sacred Grove Temple
		case 1: layer1 = new Map("1   2   3   1   2   3   1   2   3   1   2   3   1   2   3   1   2   3   1   2   3   1   2   3   1     1/4   5   6   4   5   6   4   5   6   4   5   6   4   5   6   4   5   6   4   5   6   4   5   6   4     4/7   8   9   7   8   9   7   8   9   7   8   9   7   8   9   7   8   9   7   8   9   7   8   9   7     7/10  11  12  13  14  15  16  14  15  16  14  15  16  14  15  16  14  15  16  14  15  17  18  11  19   19/20  21  22  23  24  25  26  24  25  26  24  25  26  24  25  26  24  25  26  24  25  27  20  21  22   22/28  29  30  31  32  32  33  34  35  35  35  35  35  35  35  34  34  35  36  32  32  37  28  29  30   30/1   2   3   38  39  33  40  41  41  42  41  43  44  41  45  45  45  45  46  36  47  48  1   2   3     3/4   5   6   4   49  50  41  43  44  41  51  41  41  52  45  45  52  41  45  53  54  6   4   5   6     6/7   8   9   7   55  50  51  41  41  52  56  57  58  59  60  51  41  61  62  53  63  9   7   8   9     9/10  11  12  13  64  50  65  66  67  68  69  70  71  72  73  74  75  76  77  53  78  79  80  11  81   81/20  21  22  23  82  50  83  84  85  84  86  87  88  89  90  91  77  92  93  53  94  95  20  21  22   22/28  29  30  31  32  96  97  98  99  41  100 101 102 103 104 45  45  45  105 106 32  37  28  29  30   30/1   2   107 38  39  32  96  97  108 45  77  75  45  43  51  42  45  105 106 32  47  48  109 2   3     3/4   5   6   4   49  32  32  96  97  99  110 77  45  41  41  111 105 106 32  32  54  6   4   5   6     6/7   8   9   7   55  32  32  32  96  112 112 113 45  114 112 112 106 32  32  32  63  9   7   8   9     9/10  11  81  10  115 116 117 118 116 117 119 120 121 122 123 116 117 118 116 117 124 81  10  11  81   81/20  21  22  20  21  22  20  21  125 126 127 128 45  129 130 131 132 21  22  20  21  22  20  21  22   22/28  29  30  28  133 134 135 135 136 137 138 139 45  140 141 142 143 135 135 144 145 30  28  29  30   30/1   146 147 148 149 150 151 152 151 153 154 155 45  156 157 158 151 159 151 160 161 147 148 162 3     3/4   163 164 165 149 166 151 151 151 167 168 169 170 171 168 151 151 151 151 172 161 164 165 173 6     6/7   55  32  33  149 166 153 153 153 153 168 174 175 176 168 153 153 153 153 172 161 36  32  63  9     9/3   177 39  50  149 178 179 180 181 179 168 182 183 184 168 180 181 179 179 185 161 53  47  186 1     1/6   4   49  187 149 188 98  189 190 45  191 192 193 194 191 45  45  45  45  195 161 196 54  6   4     4/9   7   55  187 149 188 45  197 198 199 75  200 201 202 45  45  45  43  44  195 161 196 63  9   7     7/81  203 64  50  149 188 45  204 205 206 77  44  41  45  45  207 208 209 41  195 161 53  78  210 10   10/22  211 82  50  149 188 45  212 213 214 42  41  52  45  45  215 213 216 67  195 161 53  94  217 20   20/30  31  32  187 149 188 43  218 219 220 41  66  45  45  43  221 222 223 85  195 161 196 32  37  28   28/3   177 39  50  149 188 41  41  41  41  52  45  45  45  41  41  111 98  99  195 161 53  47  186 1     1/6   4   49  187 149 188 66  67  66  67  68  45  224 45  61  51  41  61  62  195 161 196 54  6   4     4/9   7   55  187 149 188 84  85  45  45  225 226 227 228 229 74  75  76  77  195 161 196 63  9   7     7/81  203 64  50  149 188 98  99  45  45  230 231 232 233 234 91  77  92  93  195 161 53  78  210 10   10/22  211 82  50  149 188 45  45  45  45  235 236 237 238 239 45  45  45  45  195 161 53  94  217 20   20/30  31  32  187 149 188 45  45  45  45  240 241 242 243 244 245 45  45  45  195 161 196 32  37  28   28/3   177 39  50  149 188 43  51  42  45  246 247 248 249 250 45  43  51  42  195 161 53  47  186 1     1/6   4   49  187 149 188 45  251 251 252 45  45  45  41  42  41  43  44  41  195 161 196 54  6   4     4/9   7   55  187 149 188 45  253 254 251 45  45  45  44  41  51  41  41  52  195 161 196 63  9   7     7/81  203 64  50  149 188 45  251 251 255 45  45  45  43  44  45  41  41  42  195 161 53  78  210 10   10/22  211 82  50  149 188 45  256 257 258 252 45  75  41  41  45  43  44  41  195 161 53  94  217 20   20/30  31  32  187 149 188 45  41  42  41  259 260 261 262 259 45  41  41  52  195 161 196 32  37  28   28/3   177 39  50  149 188 45  44  41  51  263 264 264 264 263 45  66  67  68  195 161 53  47  186 1     1/6   4   49  187 149 188 45  265 266 45  267 264 264 264 267 45  265 266 45  195 161 196 54  6   4     4/9   7   55  187 149 268 269 260 262 303 269 260 261 262 270 269 260 262 303 271 161 196 63  9   7     7/81  203 64  50  149 272 264 264 264 264 264 264 264 273 274 275 264 264 264 276 161 53  78  210 10   10/22  211 82  50  149 272 264 264 264 264 264 264 264 264 264 264 264 264 264 276 161 53  94  217 20   20/30  31  32  187 149 272 264 264 264 264 264 264 264 264 264 264 264 264 264 276 161 196 32  37  28   28/3   177 39  50  149 272 264 264 264 264 264 264 264 264 264 264 264 264 264 276 161 53  47  186 1     1/6   4   49  187 277 278 135 135 135 135 135 135 135 135 135 135 135 135 135 279 280 196 54  6   4     4/9   7   55  187 281 151 152 151 152 151 152 151 152 151 152 151 152 152 151 152 282 196 63  9   7     7/81  203 64  50  281 151 151 151 167 151 151 151 167 151 151 151 167 151 151 167 282 53  78  210 10   10/22  211 82  50  281 153 153 153 153 153 153 153 153 153 153 153 153 153 153 153 282 53  94  217 20   20/30  31  32  187 283 284 118 285 284 118 286 287 118 286 284 118 285 287 118 286 288 196 32  37  28   28/");				 
				layer2 = new Map("289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 290 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 291 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 292 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 293 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 294 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 295 296 289 289 289 289 289 289 295 296 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 297 298 299 289 289 289 289 289 297 298 299 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 300 301 302 289 289 289 289 289 300 301 302 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289 289/");
				level = new Level("map1", layer1, layer2);
			   	screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/Spritesheet(Sacred_Grove_Temple).png")); 	 
			  // Message Boxes
				MessageBox sign1 = new MessageBox(14, 42, "the guardians awaken to the smell of bubble tea.");
				level.addMessageBox(sign1);
					break;
					
		//Ordon Village Link's House
		case 2:	layer1 = new Map("0    0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0/"
							   + "0    0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0/"
							   + "0    0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0/"
							   + "0    0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0/"
							   + "0    0   0   0   0   9   9   9   30  49  50  51  52  31  9   9   9   9   9   9   9   9   9   9   9   9   9   0   0   0   0   0/"
							   + "0    0   0   0   0   1   2   2   30  41  42  43  44  31  2   2   2   2   2   2   2   2   2   2   2   2   3   0   0   0   0   0/"
							   + "0    0   0   0   0   4   5   5   26  28  37  28  28  29  5   5   5   5   5   5   5   5   5   5   5   5   6   0   0   0   0   0/"
							   + "0    0   0   0   0   4   5   5   17  9   9   9   16  16  17  5   5   5   17  9   9   9   16  16  17  5   6   0   0   0   0   0/"
							   + "0    0   0   0   0   4   9   9   17  9   17  9   9   16  17  9   9   9   17  9   17  9   9   16  17  9   6   0   0   0   0   0/"
							   + "0    0   0   0   0   4   17  9   17  9   9   17  16  16  9   17  17  9   17  9   9   17  16  16  9   17  6   0   0   0   0   0/"
							   + "0    0   0   0   0   4   5   9   9   17  9   9   16  17  17  5   5   9   9   17  9   9   16  17  17  5   6   0   0   0   0   0/"
							   + "0    0   0   0   0   4   5   5   9   9   20  21  16  16  5   5   5   5   9   9   20  21  16  16  5   5   6   0   0   0   0   0/"
							   + "0    0   0   0   0   7   2   2   2   2   24  25  2   2   2   2   2   2   2   2   24  25  2   2   2   2   8   0   0   0   0   0/"
							   + "0    0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0/"
							   + "0    0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0/"
							   + "0    0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0/"
							   + "0    0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0/");
		
				layer2 = new Map("-1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1/"
							   + "-1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1/"
							   + "-1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1/"
							   + "-1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1/"
							   
							   + "-1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1/"
							   + "-1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1/"
							   + "-1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1/" 
							   + "-1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  53  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1/"
							   + "-1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1/"
							   + "-1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1/"
							   + "-1  -1  -1  -1  -1  -1  -1  -1  -1  -1   18  19 -1  -1  -1  -1  -1  -1  -1  -1   18  19 -1  -1  -1  -1  -1  -1  -1  -1  -1  -1/"
							   + "-1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1/"
							   + "-1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1/"
							   
							   + "-1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1/"
							   + "-1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1/"
							   + "-1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1/"
							   + "-1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1  -1/");
				level = new Level("map2", layer1, layer2);
				
				// Message Boxes
				MessageBox sign2 = new MessageBox(12, 7, "hello world!");
				level.addMessageBox(sign2);
				
				// NPCs
				NPCs.clear();
				NPC genericNPC = new NPC(this, level, 16 * 16, 9 * 16 - 8, "don't take the blue acid!");
				NPCs.add(genericNPC);
				level.addEntity(genericNPC);
					break;
		}
	}
	//////////////////////////
	
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
	
	// Action Button Handler //
	public void actionHandler() {
		if (a_is_released) {
			if (controller.a_button.isPressed()) {
				a_is_hit = true;
				a_is_released = false;
			}
		} else if (controller.a_button.isPressed() == false) {
			a_is_released = true;
		}
		
		if (start_is_released) {
			if (controller.start.isPressed()) {
				start_is_hit = true;
				start_is_released = false;
			} else if (controller.start.isPressed() == false) {
				start_is_released = true;
			}
		}
		
		if (select_is_released) {
			if (controller.select.isPressed()) {
				select_is_hit = true;
				select_is_released = false;
			} else if (controller.select.isPressed() == false) {
				select_is_released = true;
			}
		}
		
		if (b_is_released) {
			if (controller.b_button.isPressed()) {
				b_is_hit = true;
				b_is_released = false;
			}
		} else if (controller.b_button.isPressed() == false) {
			b_is_released = true;
		}
	}
	///////////////////////////
	
	// Render Message Box Method //
	public void renderMessageBox() {
		boolean above = false;
		boolean below = false;
		boolean toRight = false;
		boolean toLeft = false;
		int index = 0;
		Fonts fonts = new Fonts();
		if (player.numSteps == 0) {
			if (a_is_hit & messageBoxOpen) {
				messageBoxOpen = false;
				a_is_hit = false;
				player.canMove = true;
				this.freeNPC = true;
				screen.renderMessageBox(false);
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
				if ((above || below || toRight || toLeft) & (a_is_hit || messageBoxOpen)) {
						screen.renderMessageBox(true);
						fonts.render(m.getMessage(), screen, screen.xOffset + 5, screen.yOffset + 84);
						messageBoxOpen = true;
						a_is_hit = false;
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
							if ((above || below || toRight || toLeft) & (a_is_hit || messageBoxOpen)) {
									screen.renderMessageBox(true);
									fonts.render(npc.getMessage(), screen, screen.xOffset + 5, screen.yOffset + 84);
									messageBoxOpen = true;
									a_is_hit = false;
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
								screen.renderMessageBox(false);
							}
						}
					if (a_is_hit) 
						a_is_hit = false;
					}
				}
			}
		}
	}
	///////////////////////////////
	
	// Render Player Method //
	public void renderPlayer(Graphics g) {
		int dir = player.direction - 1;
		int stage = player.spriteStage;
		g.drawImage(linkSprites[dir][stage], 6 * 16 * SCALE + 3, 4 * 16 * SCALE + 12, 14 * SCALE, 28 * SCALE, null);
	}
	//////////////////////////
	
	// Render Menu Method //
	public void renderMenu() {
		
	}
	////////////////////////////
	
	// Render Layer Method //
	public void renderLayer(Screen screen, int xOffset, int yOffset, Map layer, Graphics g) {
		if (xOffset > ((level.width << 4) - screen.width))
			xOffset = ((level.width << 4) - screen.width);
		if (xOffset < 0)
			xOffset = 0;
		if (yOffset > ((level.height << 4) - screen.height))
			yOffset = ((level.height << 4) - screen.height);
		if (yOffset < 0)
			yOffset = 0;

		screen.setOffset(xOffset, yOffset);
		
		for (int y = 0; y < level.height; y++) {
			for (int x = 0; x < level.width; x++) {
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
		
		Graphics g = bs.getDrawGraphics();
		
		int xOffset = player.xPos + 8 - (screen.width / 2);
		int yOffset = player.yPos + 8 - (screen.height / 2);
		
		if (playing) {
			
			renderLayer(screen, xOffset, yOffset, layer1, g);
			renderPlayer(g);
			renderLayer(screen, xOffset, yOffset, layer2, g);
			renderMessageBox();
			if (screen.renderMessageBox) {
				g.drawImage(messageBox, 0, 384, getWidth(), getHeight() - 384, null);
			}
		}
		
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		
		if (atTitle) {
			g.drawImage(titlescreen, 0, 0, getWidth(), getHeight(), null);
			if (start_is_visible)
				g.drawImage(startbar, 15, 300, getWidth(), 12, null);
		}
		
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
			File oldsave = new File("profile.sav");
			oldsave.delete();
			File newsave = new File("profile.sav");
			newsave.createNewFile();
			bufferedWriter = new BufferedWriter(new FileWriter("profile.sav"));
			bufferedWriter.write("" + levelNum);
			bufferedWriter.newLine();
			bufferedWriter.write("" + player.xPos);
			bufferedWriter.newLine();
			bufferedWriter.write("" + player.yPos);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (bufferedWriter != null) {
					bufferedWriter.flush();
					bufferedWriter.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	/////////////////
	
	// Load Method //
	public void loadGame() {
		File file = new File("profile.sav");
		BufferedReader br = null;
		try {
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
		Game game = new Game();
			game.start();
	}
	/////////////////
}
