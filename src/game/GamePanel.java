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
		// update the characters' positions
		if (p1 != null)
			p1.update();
		if (p2 != null)
			p2.update();

		// check every tile to see if character is colliding with them
		ArrayList<Tile> tiles = levelManager.getActiveTiles();

		for (Tile t : tiles) {
			// We only want to push back against walls right now
			if (t instanceof Wall) {
				// Player 1 Wall Collisions
				if (p1 != null && t.isColliding(p1))
					resolveWallCollision(p1, t);

				// Player 2 Wall Collisions
				if (p2 != null && t.isColliding(p2))
					resolveWallCollision(p2, t);
			}
		}

		repaint();
	}

	private void resolveWallCollision(Character p, Tile t) {
		// get the hitboxes of the character and tile
		java.awt.Rectangle pBounds = p.getBounds();
		java.awt.Rectangle tBounds = t.getBounds();

		// Find the rectangle of the overlap zone
		java.awt.Rectangle intersection = pBounds.intersection(tBounds);
		// if the character intersected the tile on it's left/right side
		if (intersection.width < intersection.height) {// intersection is skinnier than tall
			if (p.getBounds().x < tBounds.x) {// if character is on the left
				p.setX(p.getX() - intersection.width);// we push them to the tile's left edge
			} else {// if character is on right
				p.setX(p.getX() + intersection.width);// we push them to the tile's right edge
			}
		} else {// if the character intersected the tile on it's top/bottom side (wider than
				// tall)
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
	}

}
