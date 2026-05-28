package game;

import java.awt.Graphics;

public interface Tile {
	//All types of tiles have to have these methods
	
	public int getX();
	public int getY();
	public int getWidth();
	public int getHeight();
	/**
	 * Checks if it is colliding with the character
	 * @param c (the character in question)
	 * @return if it is colliding
	 */
	public boolean isColliding(Character c);
	
	/**
	 * Allow each tile to draw themselves
	 * @param g
	 */
	public void draw(Graphics g);
}
