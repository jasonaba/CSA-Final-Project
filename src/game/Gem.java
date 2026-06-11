package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Gem implements Tile {
	private int x, y, width, height;
	private String color;

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

	@Override
	public void draw(Graphics g) {
		if (color.equals("Purple"))
			g.setColor(new Color(128,0,128));
		else if (color.equals("Green"))
			g.setColor(Color.green);

		g.fillRect(x, y, width, height);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

}