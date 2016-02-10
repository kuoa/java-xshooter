package kuoa.xShooter;

import java.util.ArrayList;

public class Levels {

	public static void newLevel(int number) {

		ArrayList<Enemy> enemies = GamePanel.enemies;

		// clear enemies just to be sure
		enemies.clear();

		switch (number) {
		case 1:

			for (int i = 0; i < 6; i++) {
				enemies.add(new Enemy(1, 1));
			}

			break;

		case 2:

			for (int i = 0; i < 10; i++) {
				enemies.add(new Enemy(1, 1));
			}
			for (int i = 0; i < 4; i++) {
				enemies.add(new Enemy(1, 2));
			}

			break;

		case 3:

			for (int i = 0; i < 6; i++) {
				enemies.add(new Enemy(1, 1));
			}

			for (int i = 0; i < 4; i++) {
				enemies.add(new Enemy(1, 2));
			}

			for (int i = 0; i < 2; i++) {
				enemies.add(new Enemy(1, 3));
			}

			break;

		case 4:

			for (int i = 0; i < 2; i++) {
				enemies.add(new Enemy(1, 3));
			}

			for (int i = 0; i < 8; i++) {
				enemies.add(new Enemy(2, 1));
			}

			break;

		case 5:

			for (int i = 0; i < 2; i++) {
				enemies.add(new Enemy(1, 3));
			}

			for (int i = 0; i < 4; i++) {
				enemies.add(new Enemy(2, 2));
			}

			for (int i = 0; i < 8; i++) {
				enemies.add(new Enemy(2, 1));
			}

			break;

		case 6:

			for (int i = 0; i < 4; i++) {
				enemies.add(new Enemy(2, 3));
			}

			break;

		case 7:

			for (int i = 0; i < 2; i++) {
				enemies.add(new Enemy(1, 3));
			}

			for (int i = 0; i < 8; i++) {
				enemies.add(new Enemy(3, 1));
			}

			break;

		case 8:

			for (int i = 0; i < 2; i++) {
				enemies.add(new Enemy(2, 2));
			}

			for (int i = 0; i < 8; i++) {
				enemies.add(new Enemy(3, 1));
			}

			for (int i = 0; i < 4; i++) {
				enemies.add(new Enemy(3, 2));
			}

			break;

		case 9:

			for (int i = 0; i < 6; i++) {
				enemies.add(new Enemy(2, 2));
				enemies.add(new Enemy(1, 1));
			}

			for (int i = 0; i < 3; i++) {
				enemies.add(new Enemy(3, 1));
			}

			for (int i = 0; i < 3; i++) {
				enemies.add(new Enemy(3, 2));
			}

			break;	

		default:
			
			// AUTOMATED MODE
			
			int types = 4;
			int levels = 3;			
			int min = 0;
			int max = 0;
			int n = 0;
			int level = 0;
			
			if (number % 10 == 0){
				
				// BOSS MODE
				
				for (int i = 1; i < (number / 10 + 1); i++){
					enemies.add(new BossEnemy(4, 3));
				}
			}
			
			for (int i = 1; i < types; i++) {
				
				// type i

				if (i == 1) {
					level = (int) (Math.random() * levels + 1);

					if (level == 1) {
						min = (int) (number * 0.4);
						max = (int) (number * 0.8);

						n = (int) (min + Math.random() * (max - min + 1));
					}

					else if (level == 2) {
						min = (int) (number * 0.2);
						max = (int) (number * 0.5);

						n = (int) (min + Math.random() * (max - min + 1));
					}

					else {

						min = (int) (number * 0.1);
						max = (int) (number * 0.2);

						n = (int) (min + Math.random() * (max - min + 1));
					}
					

				}
				
				else  if (i == 2){
					level = (int) (Math.random() * levels + 1);
					
					if (level == 1){
						min = (int) (number * 0.3);
						max = (int) (number * 0.6);
						
						n = (int) (min + Math.random()* (max - min + 1));							
					}
					
					else if (level == 2){
						min = (int) (number * 0.1);
						max = (int) (number * 0.3);
						
						n = (int) (min + Math.random()* (max - min + 1));							
					}
					
					else {
						
						min = (int) (number * 0.5);
						max = (int) (number * 0.1);
						
						n = (int) (min + Math.random()* (max - min + 1));						
					}
																				
					
				}
				
				else {
					level = (int) (Math.random() * levels + 1);
					
					if (level == 1){
						min = (int) (number * 0.3);
						max = (int) (number * 0.6);
						
						n = (int) (min + Math.random()* (max - min + 1));							
					}
					
					else if (level == 2){
						min = (int) (number * 0.1);
						max = (int) (number * 0.3);
						
						n = (int) (min + Math.random()* (max - min + 1));							
					}
					
					else {
						
						min = (int) (number * 0.5);
						max = (int) (number * 0.1);
						
						n = (int) (min + Math.random()* (max - min + 1));						
					}
																				
					
				}																
				
				for (int j = 0; j < n; j++) {
					enemies.add(new Enemy(i, level));
				}
				
			}
			
			

			break;
		}

		// automatic wave generation

	}
}
