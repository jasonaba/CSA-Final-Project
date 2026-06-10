package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Door implements Tile {
	private int x, y, width, height;
	private boolean isOpen;
	private String color; // Light or Dark
	private static Image darkClosed, darkOpened, lightClosed, lightOpened;

	// Constructor
	public Door(int x, int y, int width, int height, String color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		isOpen = false;

	}
	
	// Getters
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

	public String getColor() {
		return color;
	}

	public boolean isOpen() {
		return isOpen;
	}

	// Methods

	public static void loadImages() {
		try {
			darkClosed = ImageIO.read(new File("images/Dark_Door.png"));
			darkOpened = ImageIO.read(new File("images/Opened_Dark_Door.png"));
			System.out.println("Door images loaded successfully!");
			lightClosed = ImageIO.read(new File("images/Light_Door.png"));
			lightOpened = ImageIO.read(new File("images/Opened_Light_Door.png"));
			System.out.println("All Door images loaded successfully!");

		} catch (IOException e) {
			System.out.println("Could not load door images. Using backup colors.");
		}
	}

	@Override
	public boolean isColliding(Character c) {
		return this.getBounds().intersects(c.getBounds());
	}

	public void open() {
		isOpen = true;
	}

	@Override
	public void draw(Graphics g) {
		Image currentSprite = null;
		if (darkClosed != null && darkOpened != null && lightClosed != null && lightOpened != null) {

			if ("Dark".equals(color)) {
				if (isOpen) {
					currentSprite = darkOpened;
				} else {
					currentSprite = darkClosed;
				}
			}
			if ("Light".equals(color)) {
				if (isOpen) {
					currentSprite = lightOpened;
				} else {
					currentSprite = lightClosed;
				}
			}
			if (currentSprite != null) {
				g.drawImage(currentSprite, x, y, width, height, null);
				return;
			}
		} else {
			if (!isOpen) {
				if ("Dark".equals(color))
					g.setColor(Color.DARK_GRAY);
				else if ("Light".equals(color))
					g.setColor(Color.gray);
			} else {
				g.setColor(Color.black);
			}
			g.fillRect(x, y, width, height);
		}

	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
}
