package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Lava implements Tile {
	private int x, y, width, height;
	private String color;

	// Constructor
	public Lava(int x, int y, int width, int height, String color) {
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

	@Override
	public void draw(Graphics g) {
		if (color.equals("Red"))
			g.setColor(Color.red);
		if (color.equals("Blue"))
			g.setColor(Color.blue);
		g.fillRect(x, y, width, height);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

}