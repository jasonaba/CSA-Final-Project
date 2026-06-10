package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Button implements Tile, Switchable {
	private int x, y, width, height;
	private boolean isOn;
//Animation Variable
	private static Image[] buttonFrames;

	// Constructor
	public Button(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isOn = false;// every button starts as false
	}

	// Methods
	@Override
	public boolean switchedOn() {
		return isOn;
	}

	@Override
	public void interact() {
		isOn = true;// This will only be called when a character is colliding with it
	}

	public void release() {
		isOn = false;// This will only be called when a character is not colliding with it
	}

	@Override
	public boolean isColliding(Character c) {
		return this.getBounds().intersects(c.getBounds());
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

	// Methods

	@Override
	public void draw(Graphics g) {
		if (buttonFrames != null && buttonFrames[0] != null && buttonFrames[1] != null) {
			if (isOn) {
				g.drawImage(buttonFrames[1], x, y, width, height, null);
			} else {
				g.drawImage(buttonFrames[0], x, y, width, height, null);
			}
		} else {
			if (!isOn) {
				g.setColor(Color.orange);
			} else {
				g.setColor(Color.green);
			}
			g.fillRect(x, y, width, height);
		}
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public static void loadImages() {
		try {
			buttonFrames = new Image[2]; // Index 0 is Off, Index 1 is On

			// Reads the sheet as a BufferedImage to unlock .getSubimage()
			BufferedImage sheet = ImageIO.read(new File("images/Button.png"));

			// Slicing vertically: (x, y, width, height)
			buttonFrames[0] = sheet.getSubimage(0, 0, 32, 32); // Top frame (Off)
			buttonFrames[1] = sheet.getSubimage(0, 32, 32, 32); // Bottom frame (On)

			System.out.println("Button images sliced successfully!");
		} catch (IOException e) {
			System.out.println("Error: Could not load images/button.png. Using backup colors.");
		}
	}
}
