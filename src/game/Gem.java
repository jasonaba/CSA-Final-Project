package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Gem implements Tile {
	private int x, y, width, height;
	private String color;
	private static Image purpleSprite, greenSprite;

	// Constructors
	public Gem(int x, int y, int width, int height, String color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}

	// Getters
	public String getColor() {
		return color;
	}

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

	// Methods
	@Override
	public boolean isColliding(Character c) {
		return this.getBounds().intersects(c.getBounds());
	}

	public boolean tryCollect(Character c) {
		return isColliding(c) && c.getColor().equals(this.color);
	}

	public static void loadImages() {
		try {
			greenSprite = ImageIO.read(new File("images/GreenGem.png"));
			purpleSprite = ImageIO.read(new File("images/PurpleGem.png"));
		} catch (IOException e) {
			System.out.println("Couldn't load Gem images. Using backup colors.");
		}
	}

	@Override
	public void draw(Graphics g) {
		Image currentSprite = null;
		if (purpleSprite != null & greenSprite != null) {
			if ("Purple".equals(color)) {
				currentSprite = purpleSprite;
			} else if ("Green".equals(color)) {
				currentSprite = greenSprite;
			}
			g.drawImage(currentSprite, x, y, width, height, null);
		}

		else {
			if ("Purple".equals(color))
				g.setColor(new Color(128, 0, 128));
			else if ("Green".equals(color))
				g.setColor(Color.green);

			g.fillRect(x, y, width, height);
		}
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

}