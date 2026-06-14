package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Gas implements Tile {
	private int x, y, width, height;
	private String color;
	private static Image greenSprite, purpleSprite;

	// Constructor
	public Gas(int x, int y, int width, int height, String color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
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

	// Methods
	@Override
	public boolean isColliding(Character c) {
		return this.getBounds().intersects(c.getBounds());
	}

	public boolean kills(Character c) {
		if (isColliding(c)) {
			if (!c.getColor().equals(this.color))
				return true;
		}
		return false;
	}
	
	public static void loadImages() {
		try {

			greenSprite = ImageIO.read(new File("images/GreenGas.png"));
			purpleSprite = ImageIO.read(new File("images/PurpleGas.png"));
			System.out.println("Gas images loaded successfully!");
		} catch (IOException e) {
			System.out.println("Couldn't load Gem images");
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