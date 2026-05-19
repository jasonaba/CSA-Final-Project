package game;

public class Wall implements Tile {
	private int x, y, width, height;

	public Wall(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return x;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return y;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return width;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return height;
	}

	@Override
	/**
	 * Checks if the wall is colliding with the character using Axis-Aligned Bounding Box Collisions
	 */
	public boolean isColliding(Character c) {
		// TODO Auto-generated method stub
		
		return c.getX() + c.getWidth()>=this.getX() /*Char right further right than Wall left*/ && 
				c.getX() <= this.getX() + this.getWidth() /*Char left further left than Wall right*/ &&
				c.getY()+c.getHeight() >= this.getY() /*Char Bottom further down than Wall Top */ &&
				c.getY() <= this.getY() + this.getHeight() /* Char top further up than Wall Bottom*/;
	}

}
