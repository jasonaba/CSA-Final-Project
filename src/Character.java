
public class Character {
	//Variables
	private int x, y, xVel, yVel;
	private String color;
	private boolean isPlayerOne;//so it can know whether to use WASD or arrow keys if two player
	
	//Constructors
	public Character(int x, int y, String color, boolean isPlayerOne) {
		this.x = x;
		this.y = y;
		this.xVel = 0;
		this.yVel = 0;
		this.color = color;
		this.isPlayerOne = isPlayerOne;
	}
	
	
	
}
