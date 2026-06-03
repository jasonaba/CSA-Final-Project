package game;

import java.awt.Image;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Character {
	// Variables
	private int x, y, xVel, yVel, width, height, gravity;
	private String color;
	private boolean isPlayerOne, /* so it can know whether to use WASD or arrow keys if two player */
			isOnFloor;// to check if jumping is possible
	private Image sprite;

	// Constructors
	public Character(int x, int y, int width, int height, String color, boolean isPlayerOne) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.xVel = 0;
		this.yVel = 0;
		this.color = color;
		this.isPlayerOne = isPlayerOne;
		isOnFloor = false;
		gravity = 1;
		try {
			if (isPlayerOne) {
				sprite = ImageIO.read(new File("images/player1.png"));
			} else {
				sprite = ImageIO.read(new File("images/player2.png"));
			}
		} catch (IOException e) {
			System.out.println("Unable to load character image");
			e.printStackTrace();
		}
	}

	// Getters and Setters

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getColor() {
		return color;
	}

	/**
	 * Enables other classes to see a character's hitbox
	 * 
	 * @return A rectangle of the character's hitbox
	 */
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public void setYVel(int yVel) {
		this.yVel = yVel;
	}

	public void setXVel(int xVel) {
		this.xVel = xVel;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setIsOnFloor(boolean b) {
		this.isOnFloor = b;
	}

	// Methods

	/**
	 * update the game every frame
	 */
	public void update() {
		// To implement gravity and movement when necessary
		yVel += gravity;
		x = x + xVel;
		y = y + yVel;

		isOnFloor = false;// Tiles will set this to true
	}

	public void draw(Graphics g) {
		if (sprite != null) {
			g.drawImage(sprite, x, y, width, height, null);
		} else {
			// just in case image is missing
			if ("Red".equals(color))
				g.setColor(Color.RED);

			else if ("Blue".equals(color))
				g.setColor(Color.blue);
			g.fillRect(x, y, width, height);
		}
	}

	/**
	 * When the specific button is pressed, move the character right
	 */
	public void moveRight() {
		xVel = 5;
	}

	/**
	 * When the specific button is pressed, move the character left
	 */
	public void moveLeft() {
		xVel = -5;
	}

	/**
	 * When no button is pressed, stop the character from moving
	 */
	public void stop() {
		xVel = 0;
	}

	/**
	 * Give the character a burst of vertical speed so it will move up
	 */
	public void jump() {
		// The character can only jump if they are on a platform
		if (isOnFloor) {
			yVel = -15;
			isOnFloor = false;
		}
	}

}