package kuoa.xShooter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Player {

	// FIELDS

	// position
	private int x;
	private int y;

	private int r; // hitbox radius

	// update position
	private int dx;
	private int dy;
	private int speed;

	// control
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;

	private int lives;
	private int score;

	// used for bullets
	private boolean firing;
	private long firingTimer;
	private long firingDelay;

	// images
	private BufferedImage playerSprite;
	private BufferedImage playerLives;
	private BufferedImage playerShield;
	private BufferedImage playerFireUp;
	private BufferedImage playerFireDown;
	private BufferedImage playerFireLeft;
	private BufferedImage playerFireRight;

	private BufferedImage pwrEmpty;
	private BufferedImage pwrFull;

	private static int playerWidth;
	private static int playerHeight;

	// used to stop ship from going offscreen
	public static int pixelBorder;
	private int diffWidth;
	private int diffHeight;

	private boolean shield;

	// current power level

	private int currentPowerLevel;
	private int currPoints;
	private int maxLevel;

	// CONSTRUCTOR

	public Player() {
		
		dx = 0;
		dy = 0;
		speed = 8;

		lives = 3;
		shield = false;

		// color1 = Color.WHITE; // regular
		// color2 = Color.RED; // hit

		firing = false;
		firingDelay = 200;
		firingTimer = System.nanoTime();

		currentPowerLevel = 1;
		currPoints = 0;
		maxLevel = 4;

		try {
			// loading player image
			// when selecting shitp, after loading images, set playerSprite to
			// ship(0 | 1 | 2);
			// playerSprite = ImageIO.read(new File ("img/player.png"));

			// used for jar
			playerSprite = ImageIO.read(getClass().getResourceAsStream("/Sprites/player.png"));
			playerLives = ImageIO.read(getClass().getResourceAsStream("/Sprites/lives.png"));
			playerShield = ImageIO.read(getClass().getResourceAsStream("/Sprites/shield.png"));

			playerFireUp = ImageIO.read(getClass().getResourceAsStream("/Sprites/fireU.png"));
			playerFireDown = ImageIO.read(getClass().getResourceAsStream("/Sprites/fireD.png"));
			playerFireLeft = ImageIO.read(getClass().getResourceAsStream("/Sprites/fireL.png"));
			playerFireRight = ImageIO.read(getClass().getResourceAsStream("/Sprites/fireR.png"));

			pwrEmpty = ImageIO.read(getClass().getResourceAsStream("/Sprites/pwrEmpty.png"));
			pwrFull = ImageIO.read(getClass().getResourceAsStream("/Sprites/pwrFull.png"));

			// used to compute maximum off size of ship
			playerWidth = playerSprite.getWidth();
			playerHeight = playerSprite.getHeight();
			pixelBorder = 9;

			// get width | height
			diffWidth = GamePanel.WIDTH - playerWidth - pixelBorder; // 9 pixels
																		// from
																		// screen
			diffHeight = GamePanel.HEIGHT - playerHeight - pixelBorder; // 9
																		// pixels
																		// from
																		// screen
			r = playerWidth / 2;

			// initiate x and y to low middle screen
			x = (GamePanel.WIDTH - playerWidth) / 2;
			y = GamePanel.HEIGHT;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// FUNCTIONS

	public void update() {

		if (left) {
			dx = -speed;
		}

		if (right) {
			dx = speed;
		}

		if (up) {
			dy = -speed;
		}

		if (down) {
			dy = speed;
		}

		x += dx;
		y += dy;

		// BOUNDS

		if (x < pixelBorder) {
			x = pixelBorder;
		}
		if (y < pixelBorder) {
			y = pixelBorder;
		}
		if (x > diffWidth) {
			x = diffWidth;
		}
		if (y > diffHeight) {
			y = diffHeight;
		}

		// RESET

		dx = 0;
		dy = 0;

		if (firing) {
			
			
			
			if ((System.nanoTime() - firingTimer) / 1000000 > firingDelay) {
				GamePanel.sound.play();
				switch (currentPowerLevel) {
				case 1:

					GamePanel.bullets.add(new Bullet(x + Player.playerWidth / 2, y, 270));
					firingTimer = System.nanoTime();

					break;

				case 2:

					GamePanel.bullets.add(new Bullet(x + Player.playerWidth / 2 - 15, y, 270));
					GamePanel.bullets.add(new Bullet(x + Player.playerWidth / 2 + 15, y, 270));
					firingTimer = System.nanoTime();

					break;

				case 3:
					// extra bullet in the middle

					GamePanel.bullets.add(new Bullet(x + Player.playerWidth / 2 - 15, y, 260));
					GamePanel.bullets.add(new Bullet(x + Player.playerWidth / 2, y, 270));
					GamePanel.bullets.add(new Bullet(x + Player.playerWidth / 2 + 15, y, 280));
					firingTimer = System.nanoTime();

					break;

				case 4:
					// two extra bullets in the middle

					GamePanel.bullets.add(new Bullet(x + Player.playerWidth / 2 - 20, y, 260));
					GamePanel.bullets.add(new Bullet(x + Player.playerWidth / 2 - 10, y, 270));
					GamePanel.bullets.add(new Bullet(x + Player.playerWidth / 2 + 10, y, 270));
					GamePanel.bullets.add(new Bullet(x + Player.playerWidth / 2 + 20, y, 280));
					firingTimer = System.nanoTime();

					break;

				default:

					GamePanel.bullets.add(new Bullet(x + Player.playerWidth / 2, y, 270));
					firingTimer = System.nanoTime();
					break;
				}

			}
		}

	}

	public void draw(Graphics2D g) {

		// draw player
		g.drawImage(playerSprite, null, x, y);

		// draw engine animation
		if (up) {
			g.drawImage(playerFireUp, null, x + (int) (playerWidth * 0.6), y + playerHeight);
			g.drawImage(playerFireUp, null, x + (int) (playerWidth * 0.25), y + playerHeight);
		}

		if (down) {
			g.drawImage(playerFireDown, null, x + (int) (playerWidth * 0.6), y + playerHeight);
			g.drawImage(playerFireDown, null, x + (int) (playerWidth * 0.25), y + playerHeight);
		}

		if (right) {
			g.drawImage(playerFireLeft, null, x - playerFireLeft.getWidth(), y + playerHeight / 2);
		}

		if (left) {
			g.drawImage(playerFireRight, null, x + playerSprite.getHeight() + 10, y + playerHeight / 2);

		}

		// shield
		if (shield) {
			g.drawImage(playerShield, null, x + (playerWidth - playerShield.getWidth()) / 2,
					y + (playerHeight - playerShield.getHeight()) / 2);
		}

		// player score
		String tempScore = "Score: " + score;
		g.setColor(Color.WHITE);
		g.setFont(GamePanel.myFont.deriveFont(Font.PLAIN, 32));
		int length = (int) g.getFontMetrics().getStringBounds(tempScore, g).getWidth();
		int height = (int) g.getFontMetrics().getStringBounds(tempScore, g).getHeight();		
		g.drawString(tempScore, GamePanel.WIDTH - length - pixelBorder, height);
		
		// enemies left
		
		String enemiesLeft = "Enemies: " + GamePanel.enemies.size();
		length = (int) g.getFontMetrics().getStringBounds(enemiesLeft, g).getWidth();
		height = height + (int) g.getFontMetrics().getStringBounds(enemiesLeft, g).getHeight();
		g.drawString(enemiesLeft, GamePanel.WIDTH - length - pixelBorder, height - 5);
		
		
		// player lives
		for (int i = 0; i < lives; i++) {
			g.drawImage(playerLives, null, i * playerLives.getWidth() + pixelBorder, pixelBorder);

		}

		// player strength level draw

		for (int i = 0; i < currPoints; i++) {
			g.drawImage(pwrEmpty, null, i * pwrEmpty.getWidth() + pixelBorder + i * 3,
					playerLives.getHeight() + pixelBorder + 3);
		}

		for (int i = currPoints; i < currentPowerLevel; i++) {
			g.drawImage(pwrFull, null, i * pwrFull.getWidth() + pixelBorder + i * 3,
					playerLives.getHeight() + pixelBorder + 3);
		}
	}

	public void addLives() {
		lives++;
	}

	public void decrLives() {
		lives--;
	}
	
	public boolean isDead(){
		
		return (lives <= 0);
	}

	public void updateScore(int delta) {
		score += delta;
	}

	public boolean updatePower() {

		if (currentPowerLevel == maxLevel) {
			return false;
		}

		return true;
	}

	public void addPower() {

		currPoints++;

		if (currPoints >= currentPowerLevel) {
			currentPowerLevel++;
			currPoints = 0;
		}

	}

	public void removePower() {

		if (currentPowerLevel <= 0) {
			return;
		}

		currPoints -= 2;

		if (currPoints < 0) {
			currPoints = 0;

			currentPowerLevel--;

			if (currentPowerLevel < 0) {
				currentPowerLevel = 0;
			}
		}
	}
	// SETTERS

	public void setLeft(boolean b) {
		this.left = b;
	}

	public void setRight(boolean b) {
		this.right = b;
	}

	public void setUp(boolean b) {
		this.up = b;
	}

	public void setDown(boolean b) {
		this.down = b;
	}

	public void setFiring(boolean b) {
		firing = b;
	}

	public void setShield(boolean b) {
		shield = b;
	}

	// GETTERS

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getR() {
		return r;
	}

	public int getLives() {
		return lives;
	}

	public boolean getShield() {
		return shield;
	}
	
	public int getScore(){
		return score;
	}
		

}
