package kuoa.xShooter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Enemy {

	// FIELDS
	protected double x;
	protected double y;
	protected double rad;

	protected int r;
	protected double speed;

	protected double dx;
	protected double dy;


	private Color color1;
	private Color color2;
	private Color color3;

	public int type;
	public int level;
	private int health; // how many shots the target can take

	protected boolean ready; // enemy ready to spawn
	private boolean dead;
	private boolean hit;

	private double hitStart;
	private double hitDelay = 120;

	public int pieces = 0; // number of enemies generated on explosion
	public int spawnLevel = 1; // level of spawned enemies

	protected boolean isSlowed = false;

	// CONSTRUCTOR
	public Enemy(int type, int level) {

		this.type = type;
		this.level = level;

		x = Math.random() * (GamePanel.WIDTH / 2) + GamePanel.WIDTH / 4;
		y = -r;

		// angle
		double angle = Math.random() * 140 + 20;
		rad = Math.toRadians(angle);

		ready = false;
		dead = false;
		hit = false;

		// BASIC ENEMIES
		switch (type) {
		case 1:

			// DEFAULT color for type 1

			color1 = MyColors.myBlue;
			color2 = MyColors.myBlueD;
			color3 = color1.brighter();

			switch (level) {
			case 1:
				// basic (EASY)
				r = 10; // radius
				speed = 2; // speed
				health = 1;
				pieces = 0;
				spawnLevel = 0;
				break;

			case 2:
				// slow, spawn 3 more (EASY)
				r = 13;
				speed = 1.5;
				health = 3;
				pieces = 3;
				spawnLevel = 1;
				break;

			case 3:
				// verry slow, spawn 4 (EASY)
				r = 23;
				speed = 1;
				health = 5;
				pieces = 4;
				spawnLevel = 2;
				break;
			}

			break;

		// MEDIUM ENEMIES
		case 2:
			// DEFAULT color for type 2
			color1 = MyColors.myGreen;
			color2 = MyColors.myGreenD;
			color3 = color1.brighter();

			switch (level) {
			case 1:
				// fast bastards (MEDIUM)
				r = 10; // radius
				speed = 3; // speed
				health = 1;
				pieces = 0;
				spawnLevel = 0;
				break;

			case 2:

				r = 14; // radius
				speed = 2; // speed
				health = 2;
				pieces = 0;
				spawnLevel = 0;
				break;

			case 3:

				r = 28;
				speed = 1.0;
				health = 8;
				pieces = 10;
				spawnLevel = 1;

				break;
			}

			break;

		case 3:
			// DEFAULT color for type 3
			color1 = MyColors.myPink;
			color2 = MyColors.myPinkD;
			color3 = color1.brighter();

			switch (level) {
			case 1:
				// fast bastards (MEDIUM)
				r = 18; // radius
				speed = 3; // speed
				health = 2;
				pieces = 0;
				spawnLevel = 0;
				break;

			case 2:

				r = 34; // radius
				speed = 2; // speed
				health = 3;
				pieces = 4;
				spawnLevel = 1;
				break;

			case 3:

				r = 48;
				speed = 1.0;
				health = 8;
				pieces = 6;
				spawnLevel = 1;

				break;
			}
			break;

		case 4:

			// BOSS ENEMY
			color1 = MyColors.myViolet;
			color2 = MyColors.myVioletD;
			color3 = color1.brighter();

			switch (level) {
			case 1:
				// fast bastards (MEDIUM)
				r = 10; // radius
				speed = 5; // speed
				health = 1;
				pieces = 0;
				spawnLevel = 0;
				break;

			case 2:
				// Big Boos

				r = 18; // radius
				speed = 1; // speed
				health = 20;
				pieces = 10;
				spawnLevel = 1;
				break;

			case 3:
				// Big Boos

				r = 38; // radius
				speed = 0.5; // speed
				health = 60;
				pieces = 10;
				spawnLevel = 1;
				break;
			}

			break;
		}

		// update position
		dx = Math.cos(rad) * speed;
		dy = Math.sin(rad) * speed;

	}

	public Enemy(double x, double y, int type, int level) {
		// used for dividing enemies

		this(type, level);
		this.x = x;
		this.y = y;
	}

	public Enemy(double x, double y, int type, int level, int angle) {
		// used for dividing enemies

		this(type, level);
		this.x = x;
		this.y = y;
		this.rad = Math.toRadians(angle);
	}

	// FUNCTIONS
	public void update() {

		if (isSlowed == false) {

			x += dx;
			y += dy;
		} else {
			x += 0.3 * dx;
			y += 0.3 * dy;
		}

		if (ready == false) {

			if (x > r && x < GamePanel.WIDTH - r && y > r && y < GamePanel.HEIGHT - r) {

				ready = true;
			}
		}
		// if out of grid, then bounce back under the same angle
		if (x < r && dx < 0) {
			dx = -dx;
		}

		if (y < r && dy < 0) {
			dy = -dy;
		}

		if (x > GamePanel.WIDTH - r && dx > 0) {
			dx = -dx;
		}

		if (y > GamePanel.HEIGHT - r && dy > 0) {
			dy = -dy;
		}

	}

	public void draw(Graphics2D g) {

		// NU UITA (x - r) (y - r)

		if (hit) {
			g.setColor(color3);

			double hitDiff = (System.nanoTime() - hitStart) / 1000000;

			if (hitDiff >= hitDelay) {
				hit = false;
			}
		} else {
			g.setColor(color1);
		}

		g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);

		g.setColor(color2);
		g.setStroke(new BasicStroke(2));
		g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
		g.setStroke(new BasicStroke(1));
	}

	public void gotHit() {

		health--;
		hit = true;
		hitStart = System.nanoTime();

		if (health <= 0) {
			dead = true;
		}
	}

	// GETTERS
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getR() {
		return r;
	}

	public boolean isDead() {
		return dead;
	}

	public int getType() {
		return type;
	}

	public int getLevel() {
		return level;
	}

	public int getPieces() {
		return pieces;
	}

	public int getSpawnLevel() {
		return spawnLevel;
	}

	// SETTERS

	public void setSlowed(boolean b) {
		isSlowed = b;
	}

}
