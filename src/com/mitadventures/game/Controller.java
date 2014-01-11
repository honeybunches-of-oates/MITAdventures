 /* Controller Class
  * Uses a KeyListener
  * Has a Key class for each individual button used in game
  * Toggle Key Method makes Key object's boolean "pressed" true when its corresponding key is pressed
  * 
  * up = up or w
  * down = down or s
  * right = right or d
  * left = left or a
  * 
  * a button = space
  * b button = alt or control
  * 
  * start = enter
  * select = shift
  */

package com.mitadventures.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener {
	
	public Game game;
	
	// Controller Constructor //
	public Controller(Game game) {
		game.addKeyListener(this);
		this.game = game;
	}
	////////////////////////////
	
	// Key Class //
	public class Key {
		private boolean pressed = false;

		public boolean isPressed() {
			return pressed;
		}

		public void toggle(boolean isPressed) {
			pressed = isPressed;
		}
	}
	///////////////
	
	// Key Object Declarations //
	public Key up = new Key();
	public Key down = new Key();
	public Key right = new Key();
	public Key left = new Key();
	public Key a_button = new Key();
	public Key b_button = new Key();
	public Key start = new Key();
	public Key select = new Key();
	public long lastPressProcessed = 0;
	public boolean keyPressed = false;
	//////////////////////////////
	
	// Key Pressed Method //
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
		if (keyPressed == false)
			lastPressProcessed = System.currentTimeMillis();
		keyPressed = true;
	}
	////////////////////////
	
	// Key Released Method //
	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
		keyPressed = false;
	}
	/////////////////////////

	// Key Typed Method //
	public void keyTyped(KeyEvent arg0) {
	}
	//////////////////////
	
	// Toggle Key Method //
	public void toggleKey(int keyCode, boolean isPressed) {
		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
			up.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
			down.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
			right.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A)  {
			left.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_SPACE) {
			a_button.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_ALT || keyCode == KeyEvent.VK_CONTROL) {
			b_button.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_ENTER) {
			start.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_SHIFT) {
			select.toggle(isPressed);
		}
	}
	///////////////////////
}
