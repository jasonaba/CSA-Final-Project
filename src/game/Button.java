package game;

public class Button implements Tile, Switchable{
	private int x, y, width, height;
	private boolean isOn;
	
	//Constructor
	public Button(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isOn = false;//every button starts as false
	}
	
	//Methods
	@Override
	public boolean switchedOn() {
		// TODO Auto-generated method stub
		return isOn;
	}
	@Override
	public void interact() {
		// TODO Auto-generated method stub
		isOn = true;//This will only be called when a character is colliding with it
	}
	public void release() {
		isOn = false;//This will only be called when a character is not colliding with it
	}
	
	@Override
	public boolean isColliding(Character c) {
		// TODO Auto-generated method stub
		return c.getX() + c.getWidth()>=this.getX() /*Char right further right than Wall left*/ && 
				c.getX() <= this.getX() + this.getWidth() /*Char left further left than Wall right*/ &&
				c.getY()+c.getHeight() >= this.getY() /*Char Bottom further down than Wall Top */ &&
				c.getY() <= this.getY() + this.getHeight();
	}
	
	//Getters
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
	
}
