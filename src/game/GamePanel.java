package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
	private Level levelManager;// controls everything to do with the levels
	private Timer gameClock;// for the loop
	private boolean isSinglePlayer, controllingPlayerOne, leftPressed, rightPressed;

	public GamePanel() {
		this.setPreferredSize(new Dimension(800, 600));// 800x600 screen
		this.setBackground(Color.black);

		this.isSinglePlayer = true;
		controllingPlayerOne = true;// tracks in one-player mode what character is being controlled

		// Track if user wants to move left or right
		leftPressed = false;
		rightPressed = false;

		// create level manager
		levelManager = new Level();
		levelManager.loadCurrentLevel();

		// Make this take in input from keyboard
		this.setFocusable(true);
		this.addKeyListener(this);

		// Set up timer (60 fps)
		gameClock = new Timer(16, this);
		gameClock.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// loop through all active tiles and make them draw themselves
		ArrayList<Tile> tiles = levelManager.getActiveTiles();

		for (Tile t : tiles) {
			t.draw(g);
		}

		// Draw the characters
		Character p1 = levelManager.getPlayer1();
		Character p2 = levelManager.getPlayer2();

		if (p1 != null)
			p1.draw(g);
		if (p2 != null)
			p2.draw(g);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		Character activePlayer = null;
		Character p1 = levelManager.getPlayer1();
		Character p2 = levelManager.getPlayer2();

		if (key == KeyEvent.VK_SPACE) {
			if (this.isSinglePlayer) {
				if (p1 != null)
					p1.stop();
				if (p2 != null)
					p2.stop();
				// Makes other character not automatically move when switching characters
				leftPressed = false;
				rightPressed = false;

				// Active Player Indicator
				controllingPlayerOne = !controllingPlayerOne;
				System.out.println("Switched characters! Controlling Player 1: " + this.controllingPlayerOne);
			}
			return;
		}

		// movement inputs for single player
		if (this.isSinglePlayer) {
			if (this.controllingPlayerOne) {
				activePlayer = p1;
			} else {
				activePlayer = p2;
			}
			if (activePlayer != null) {
				if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT)
					leftPressed = true;
				if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT)
					rightPressed = true;
				if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP)
					activePlayer.jump();
			}

		} else {
			// movement input for two player
			if (p1 != null) {
				if (key == KeyEvent.VK_A)
					p1.moveLeft();
				if (key == KeyEvent.VK_D)
					p1.moveRight();
				if (key == KeyEvent.VK_W)
					p1.jump();
			}

			if (p2 != null) {
				if (key == KeyEvent.VK_LEFT)
					p2.moveLeft();
				if (key == KeyEvent.VK_RIGHT)
					p2.moveRight();
				if (key == KeyEvent.VK_UP)
					p2.jump();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		Character p1 = levelManager.getPlayer1();
		Character p2 = levelManager.getPlayer2();

		// Single Player movement inputs
		if (this.isSinglePlayer) {
			if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT)
				leftPressed = false;
			if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT)
				rightPressed = false;
		}

		// Two Player movement inputs
		// Stop Player 1 if they let go of key
		else {
			if (p1 != null) {
				if (key == KeyEvent.VK_A || key == KeyEvent.VK_D) {
					p1.stop();
				}
			}

			// Stop Player 2 if they let go of key
			if (p2 != null) {
				if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
					p2.stop();
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Character p1 = levelManager.getPlayer1();
		Character p2 = levelManager.getPlayer2();
		Character activePlayer = null;
		// Character movement if single player (based on boolean horizontal movement
		// checkers)
		if (this.isSinglePlayer) {
			if (this.controllingPlayerOne) {
				activePlayer = p1;
			} else {
				activePlayer = p2;
			}
			if (activePlayer != null) {
				if (leftPressed && !rightPressed) {
					activePlayer.moveLeft();
				} else if (rightPressed && !leftPressed) {
					activePlayer.moveRight();
				} else {
					activePlayer.stop();
				}
			}
		}
		;

		// check every tile to see if character is colliding with them
		ArrayList<Tile> tiles = levelManager.getActiveTiles();

		// update the characters' positions

		// Player 1 Wall Collisions
		if (p1 != null) {
			p1.updateX();// move horizontally

			for (Tile t : tiles) {// check collisions
				if (t instanceof Wall || (t instanceof Door && !((Door) t).isOpen())) {
					if (p1 != null && t.isColliding(p1))
						resolveHorizontalCollision(p1, t);
				}
			}
		}
		if (p1 != null) {
			p1.updateY();// move vertically
			for (Tile t : tiles) {// check collisions
				if (t instanceof Wall || (t instanceof Door && !((Door) t).isOpen())) {
					if (p1 != null && t.isColliding(p1))
						resolveVerticalCollision(p1, t);
				}
			}
		}

		// Player 2 Wall Collisions
		if (p2 != null) {
			p2.updateX();// move horizontally

			for (Tile t : tiles) {// check collisions
				if (t instanceof Wall || (t instanceof Door && !((Door) t).isOpen())) {
					if (t.isColliding(p2))
						resolveHorizontalCollision(p2, t);
				}
			}
			if (p2 != null)
				p2.updateY();// move vertically
			for (Tile t : tiles) {// check wall collisions
				if (t instanceof Wall || (t instanceof Door && !((Door) t).isOpen())) {
					if (p2 != null && t.isColliding(p2))
						resolveVerticalCollision(p2, t);
				}
			}
		}

		// Button, Gem, and Lava collisions

		// Reset all buttons at the start of the frame processing
		for (Tile t : tiles) {
			if (t instanceof Button) {
				((Button) t).release();
			}
		}

		// Player 1 Gem Collisions
		for (int i = tiles.size() - 1; i >= 0; i--) {
			Tile t = tiles.get(i);

			if (p1 != null && t instanceof Gem && t.isColliding(p1)) {
				Gem g = (Gem) t;
				if ("Red".equals(g.getColor())) {
					// Collect the gem
					tiles.remove(i);
					System.out.println("Player 1 collected a red gem");
				}
			}

			// Player 1 Lava Collisions

			if (p1 != null && t instanceof Lava && t.isColliding(p1)) {
				Lava l = (Lava) t;
				if ("Blue".equals(l.getColor())) {
					// kill player 1
					System.out.println("Player1 jumped into lava");
					resetLevel();
					return;// to stop processing the frame and restart
				}
			}

			// Player 1 Button Collisions
			if (t instanceof Button) {
				Button b = (Button) t;
				if ((p1 != null && b.isColliding(p1)) || (p2 != null && b.isColliding(p2))) {
					b.interact();
				}
			}

			// Player 2 Gem Collisions

			if (p2 != null && t instanceof Gem && t.isColliding(p2)) {
				Gem g = (Gem) t;
				if ("Blue".equals(g.getColor())) {
					// Collect the gem
					tiles.remove(i);
					System.out.println("Player 2 collected a blue gem");
				}
			}

			// Player 2 Lava Collisions

			if (p2 != null && t instanceof Lava && t.isColliding(p2)) {
				Lava l = (Lava) t;
				if ("Red".equals(l.getColor())) {
					// kill player 1
					System.out.println("Player2 jumped into lava");
					resetLevel();
					return;// to stop processing the frame and restart
				}
			}

		}

		repaint();

	}

	private void resolveHorizontalCollision(Character p, Tile t) {
		// get the hitboxes of the character and tile
		java.awt.Rectangle pBounds = p.getBounds();
		java.awt.Rectangle tBounds = t.getBounds();

		// Find the rectangle of the overlap zone
		java.awt.Rectangle intersection = pBounds.intersection(tBounds);

		if (intersection.width <= 0 || intersection.height <= 0)// make sure it isn't just touching without overlapping
			return;

		// if the character intersected the tile on it's left/right side
		if (p.getBounds().x < tBounds.x) {// if character is on the left
			p.setX(p.getX() - intersection.width);// we push them to the tile's left edge
		} else {// if character is on right
			p.setX(p.getX() + intersection.width);// we push them to the tile's right edge
		}
		p.stop();
	}

	private void resolveVerticalCollision(Character p, Tile t) {
		// get the hitboxes of the character and tile
		java.awt.Rectangle pBounds = p.getBounds();
		java.awt.Rectangle tBounds = t.getBounds();

		// Find the rectangle of the overlap zone
		java.awt.Rectangle intersection = pBounds.intersection(tBounds);

		if (intersection.width <= 0 || intersection.height <= 0)// make sure it isn't just touching without overlapping
			return;

		if (pBounds.y < tBounds.y) {// if character is above tile
			p.setY(p.getY() - intersection.height);// we push the character back up
			p.setYVel(0);// so they stop moving vertically
			p.setIsOnFloor(true);// so they don't fall through the floor
		} else {// if the character is under tile
			p.setY(p.getY() + intersection.height);// we push them to tile's bottom edge
			p.setYVel(0);// make them stop moving up
			// they aren't on the ground, so isOnFloor stays false
		}
	}

	private void resetLevel() {
		System.out.println("You died... Try again!");
		levelManager.loadCurrentLevel();

		// Reset key presses so characters spawn standing still
		leftPressed = false;
		rightPressed = false;
	}

}
