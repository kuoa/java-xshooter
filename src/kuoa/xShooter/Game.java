package kuoa.xShooter;

import javax.swing.JFrame;


public class Game{

	public static void main(String[] args) {
		

		JFrame window = new JFrame ("Shooter");		
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// add the gamePanel
		window.setContentPane(new GamePanel ());		
		
		window.pack();
		window.setVisible(true);
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		
	}

}
