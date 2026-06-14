package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Platform implements Tile {
	private int x, y, width, height;
	private boolean isOn;
	private static Image platformSprite;

	// Constructor
	public Platform(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		isOn = false;
	}

	// Getters & Setters
	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public Rectangle getBounds() {

		return new Rectangle(x, y, width, height);
	}

	public boolean isOn() {
		return isOn;
	}

	public void setState(boolean onState) {
		isOn = onState;
	}
	// Methods

	@Override
	/**
	 * Checks if colliding with said character (c)
	 */
	public boolean isColliding(Character c) {
		if (!isOn) {
			return false;
		}
		return this.getBounds().intersects(c.getBounds());
	}

	/**
	 * Draw only if it is switched on
	 */
	@Override
	public void draw(Graphics g) {
		if (platformSprite != null) {
			// 3. Draw your artwork directly onto the grid space if it is switched on!
			if (isOn) {
				g.drawImage(platformSprite, x, y, width, height, null);
			} else {
				return;
			}
		} else {
			if (isOn) {
				g.setColor(Color.orange);
				g.fillRect(x, y, width, height);
			} else {
				return;
			}
		}
	}

	public static void loadImages() {
		try {
			platformSprite = ImageIO.read(new File("images/Platform.jpg"));
		} catch (IOException e) {
			System.out.println("Error: Could not load images/Wall.png. Using backup gray walls.");
		}
	}

}