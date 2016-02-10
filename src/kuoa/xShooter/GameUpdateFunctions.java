package kuoa.xShooter;

import java.util.ArrayList;

import kuoa.xShooter.PowerUp.Power;


public class GameUpdateFunctions {

	
	
	public static void bulletsUpdate() {
		// BULLETS UPDATE
		ArrayList<Bullet> bullets = GamePanel.bullets;

		for (int i = 0; i < bullets.size(); i++) {

			boolean remove = bullets.get(i).update();

			if (remove) {
				bullets.remove(i);
				i--;
			}
		}
	}

	public static void enemiesUpdate() {
		// enemies update position

		ArrayList<Enemy> enemies = GamePanel.enemies;
		
		GamePanel.bossDivisionDiff = (System.nanoTime() - GamePanel.bossDivisionStart) / 1000000;
		
		if (GamePanel.bossDivisionDiff >= GamePanel.bossDivisonDelay){
			GamePanel.bossDivision = false;						
		}
		
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i); 
			
			if(e instanceof BossEnemy && GamePanel.bossDivision == false){
				
				enemies.add(new Enemy(e.getX(), e.getY(), e.getType(), 1));
				GamePanel.bossDivisionStart = System.nanoTime();
				GamePanel.bossDivision = true;
			}
			
			e.update();
		}
	}

	public static void powerUpUpdate() {
		// power up update position

		ArrayList<PowerUp> powerups = GamePanel.powerups;

		for (int i = 0; i < powerups.size(); i++) {

			boolean remove = powerups.get(i).update();

			if (remove) {
				powerups.remove(i);
				i--;
			}
		}
	}

	public static void textUpdate() {
		// text update position
		ArrayList<Text> text = GamePanel.text;

		for (int i = 0; i < text.size(); i++) {
			boolean remove = text.get(i).update();

			if (remove) {
				text.remove(i);
				i--;
			}
		}
	}
	
	public static void explosionUpdate(){
		
		ArrayList<Explosion> explosions = GamePanel.explosions;
		
		for (int i = 0; i < explosions.size(); i++) {
			
			Boolean remove = explosions.get(i).update();
			
			if (remove){
				explosions.remove(i);
				i--;
			}			
		}		
	}
	
	public static void checkBulletEnemyCollision() {

		ArrayList<Bullet> bullets = GamePanel.bullets;
		ArrayList<Enemy> enemies = GamePanel.enemies;
		ArrayList<PowerUp> powerups = GamePanel.powerups;
		ArrayList<Explosion> explosion = GamePanel.explosions;

		Player player = GamePanel.player;

		for (int i = 0; i < bullets.size(); i++) {

			Bullet b = bullets.get(i);
			double bX = b.getX();
			double bY = b.getY();
			int bR = b.getR();

			for (int j = 0; j < enemies.size(); j++) {

				Enemy e = enemies.get(j);
				double eX = e.getX();
				double eY = e.getY();
				int eR = e.getR();

				// pitagora distance between 2 points
				double deltaX = bX - eX;
				double deltaY = bY - eY;
				double distanceBE = Math.sqrt((deltaX * deltaX + deltaY * deltaY));

				// if distance smaller than sume of radius

				if (distanceBE < bR + eR) {

					// update score
					player.updateScore(2 * (e.getLevel() + e.getType()));

					// enemy got hit by bullet
					e.gotHit();

					// remove bullet from the list
					bullets.remove(i);
					i--;

					if (e.isDead()) {
						// if enemy dead, remove him
						GamePanel.explosion.play();
						int type = e.type;
						int level = e.level;

						enemies.remove(j);
						j--;
						
						//create explosion
						explosion.add(new Explosion(eX, eY, eR));
						
						
						// create new enemies
						if (level > 1) {
							for (i = 0; i < e.getPieces(); i++) {
								enemies.add(new Enemy(eX, eY, type, e.getSpawnLevel()));
							}
						}
					}

					double p = Math.random();

					// add new powerUp

					if (p < 0.001) {
						GamePanel.powerUpSpawn.play();
						powerups.add(new PowerUp((int) e.getX(), (int) e.getY(), Power.LIFE, GamePanel.pwrLife));
					} 
					else if (p< 0.01) {
						
						GamePanel.powerUpSpawn.play();
						powerups.add(new PowerUp((int) e.getX(), (int) e.getY(), Power.SHIELD, GamePanel.pwrShield));
					}
						//0.03
					else if (p < 0.03) {
						GamePanel.powerUpSpawn.play();
						powerups.add(
								new PowerUp((int) e.getX(), (int) e.getY(), Power.STRENGTH, GamePanel.pwrStrength));
					}
					
					else if (p < 0.06){
						GamePanel.powerUpSpawn.play();
						powerups.add(
								new PowerUp((int) e.getX(), (int) e.getY(), Power.SLOWDOWN, GamePanel.pwrSlowDown));
					}

					break;
				}
			}
		}
	}
	
	public static boolean checkPlayerEnemyCollision(){
		
		// returns true if shield must be activated
		
		ArrayList<Enemy> enemies = GamePanel.enemies;
		ArrayList<Explosion> explosion = GamePanel.explosions;
		Player player = GamePanel.player;
		
		double pR = player.getR();
		double pX = player.getX() + pR;
		double pY = player.getY() + pR;

		// PLAYER ENEMY UNIT COLISION DETECTION
		for (int j = 0; j < enemies.size(); j++) {

			Enemy e = enemies.get(j);
			double eX = e.getX();
			double eY = e.getY();
			int eR = e.getR();

			// pitagora distance between 2 points
			double deltaX = pX - eX;
			double deltaY = pY - eY;
			double distanceBE = Math.sqrt((deltaX * deltaX + deltaY * deltaY));

			if (player.getShield() == false && distanceBE < pR + eR) {
				
				GamePanel.playerHit.play();
				// player takes 1 damage
				player.decrLives();

				// enemy takes 1 damage
				e.gotHit();

				// remove enemy if needed
				if (e.isDead()) {
					enemies.remove(e);
					j--;
					
					//create explosion
					explosion.add(new Explosion(eX, eY, eR));
					
				}

				// remove 2 power
				player.removePower();
				
				return true;
				
			}
		}
		
		return false;
		
	}
	
	public static void checkPlayerPowerUpCollision(){
		
		Player player = GamePanel.player;
		ArrayList<PowerUp> powerups = GamePanel.powerups;
		ArrayList<Text> text = GamePanel.text;
		ArrayList<Enemy> enemies = GamePanel.enemies; 
		
		double pR = player.getR();
		double pX = player.getX() + pR;
		double pY = player.getY() + pR;
		
		for (int i = 0; i < powerups.size(); i++) {

			PowerUp pwr = powerups.get(i);
			double pwrX = pwr.getX();
			double pwrY = pwr.getY();
			int pwrR = pwr.getR();

			// pitagora distance between 2 points
			double deltaX = pX - pwrX;
			double deltaY = pY - pwrY;
			double distancePPWR = Math.sqrt((deltaX * deltaX + deltaY * deltaY));

			if (distancePPWR < pR + pwrR) {

				Power type = pwr.getType();

				switch (type) {
				case LIFE:
					GamePanel.powerLife.play();
					text.add(new Text(pwrX, pwrY, "Life +1"));
					player.addLives();
					break;

				case SHIELD:
					text.add(new Text(pwrX, pwrY, "Shield"));					
					player.setShield(true);
					GamePanel.hasShield = true;
					GamePanel.shieldActivationTimer = System.nanoTime();
					break;

				case STRENGTH:
					GamePanel.powerPower.play();
					text.add(new Text(pwrX, pwrY, "Power +1"));
					boolean add = player.updatePower();

					if (add) {
						player.addPower();
					}

					break;
					
				case SLOWDOWN:
					GamePanel.powerSlowOn.play();
					text.add(new Text(pwrX, pwrY, "Slow time"));
					
					for (int j = 0; j < enemies.size(); j++) {
						enemies.get(j).setSlowed(true);						
					}
					GamePanel.slowDown = true;
					GamePanel.slowDownActivationTimer = System.nanoTime();
					
					
					break;

				}

				// remove powerup
				powerups.remove(i);
				i--;
			}
		}
	}
}
