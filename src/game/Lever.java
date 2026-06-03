package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Lever implements Tile, Switchable {
	private int x, y, width, height;
	private boolean isOn;// if switched on

	// Constructor
	public Lever(int x, int y, int width, int height, boolean isOn) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isOn = isOn;// based on the level, the interactable object will start on or off
	}

	// Methods
	@Override
	public boolean switchedOn() {
		return isOn;
	}

	@Override
	public void interact() {
		isOn = !isOn;
	}

	@Override
	public boolean isColliding(Character c) {
		return c.getX() + c.getWidth() >= this.getX()
				/* Char right further right than Wall left */ && c.getX() <= this.getX()
						+ this.getWidth() /* Char left further left than Wall right */
				&& c.getY() + c.getHeight() >= this.getY() /* Char Bottom further down than Wall Top */
				&& c.getY() <= this.getY() + this.getHeight();
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

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.pink);
		g.fillRect(x, y, width, height);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

}
