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
	private static Image purpleClosed, purpleOpened, greenClosed, greenOpened;

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
			purpleClosed = ImageIO.read(new File("images/Purple_Door.png"));
			purpleOpened = ImageIO.read(new File("images/Purple_Door_Opened.png"));
			greenClosed = ImageIO.read(new File("images/Green_Door.png"));
			greenOpened = ImageIO.read(new File("images/Green_Door_Opened.png"));

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
		if (purpleClosed != null && purpleOpened != null && greenClosed != null && greenOpened != null) {

			if ("Purple".equals(color)) {
				if (isOpen) {
					currentSprite = purpleOpened;
				} else {
					currentSprite = purpleClosed;
				}
			}
			if ("Green".equals(color)) {
				if (isOpen) {
					currentSprite = greenOpened;
				} else {
					currentSprite = greenClosed;
				}
			}
			if (currentSprite != null) {
				g.drawImage(currentSprite, x, y, width, height, null);
				return;
			}
		} else {
			if (!isOpen) {
				if ("Purple".equals(color))
					g.setColor(new Color(128, 0, 128));
				else if ("Green".equals(color))
					g.setColor(Color.green);
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