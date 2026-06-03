package game;

import javax.swing.JFrame;

public class Driver {

	public static void main(String[] args) {

		//Create JFrame
		JFrame window = new JFrame("Solo Duo");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		
		//Create JPanel
		GamePanel panel = new GamePanel();
		window.add(panel);
		window.pack();//makes JFrame (window) fit 800x600 panel
		window.setLocationRelativeTo(null);//centers the window
		window.setVisible(true);
	}

}
