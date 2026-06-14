package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

	public enum GameState {// To track what state the game is in
		MAIN_MENU, MODE_SELECT, LEVEL_SELECT, PLAYING, PAUSED, DEATH_SCREEN, WIN_SCREEN, NEXT_LEVEL
	}

	private GameState currentState;
	private Image backgroundImage;
	private Level levelManager;// controls everything to do with the levels
	private Timer gameClock;// for the loop
	private boolean isSinglePlayer, controllingPlayerOne, greenGemsRemaining, purpleGemsRemaining, switchIsActive;
	// In order to track movement (to get rid of delay)
	private boolean wPressed, aPressed, dPressed, upPressed, leftPressed, rightPressed;

	public GamePanel() {
		Wall.loadImages();
		Platform.loadImages();
		Button.loadImages();
		Lever.loadImages();
		Door.loadImages();
		Gem.loadImages();
		Gas.loadImages();
		this.setPreferredSize(new Dimension(800, 600));// 800x600 screen
		try {
			// Make sure the file name matches exactly where it is in your project!
			backgroundImage = ImageIO.read(new File("images/lab_bg.png")); 
		} catch (IOException e) {
			System.out.println("Could not load background image!");
			e.printStackTrace();
		}
		
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

	// Drawing different menus (AI CREATED THIS)

	private void drawMainMenu(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight()); // Pure black background

		// --- RETRO TITLE ---
		Font titleFont = new Font("Monospaced", Font.BOLD, 50);
		g.setFont(titleFont);

		// Get FontMetrics to help us perfectly center the text!
		FontMetrics metrics = g.getFontMetrics(titleFont);

		// LINE 1: "SOLO DUO:"
		String line1 = "SOLO DUO:";
		int x1 = (this.getWidth() - metrics.stringWidth(line1)) / 2; // Centers it mathematically
		g.setColor(Color.DARK_GRAY);
		g.drawString(line1, x1 + 5, 125); // Shadow
		g.setColor(Color.WHITE);
		g.drawString(line1, x1, 120); // Main Text

		// LINE 2: "ALKALINITY & TOXICITY" (Split into 3 pieces)
		String word1 = "ALKALINITY ";
		String word2 = "& ";
		String word3 = "TOXICITY";

		// Calculate the total width to find our starting point
		int totalWidth = metrics.stringWidth(word1 + word2 + word3);
		int startX = (this.getWidth() - totalWidth) / 2;
		int y2 = 220; // Lower down the screen

		// Draw the shadow for all three words at once
		g.setColor(Color.DARK_GRAY);
		g.drawString(word1 + word2 + word3, startX + 5, y2 + 5);

		// Draw "ALKALINITY" in Purple
		g.setColor(new Color(150, 0, 255));
		g.drawString(word1, startX, y2);

		// Draw "&" in White
		int currentX = startX + metrics.stringWidth(word1); // Move X to the right
		g.setColor(Color.WHITE);
		g.drawString(word2, currentX, y2);

		// Draw "TOXICITY" in Green
		currentX += metrics.stringWidth(word2); // Move X to the right again
		g.setColor(Color.GREEN);
		g.drawString(word3, currentX, y2);

		// --- BLINKING PROMPT EFFECT ---
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

		// --- Box 1 ---
		g.setColor(Color.DARK_GRAY);
		g.fillRect(100, 260, 150, 150); // Deep shadow
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(90, 250, 150, 150); // Main Button block
		g.setColor(Color.WHITE);
		g.drawRect(90, 250, 150, 150);
		g.drawRect(92, 252, 146, 146); // Double outline

		g.setColor(Color.BLACK);
		g.drawString("LEVEL 1", 125, 310);
		g.setColor(Color.DARK_GRAY);
		g.drawString("[Press 1]", 110, 350);

		// --- Box 2 ---
		g.setColor(Color.DARK_GRAY);
		g.fillRect(335, 260, 150, 150); // Deep shadow
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(325, 250, 150, 150); // Main Button block
		g.setColor(Color.WHITE);
		g.drawRect(325, 250, 150, 150);
		g.drawRect(327, 252, 146, 146); // Double outline

		g.setColor(Color.BLACK);
		g.drawString("LEVEL 2", 360, 310);
		g.setColor(Color.DARK_GRAY);
		g.drawString("[Press 2]", 345, 350);

		// --- Box 3 ---
		g.setColor(Color.DARK_GRAY);
		g.fillRect(570, 260, 150, 150); // Deep shadow
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(560, 250, 150, 150); // Main Button block
		g.setColor(Color.WHITE);
		g.drawRect(560, 250, 150, 150);
		g.drawRect(562, 252, 146, 146); // Double outline

		g.setColor(Color.BLACK);
		g.drawString("LEVEL 3", 595, 310);
		g.setColor(Color.DARK_GRAY);
		g.drawString("[Press 3]", 580, 350);
	}

	private void drawPauseScreen(Graphics g) {
		// 1. Draw a semi-transparent black overlay
		// The '180' is the alpha (transparency) level! 0 is invisible, 255 is solid.
		g.setColor(new Color(0, 0, 0, 180));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		// 2. Draw the PAUSED Title
		g.setFont(new Font("Monospaced", Font.BOLD, 60));
		g.setColor(Color.WHITE);
		FontMetrics titleMetrics = g.getFontMetrics();
		String title = "PAUSED";
		int titleX = (this.getWidth() - titleMetrics.stringWidth(title)) / 2;
		g.drawString(title, titleX, 250);

		// 3. Draw the interactive prompts
		g.setFont(new Font("Monospaced", Font.BOLD, 25));
		g.setColor(Color.LIGHT_GRAY);
		FontMetrics promptMetrics = g.getFontMetrics();

		String resumeStr = "> Press ESC to Resume <";
		String menuStr = "> Press M for Main Menu <";

		int resumeX = (this.getWidth() - promptMetrics.stringWidth(resumeStr)) / 2;
		int menuX = (this.getWidth() - promptMetrics.stringWidth(menuStr)) / 2;

		// Spaced out nicely below the title
		g.drawString(resumeStr, resumeX, 330);
		g.drawString(menuStr, menuX, 380);
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
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
		}else {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
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
		Character p1 = levelManager.getPlayer1();
		Character p2 = levelManager.getPlayer2();
		Character activePlayer = null;

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
				return;// so the characters don't move when it is paused
			}
		}
		if (currentState == GameState.PAUSED) {
			if (key == KeyEvent.VK_ESCAPE) {
				currentState = GameState.PLAYING;
			}
			if (key == KeyEvent.VK_M) {
				resetLevel();
				currentState = GameState.MAIN_MENU;
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
		if (currentState == GameState.LEVEL_SELECT) {
			if (key == KeyEvent.VK_1) {
				levelManager.setCurrentLevelIndex(0);
				levelManager.loadCurrentLevel();
				currentState = GameState.PLAYING;
			} else if (key == KeyEvent.VK_2) {
				levelManager.setCurrentLevelIndex(1);
				levelManager.loadCurrentLevel();
				currentState = GameState.PLAYING;
			} else if (key == KeyEvent.VK_3) {
				levelManager.setCurrentLevelIndex(2);
				levelManager.loadCurrentLevel();
				currentState = GameState.PLAYING;
			}
			return;
		}
		if (currentState == GameState.DEATH_SCREEN) {
			if (key == KeyEvent.VK_R) {
				resetLevel();
				currentState = GameState.PLAYING;
			}
			return;
		}
		if (currentState == GameState.NEXT_LEVEL) {
			if (key == KeyEvent.VK_N) {
				leftPressed = false;
				rightPressed = false;
				aPressed = false;
				dPressed = false;
				levelManager.nextLevel();
				levelManager.loadCurrentLevel();
				currentState = GameState.PLAYING;
			}
			return;
		}
		if (currentState == GameState.WIN_SCREEN) {
			if (key == KeyEvent.VK_M) {
				currentState = GameState.MAIN_MENU;
			}
			return;
		}

		if (key == KeyEvent.VK_SPACE) {
			if (this.isSinglePlayer) {

				// Active Player Indicator
				controllingPlayerOne = !controllingPlayerOne;
			}
			return;
		}
		if (currentState == GameState.PLAYING) {
			// turn on movement input trackers when the button is pressed
			if (key == KeyEvent.VK_A)
				aPressed = true;
			if (key == KeyEvent.VK_D)
				dPressed = true;
			if (key == KeyEvent.VK_LEFT)
				leftPressed = true;
			if (key == KeyEvent.VK_RIGHT)
				rightPressed = true;

			if (this.isSinglePlayer) {
				if (this.controllingPlayerOne) {
					activePlayer = p1;
				} else {
					activePlayer = p2;
				}
				// Single Player jumping
				if (activePlayer != null && (key == KeyEvent.VK_W || key == KeyEvent.VK_UP)) {
					activePlayer.jump();
				}
			} else {
				// Two Player jumping
				if (p1 != null && key == KeyEvent.VK_W) {
					p1.jump();
				}
				if (p2 != null && key == KeyEvent.VK_UP) {
					p2.jump();
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		// turn off movement input trackers when key is released
		if (key == KeyEvent.VK_A)
			aPressed = false;
		if (key == KeyEvent.VK_D)
			dPressed = false;

		if (key == KeyEvent.VK_LEFT)
			leftPressed = false;
		if (key == KeyEvent.VK_RIGHT)
			rightPressed = false;
	}

	private void processMovement(Character p1, Character p2) {
		Character activePlayer = null;
		Character inactivePlayer = null;
		// movement inputs for single player
		if (this.isSinglePlayer) {
			if (this.controllingPlayerOne) {
				activePlayer = p1;
				inactivePlayer = p2;
			} else {
				activePlayer = p2;
				inactivePlayer = p1;
			}
			if (activePlayer != null) {
				if ((this.aPressed || this.leftPressed) && !(dPressed || rightPressed)) {
					activePlayer.moveLeft();
				} else if ((this.dPressed || this.rightPressed) && !(aPressed || leftPressed))
					activePlayer.moveRight();
				else {
					activePlayer.stop();
				}
			}
			if (inactivePlayer != null) {
				inactivePlayer.stop();
			}

		} else {
			// movement input for two player
			if (p1 != null) {
				if (aPressed && !dPressed)
					p1.moveLeft();
				else if (dPressed && !aPressed)
					p1.moveRight();
				else
					p1.stop();
			}

			if (p2 != null) {
				if (leftPressed && !rightPressed)
					p2.moveLeft();
				else if (rightPressed && !leftPressed)
					p2.moveRight();
				else
					p2.stop();
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
			processMovement(p1, p2);

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
		aPressed = false;
		dPressed = false;
	}

}