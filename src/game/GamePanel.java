package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

	public enum GameState {// To track what state the game is in
		MAIN_MENU, MODE_SELECT, LEVEL_SELECT, PLAYING, PAUSED, DEATH_SCREEN, WIN_SCREEN, NEXT_LEVEL
	}

	private GameState currentState;

	private Level levelManager;// controls everything to do with the levels
	private Timer gameClock;// for the loop
	private boolean isSinglePlayer, controllingPlayerOne, leftPressed, rightPressed, greenGemsRemaining,
			purpleGemsRemaining, switchIsActive;

	public GamePanel() {
		Wall.loadImages();
		Platform.loadImages();
		Button.loadImages();
		Lever.loadImages();
		Door.loadImages();
		Gem.loadImages();
		Gas.loadImages();
		this.setPreferredSize(new Dimension(800, 600));// 800x600 screen
		this.setBackground(Color.LIGHT_GRAY);
		currentState = GameState.MAIN_MENU;

		this.isSinglePlayer = false;
		controllingPlayerOne = true;// tracks in one-player mode what character is being controlled

		// Track if user wants to move left or right
		leftPressed = false;
		rightPressed = false;

		// tracks if any gems are remaining
		this.greenGemsRemaining = false;
		this.purpleGemsRemaining = false;

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

		// Do different things based on the game's current state
		switch (currentState) {
		case DEATH_SCREEN:
			this.drawDeathScreen(g);
			break;
		case LEVEL_SELECT:
			this.drawLevelSelect(g);
			break;
		case MAIN_MENU:
			drawMainMenu(g);
			break;
		case MODE_SELECT:
			drawModeSelect(g);
			break;
		case PAUSED:
			this.drawPauseScreen(g);
			break;
		case PLAYING:
			drawGameplay(g);
			break;
		case WIN_SCREEN:
			this.drawWinScreen(g);
			break;
		case NEXT_LEVEL:
			this.drawNextLevel(g);
			;
		}

	}
	
	//Drawing different menus (AI CREATED THIS)

	private void drawMainMenu(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight()); // Pure black background

		// --- RETRO TITLE ---
		Font titleFont = new Font("Monospaced", Font.BOLD, 50);
		g.setFont(titleFont);
		
		// 1. Draw the drop shadows first (Offset by +5 on X and Y)
		g.setColor(new Color(0, 100, 0)); // Dark Green shadow
		g.drawString("Alkalinity & Toxicity", 85, 205);
		
		// 2. Draw the bright neon text on top
		g.setColor(Color.GREEN);
		g.drawString("Alkalinity & Toxicity", 80, 200);

		// --- BLINKING PROMPT EFFECT ---
		// We can make it look like a retro terminal prompt
		g.setFont(new Font("Monospaced", Font.BOLD, 30));
		g.setColor(Color.WHITE);
		g.drawString("> Press ENTER to Start <", 170, 400);
	}

	private void drawModeSelect(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		g.setFont(new Font("Monospaced", Font.BOLD, 40));
		g.setColor(Color.DARK_GRAY);
		g.drawString("SELECT GAME MODE", 205, 155); // Shadow
		g.setColor(Color.WHITE);
		g.drawString("SELECT GAME MODE", 200, 150);

		g.setFont(new Font("Monospaced", Font.BOLD, 25));
		
		// Single Player Retro Box
		g.setColor(Color.DARK_GRAY);
		g.drawRect(95, 255, 610, 50); // Shadow outline
		g.setColor(Color.GREEN);
		g.drawRect(90, 250, 610, 50); // Neon Box
		g.drawString("[1] Single Player (Swap: SPACE)", 110, 285);

		// Co-op Retro Box
		g.setColor(Color.DARK_GRAY);
		g.drawRect(95, 355, 610, 50); // Shadow outline
		g.setColor(new Color(180, 50, 255)); // Bright Purple
		g.drawRect(90, 350, 610, 50); // Neon Box
		g.drawString("[2] Co-op Mode (WASD & Arrows)", 110, 385);
	}

	private void drawLevelSelect(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		g.setFont(new Font("Monospaced", Font.BOLD, 40));
		g.setColor(Color.WHITE);
		g.drawString("- LEVEL SELECT -", 220, 100);

		g.setFont(new Font("Monospaced", Font.BOLD, 20));
		g.setColor(Color.GREEN);
		g.drawString("> Enter Level Number:", 250, 160);

		// Draw Level 1 Chunky Button
		g.setColor(Color.DARK_GRAY);
		g.fillRect(160, 260, 150, 150); // Deep shadow
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(150, 250, 150, 150); // Main Button block
		g.setColor(Color.WHITE);
		g.drawRect(150, 250, 150, 150); 
		g.drawRect(152, 252, 146, 146); // Double outline for thickness
		
		g.setColor(Color.BLACK);
		g.drawString("LEVEL 1", 185, 310);
		g.setColor(Color.DARK_GRAY);
		g.drawString("[Press 1]", 170, 350);

		// Draw Level 2 Chunky Button
		g.setColor(Color.DARK_GRAY);
		g.fillRect(510, 260, 150, 150); // Deep shadow
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(500, 250, 150, 150); // Main Button block
		g.setColor(Color.WHITE);
		g.drawRect(500, 250, 150, 150);
		g.drawRect(502, 252, 146, 146); // Double outline
		
		g.setColor(Color.BLACK);
		g.drawString("LEVEL 2", 535, 310);
		g.setColor(Color.DARK_GRAY);
		g.drawString("[Press 2]", 520, 350);
	}

	private void drawPauseScreen(Graphics g) {
		g.setColor(new Color(0, 0, 0, 180)); // Slightly darker fade
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setFont(new Font("Monospaced", Font.BOLD, 60));
		g.setColor(Color.BLACK);
		g.drawString("PAUSED", 295, 305); // Shadow
		g.setColor(Color.YELLOW); // Retro yellow for pause screens
		g.drawString("PAUSED", 290, 300);
		
		g.setFont(new Font("Monospaced", Font.BOLD, 25));
		g.setColor(Color.WHITE);
		g.drawString("> Press ESC to Resume <", 230, 400);
	}

	private void drawDeathScreen(Graphics g) {
		g.setColor(new Color(150, 0, 0, 180)); // Dark red fade
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		g.setFont(new Font("Monospaced", Font.BOLD, 70));
		g.setColor(Color.BLACK);
		g.drawString("SYSTEM FAILURE", 105, 255); // Shadow
		g.setColor(Color.WHITE);
		g.drawString("SYSTEM FAILURE", 100, 250);

		g.setFont(new Font("Monospaced", Font.BOLD, 30));
		g.setColor(Color.YELLOW);
		g.drawString("> Press 'R' to Reboot <", 190, 350);
	}

	private void drawNextLevel(Graphics g) {
		g.setColor(new Color(0, 100, 0, 180)); // Dark green fade
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		g.setFont(new Font("Monospaced", Font.BOLD, 55));
		g.setColor(Color.BLACK);
		g.drawString("SECTOR CLEARED", 165, 255); // Shadow
		g.setColor(Color.WHITE);
		g.drawString("SECTOR CLEARED", 160, 250);

		g.setFont(new Font("Monospaced", Font.BOLD, 30));
		g.setColor(Color.YELLOW);
		g.drawString("> Press 'N' to Advance <", 180, 350);
	}

	private void drawWinScreen(Graphics g) {
		g.setColor(new Color(0, 0, 0, 220)); // Very dark fade for the final win
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		g.setFont(new Font("Monospaced", Font.BOLD, 60));
		g.setColor(Color.DARK_GRAY);
		g.drawString("MISSION COMPLETE", 115, 255); // Shadow
		g.setColor(Color.GREEN);
		g.drawString("MISSION COMPLETE", 110, 250);

		g.setFont(new Font("Monospaced", Font.BOLD, 25));
		g.setColor(Color.WHITE);
		g.drawString("> Press 'M' to return to Main Menu <", 120, 350);
	}

    private void drawGameplay(Graphics g) {
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

		// Based on what state it is, what buttons can the user click?
		if (currentState == GameState.MAIN_MENU) {
			if (key == KeyEvent.VK_ENTER) {
				currentState = GameState.MODE_SELECT;
			}
			return;
		}
		if (currentState == GameState.PLAYING) {
			if (key == KeyEvent.VK_ESCAPE) {
				currentState = GameState.PAUSED;
				return;//so the characters don't move when it is paused
			}	
		}
		if (currentState == GameState.PAUSED) {
			if (key == KeyEvent.VK_ESCAPE) {
				currentState = GameState.PLAYING;
			}
			return;
		}
		if (currentState == GameState.MODE_SELECT) {
			if (key == KeyEvent.VK_1) {
				isSinglePlayer = true;
				// make it go to level picker from here
				currentState = GameState.LEVEL_SELECT;
			} else if (key == KeyEvent.VK_2) {
				isSinglePlayer = false;
				// make it go to level picker from here
				currentState = GameState.LEVEL_SELECT;
			}
			return;
		}
		if(currentState == GameState.LEVEL_SELECT) {
			if(key == KeyEvent.VK_1) {
				levelManager.setCurrentLevelIndex(0);
				levelManager.loadCurrentLevel();
				currentState = GameState.PLAYING;
			}else if(key == KeyEvent.VK_2) {
				levelManager.setCurrentLevelIndex(1);
				levelManager.loadCurrentLevel();
				currentState = GameState.PLAYING;
			}
			return;
		}
		if (currentState == GameState.DEATH_SCREEN) {
			if (key == KeyEvent.VK_R) {
				resetLevel();
				currentState = GameState.PLAYING;
			}return;
		}
		if (currentState == GameState.NEXT_LEVEL) {
			if (key == KeyEvent.VK_N) {
				leftPressed = false;
				rightPressed = false;
				levelManager.nextLevel();
				levelManager.loadCurrentLevel();
				currentState = GameState.PLAYING;
			}return;
		}
		if (currentState == GameState.WIN_SCREEN) {
			if (key == KeyEvent.VK_M) {
				currentState = GameState.MAIN_MENU;
			}
			return;
		}

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

		// All of this runs ONLY IF the user is playing
		if (currentState == GameState.PLAYING) {
			Character p1 = levelManager.getPlayer1();
			Character p2 = levelManager.getPlayer2();
			ArrayList<Tile> tiles = levelManager.getActiveTiles();

			// Character movement when single player
			singlePlayerMovement(p1, p2);

			// Apply Barrier Collisions
			updatePhysics(p1, tiles);
			updatePhysics(p2, tiles);

			// Prepare Map to Check for Collisions
			prepareMap(tiles);

			// Button, Gem, and Lava collisions

			// Run interaction loop to look at all tiles
			boolean playerDied = checkTileInteractions(p1, p2, tiles);
			if (playerDied) {
				currentState = GameState.DEATH_SCREEN;
				return;
			}
			if (this.getWidth() > 0 && this.getHeight() > 0) {

				// Individually check if p1 and p2 are OOB to make level restart
				boolean p1OutOfBounds = (p1 != null) && (p1.getX() > this.getWidth() || p1.getY() > this.getHeight());
				boolean p2OutOfBounds = (p2 != null) && (p2.getX() > this.getWidth() || p2.getY() > this.getHeight());

				if (p1OutOfBounds || p2OutOfBounds) {
					currentState = GameState.DEATH_SCREEN;
					return;
				}
			}
			// Check if the player(s) won
			winState(p1, p2, tiles);
		}

		repaint();

	}

	private void singlePlayerMovement(Character p1, Character p2) {
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
	}

	private void prepareMap(ArrayList<Tile> tiles) {
		// Reset the gem counter
		this.purpleGemsRemaining = false;
		this.greenGemsRemaining = false;

		// Reset all buttons at the start of the frame processing
		switchIsActive = false;
		for (Tile t : tiles) {
			if (t instanceof Button) {
				((Button) t).release();
			}
			// Check if there are any gems that are remanining
			if (t instanceof Gem) {
				Gem gem = (Gem) t;
				if ("Green".equals(gem.getColor())) {
					this.greenGemsRemaining = true;
				}
				if ("Purple".equals(gem.getColor())) {
					this.purpleGemsRemaining = true;
				}
			}

		}
	}

	/**
	 * 
	 * @param p1
	 * @param p2
	 * @return if a player died
	 */
	private boolean checkTileInteractions(Character p1, Character p2, ArrayList<Tile> tiles) {

		// Run interaction loop to look at all tiles

		// Player 1 Gem Collisions
		for (int i = tiles.size() - 1; i >= 0; i--) {
			Tile t = tiles.get(i);

			if (p1 != null && t instanceof Gem && t.isColliding(p1)) {
				Gem g = (Gem) t;
				if (p1.getColor().equals(g.getColor())) {
					// Collect the gem
					tiles.remove(i);
				}
			}

			// Player 1 Lava Collisions
			if (p1 != null && t instanceof Gas && t.isColliding(p1)) {
				Gas l = (Gas) t;
				if (!p1.getColor().equals(l.getColor())) {
					// kill player 1
					return true;// to stop processing the frame and restart
				}
			}

			// Player 2 Gem Collisions
			if (p2 != null && t instanceof Gem && t.isColliding(p2)) {
				Gem g = (Gem) t;
				if (p2.getColor().equals(g.getColor())) {
					// Collect the gem
					tiles.remove(i);
				}
			}

			// Player 2 Lava Collisions
			if (p2 != null && t instanceof Gas && t.isColliding(p2)) {
				Gas l = (Gas) t;
				if (!p2.getColor().equals(l.getColor())) {
					// kill player 1

					return true;// to stop processing the frame and restart
				}
			}
			// Lever collisions
			if (t instanceof Lever) {
				Lever lever = (Lever) t;
				lever.update(p1, p2);
				if (lever.switchedOn()) {
					switchIsActive = true;
				}
			}
			// Button Collisions
			if (t instanceof Button) {
				Button b = (Button) t;
				if ((p1 != null && b.isColliding(p1)) || (p2 != null && b.isColliding(p2))) {
					b.interact();
				}
				if (b.switchedOn()) {
					switchIsActive = true;
				}
			}
		}
		for (Tile t : tiles) {// in it's own loop so even if the tile is checked before the levers/buttons it
								// still gets updated
			// Platform Collisions
			if (t instanceof Platform) {
				Platform platform = (Platform) t;

				platform.setState(switchIsActive);
			}
		}

		return false;
	}

	private void updatePhysics(Character p, ArrayList<Tile> tiles) {
		// Horizontal
		if (p != null) {
			p.updateX();// move horizontally

			for (Tile t : tiles) {// check collisions
				barrierCollisionsX(p, t);
			}
			// Vertical
			p.updateY();// move vertically
			for (Tile t : tiles) {// check collisions
				barrierCollisionsY(p, t);
			}
		}
	}

	/**
	 * To check and fix horizontal barrier collisions with any character
	 * 
	 * @param p = Character
	 * @param t = Tile
	 */
	private void barrierCollisionsX(Character p, Tile t) {
		if (t instanceof Wall || (t instanceof Platform) || (t instanceof Door && !((Door) t).isOpen())) {
			if (p != null && t.isColliding(p))
				resolveHorizontalCollision(p, t);
		}
	}

	private void barrierCollisionsY(Character p, Tile t) {
		if (t instanceof Wall || (t instanceof Platform) || (t instanceof Door && !((Door) t).isOpen())) {
			if (p != null && t.isColliding(p))
				resolveVerticalCollision(p, t);
		}
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

	private void winState(Character p1, Character p2, ArrayList<Tile> tiles) {
		boolean p1AtDoor = false;
		boolean p2AtDoor = false;
		for (Tile t : tiles) {
			if (t instanceof Door) {
				Door door = (Door) t;
				// Open the door if the character collected enough gems
				if ("Purple".equals(door.getColor()) && !this.purpleGemsRemaining) {
					door.open();
				}
				if ("Green".equals(door.getColor()) && !this.greenGemsRemaining) {
					door.open();
				}

				if (door.isOpen()) {
					if ("Purple".equals(door.getColor()) && p1 != null && door.isColliding(p1)) {
						p1AtDoor = true;
					}
					if ("Green".equals(door.getColor()) && p2 != null && door.isColliding(p2)) {
						p2AtDoor = true;
					}
				}
			}
			if (p1AtDoor && p2AtDoor) {
				if (levelManager.getCurrentLevelIndex() == levelManager.getBlueprints().size() - 1) {
					currentState = GameState.WIN_SCREEN;
					return;
				}
				currentState = GameState.NEXT_LEVEL;
				return;
			}

		}
	}

	private void resetLevel() {
		levelManager.loadCurrentLevel();

		// Reset key presses so characters spawn standing still
		leftPressed = false;
		rightPressed = false;
	}

}