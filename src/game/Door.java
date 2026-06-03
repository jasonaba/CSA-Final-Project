package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Door implements Tile {
	private int x, y, width, height;
	private boolean isOpen;
	private String color;

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
	@Override
	public boolean isColliding(Character c) {
		return c.getX() + c.getWidth() >= this.getX()
				/* Char right further right than Wall left */ && c.getX() <= this.getX()
						+ this.getWidth() /* Char left further left than Wall right */
				&& c.getY() + c.getHeight() >= this.getY() /* Char Bottom further down than Wall Top */
				&& c.getY() <= this.getY() + this.getHeight();
	}

	public void open() {
		isOpen = true;
	}

	@Override
	public void draw(Graphics g) {
		if (color.equals("Red"))
			g.setColor(Color.RED);
		else if (color.equals("Blue"))
			g.setColor(Color.blue);
		g.drawRect(x, y, width, height);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
}
