package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Wall implements Tile {
	private int x, y, width, height;
	private static Image wallSprite;

	// Constructor
	public Wall(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

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

	// Methods

	@Override
	/**
	 * Checks if the wall is colliding with the character using Axis-Aligned
	 * Bounding Box Collisions
	 */
	public boolean isColliding(Character c) {

		return c.getX() + c.getWidth() >= this.getX()/* Char right further right than Wall left */ && c
				.getX() <= this.getX() + this.getWidth() /* Char left further left than Wall right */
				&& c.getY() + c.getHeight() >= this.getY() /* Char Bottom further down than Wall Top */
				&& c.getY() <= this.getY() + this.getHeight() /* Char top further up than Wall Bottom */;
	}

	@Override
	public void draw(Graphics g) {
		if (wallSprite != null) {
			// 3. Draw your artwork directly onto the grid space!
			g.drawImage(wallSprite, x, y, width, height, null);
		} else {
			g.setColor(Color.gray);
			g.fillRect(x, y, width, height);
		}
	}

	@Override
	public Rectangle getBounds() {

		return new Rectangle(x, y, width, height);
	}

	public static void loadImages(){
		try {
			wallSprite = ImageIO.read(new File("images/Wall.png"));
			System.out.println("Wall images loaded successfully!");
		} catch (IOException e) {
			System.out.println("Error: Could not load images/Wall.png. Using backup gray walls.");
		}
	}

}
