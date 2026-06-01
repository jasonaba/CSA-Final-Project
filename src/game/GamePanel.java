package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
	private Level levelManager;//controls everything to do with the levels
	private Timer gameClock;//for the loop
	private boolean isSinglePlayer, controllingPlayerOne;
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(800,600));//800x600 screen
		this.setBackground(Color.black);
		
		this.isSinglePlayer = false;
		controllingPlayerOne = true;//tracks in one-player mode what character is being controlled
		
		levelManager = new Level();
		levelManager.loadCurrentLevel();
		
		//Make this take in input from keyboard
		this.setFocusable(true);
		this.addKeyListener(this);
		
		//Set up timer (60 fps)
		gameClock = new Timer(16,this);
		gameClock.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//loop through all active tiles and make them draw themselves
		ArrayList<Tile> tiles = levelManager.getActiveTiles();
		
		for(Tile t : tiles) {
			t.draw(g);
		}
		
		// Draw the characters
        Character p1 = levelManager.getPlayer1();
        Character p2 = levelManager.getPlayer2();
        
        if (p1 != null) p1.draw(g);
        if (p2 != null) p2.draw(g);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		Character p1 = levelManager.getPlayer1();
		Character p2 = levelManager.getPlayer2();
		
		if(key == KeyEvent.VK_SPACE) {
			if(this.isSinglePlayer){
				if(p1 != null)p1.stop();
				if(p2 != null)p2.stop();
				
				//Active Player Indicator
				controllingPlayerOne = !controllingPlayerOne;
				System.out.println("Switched characters! Controlling Player 1: " + this.controllingPlayerOne);
			}
			return;
		}
		
		//movement inputs for single player
		if(this.isSinglePlayer) {
			if(this.controllingPlayerOne) {
				Character activePlayer = p1;
			} else {
				Character activePlayer = p2;
			}
			
		}
		
		if(p1 != null) {
			if(key == KeyEvent.VK_A) p1.moveLeft();
			if(key == KeyEvent.VK_D) p1.moveRight();
			if(key == KeyEvent.VK_W) p1.jump();
		}
		
		if(p2 != null) {
			if(key == KeyEvent.VK_LEFT) p2.moveLeft();
			if(key == KeyEvent.VK_RIGHT) p2.moveRight();
			if(key == KeyEvent.VK_UP) p2.jump();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
	    
	    Character p1 = levelManager.getPlayer1();
	    Character p2 = levelManager.getPlayer2();
	    
	    // Stop Player 1 if they let go of key
	    if (p1 != null) {
	        if (key == KeyEvent.VK_A || key == KeyEvent.VK_D) {
	            p1.stop();
	        }
	    }
	    
	    // Stop Player 2 if they let go of key
	    if (p2 != null) {
	        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
	            p2.stop();
	        }
	    }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Character p1 = levelManager.getPlayer1();
		Character p2 = levelManager.getPlayer2();
		
		if(p1 != null) p1.update();
		if(p2 != null) p2.update();
		
		repaint();
	}
	
}
