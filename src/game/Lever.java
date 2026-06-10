package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Lever implements Tile, Switchable {
	private int x, y, width, height;
	private boolean isOn, /* if switched on */ wasCollidingLastFrame;
	private static Image leverSprite;

	// Constructor
	public Lever(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isOn = false;// starts off always
		this.wasCollidingLastFrame = false;
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
		return this.getBounds().intersects(c.getBounds());
	}

	public static void loadImages() {
		try {
			leverSprite = ImageIO.read(new File("images/Lever.png"));
			System.out.println("Lever image loaded successfully!");
		} catch (IOException e) {
			System.out.println("Couldn't load Lever image");
		}
	}

	/**
	 * Checks if the character just interacted with the lever and updates the
	 * wasCollidingLastFrame variable
	 * 
	 * @param p1 Character 1
	 * @param p2 Character 2
	 */
	public void update(Character p1, Character p2) {
		boolean isCurrentlyColliding = isColliding(p1) || isColliding(p2);

		// Trigger interact() ONLY when a player first steps into the lever's hitbox
		if (isCurrentlyColliding && !wasCollidingLastFrame) {
			interact();
		}

		// Update tracking for the next frame
		wasCollidingLastFrame = isCurrentlyColliding;
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
		if (leverSprite != null) {
			if (!isOn) {
				// State 1: Off - Draw the lever facing its normal direction
				g.drawImage(leverSprite, x, y, width, height, null);
			} else {
				// State 2: On - Mirror trick: Flip it horizontally so it faces the other way!
				g.drawImage(leverSprite, x + width, y, -width, height, null);
			}
		} else {
			if (!isOn) {
				g.setColor(Color.pink);
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

}
