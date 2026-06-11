package game;

import java.awt.Image;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Character {
	// Variables
	private int x, y, xVel, yVel, width, height, gravity;
	private String color;
	private boolean isPlayerOne, /* so it can know whether to use WASD or arrow keys if two player */
			isOnFloor;// to check if jumping is possible
	// Animation variables
	private Image[] frames;
	private int animationCounter;
	private boolean facingRight;

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

		this.frames = new Image[4];
		this.animationCounter = 0;
		this.facingRight = true;
		loadSpritesheet();
	}

	private void loadSpritesheet() {
		String filename = "";

		
		try {
			if (isPlayerOne) {
				filename = "images/player1.png";
			} else {
				filename = "images/player2.png";
			}
			//So we can use Subimage
			BufferedImage sheet = ImageIO.read(new File(filename));
			int frameWidth= sheet.getWidth();
			int frameHeight = sheet.getHeight();
			// AP CSA: Slicing the 2x2 array out using 32x32 bounding boxes
			frames[0] = sheet.getSubimage(0, 0, frameWidth, frameHeight/2); // Top Left (Idle)
			frames[1] = sheet.getSubimage(0, frameHeight/2, frameWidth, frameHeight/2); // Top Right (Walk 1)
			/*
			frames[2] = sheet.getSubimage(0, 32, 32, 32); // Bottom Left (Walk 2)
			frames[3] = sheet.getSubimage(32, 32, 32, 32); // Bottom Right (Jump)*/
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
	 * Moves Character horizontally
	 */
	public void updateX() {
		x = x + xVel;
		
		//Change Direction of sprite
		if(xVel > 0) {
			facingRight = true;
		}
		else if (xVel < 0 ) {
			facingRight = false;
		}
	}

	/**
	 * Moves the character vertically and applies gravity
	 */
	public void updateY() {
		yVel += gravity; // to implement gravity
		y = y + yVel;
		isOnFloor = false;// Tiles will set this to true
	}

	public void draw(Graphics g) {
		//Increase the counter every frame so it animates it
		animationCounter++;
		//Choose the right frame to draw
		Image activeFrame = frames[0];
		
		if(!isOnFloor){//if falling/not on ground
			activeFrame = frames[0];
		}else if (xVel != 0) {//if moving on the ground
			if((animationCounter/10)%2 == 0) {
				activeFrame = frames[1];
			}else {
				activeFrame = frames[0];
			}
		}
		
		if(activeFrame != null){
			if(facingRight) {
				g.drawImage(activeFrame, x, y, width, height, null);
			}else {//negative width to flip it around
				g.drawImage(activeFrame, x+width, y, -width, height, null);
		}
		} else {
			// just in case image is missing
			if ("Purple".equals(color))
				g.setColor(new Color(128,0,128));

			else if ("Green".equals(color))
				g.setColor(Color.green);
			g.fillRect(x, y, width, height);
		}
	}

	/**
	 * When the specific button is pressed, move the character right
	 */
	public void moveRight() {
		xVel = 3;
	}

	/**
	 * When the specific button is pressed, move the character left
	 */
	public void moveLeft() {
		xVel = -3;
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