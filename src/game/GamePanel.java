package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
	private Level levelManager;//controls everything to do with the levels
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(800,600));//800x600 screen
		this.setBackground(Color.black);
		
		levelManager = new Level();
		levelManager.loadCurrentLevel();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//loop through all active tiles and make them draw themselves
		ArrayList<Tile> tiles = levelManager.getActiveTiles();
		
		for(Tile t : tiles) {
			t.draw(g);
		}
	}
	
}
