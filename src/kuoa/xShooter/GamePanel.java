package kuoa.xShooter;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;

	// FIELDS
	public static int WIDTH = 800;
	public static int HEIGHT = 700;

	private Thread thread;

	private boolean running;

	private BufferedImage image; // canvans
	private BufferedImage background1; // background1 image
	public static BufferedImage pwrLife;
	public static BufferedImage pwrShield;
	public static BufferedImage pwrStrength;
	public static BufferedImage pwrSlowDown;

	private Graphics2D g; // paint brush

	public static Font myFont;

	private int FPS = 60;

	@SuppressWarnings("unused")
	private double averageFPS;

	public static Player player;

	public static ArrayList<Bullet> bullets;

	public static ArrayList<Enemy> enemies;

	public static ArrayList<PowerUp> powerups;

	public static ArrayList<Text> text;
	
	public static ArrayList<Explosion> explosions;
	
	// (IDEA: maybe use a HashTable)

	// SFX & MUSIC
	public static MySound sound;
	public static MySound explosion;
	public static MySound levelFinished;
	public static MySound shieldOn;
	public static MySound shieldOff;
	public static MySound powerUpSpawn;
	public static MySound powerLife;
	public static MySound powerPower;
	public static MySound powerSlowOn;
	public static MySound powerSlowOff;
	public static MySound playerHit;
	public static MySound gameOver;
	public static MySound bg;
	
		

	// wave management
	private double waveStartTimer;
	private double waveTimeDiff;
	private double waveDelay = 2000; // 2s
	private boolean waveStart = false;
	private int waveNumber = 0;

	// shield management
	public static boolean hasShield = false;
	public static double shieldActivationTimer;
	private double shieldDelay = 1500;
	
	// slowTime management
	public static boolean slowDown = false;
	public static double slowDownActivationTimer;
	private int slowDownDelay = 4000;
	private double slowDownActivationDiff;
	
	// bossDivision management
	public static boolean bossDivision = false;
	public static double bossDivisionStart;
	public static int bossDivisonDelay = 1000;
	public static double bossDivisionDiff;	

	// CONSTRUCTOR
	public GamePanel() {

		super();

		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		// for the keyboard input
		setFocusable(true);
		requestFocus();

		try {

			background1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/bg1.png"));
			pwrLife = ImageIO.read(getClass().getResourceAsStream("/Sprites/pwrLife.png"));
			pwrShield = ImageIO.read(getClass().getResourceAsStream("/Sprites/pwrShield.png"));
			pwrStrength = ImageIO.read(getClass().getResourceAsStream("/Sprites/pwrStrength.png"));
			pwrSlowDown = ImageIO.read(getClass().getResourceAsStream("/Sprites/pwrSlowDown.png"));
			
			myFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/Fonts/myFont.ttf"));
			
			sound = new MySound("/SFX/fire.wav");
			explosion = new MySound("/SFX/explosion.wav");
			levelFinished = new MySound("/SFX/levelFinished.wav");
			shieldOn = new MySound("/SFX/shieldOn.wav");
			shieldOff = new MySound("/SFX/shieldOff.wav");
			powerUpSpawn = new MySound("/SFX/powerUpSpawn.wav");
			powerSlowOn = new MySound("/SFX/powerSlowOn.wav");
			powerSlowOff = new MySound("/SFX/powerSlowOff.wav");
			powerPower = new MySound("/SFX/powerPower.wav");
			powerLife = new MySound("/SFX/powerLife.wav");
			playerHit = new MySound("/SFX/playerHit.wav");
			gameOver = new MySound("/SFX/gameOver.wav");
			bg = new MySound("/Music/bgMusic.wav");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// FUNCTIONS

	@Override
	public void addNotify() {
		// this method is a hack used to launche the THREAD and get keyInput

		super.addNotify();
	
		// JPanel is done loading, start the thread
		if (thread == null) {
			// add the GamePanel to the Thread
			thread = new Thread(this);
			thread.start();
		}

		// KeyListener
		addKeyListener(this);
	}

	// THREAD RUN
	@Override
	public void run() {
		// run method used by thread.start()		
		
		running = true;

		// internal image init
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();

		// anti-aliasing graphics
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// anti-aliasing text
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Array list init
		player = new Player();

		bullets = new ArrayList<Bullet>();

		enemies = new ArrayList<Enemy>();

		powerups = new ArrayList<PowerUp>();

		text = new ArrayList<Text>();
		
		explosions = new ArrayList<Explosion>();
		
		// background music
		bg.loop();

		long startTime;
		long URDTimeMillis;
		long waitTime;
		long totalTime = 0;

		int frameCount = 0;
		int maxFrameCount = 60;

		int targetTime = 1000 / FPS; // time for a loop

		
		// GAME LOOP
		while (running) {
			
			startTime = System.nanoTime();

			gameUpdate();
			gameRender();
			gameDraw();

			// 10 ^ 9 * 10 ^ (-6)
			URDTimeMillis = (System.nanoTime() - startTime) / 1000000;
			waitTime = targetTime - URDTimeMillis; // sleep time

			try {
				if (waitTime > 0) {
					Thread.sleep(waitTime);
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}

			// actual loop time
			totalTime += System.nanoTime() - startTime;
			frameCount++;

			if (frameCount == maxFrameCount) {

				// average fps
				averageFPS = 1000.0 / ((totalTime / frameCount) / 1000000);
				frameCount = 0;
				totalTime = 0;
			}
		}
		
		
		// Game Over Screen
		gameOver.play();
		g.drawImage(background1, null, 0, 0);
		
		String gameOver = "G A M E   O V E R";
		g.setColor(Color.WHITE);
		g.setFont(GamePanel.myFont.deriveFont(Font.PLAIN, 48));
		int length = (int) g.getFontMetrics().getStringBounds(gameOver, g).getWidth();
		int height = (int) g.getFontMetrics().getStringBounds(gameOver, g).getHeight();		
		g.drawString(gameOver, (GamePanel.WIDTH - length) / 2, GamePanel.HEIGHT / 3);
		
		String score = "S c o r e : " + player.getScore();
		length = (int) g.getFontMetrics().getStringBounds(score, g).getWidth();		
		g.drawString(score, (GamePanel.WIDTH - length) / 2, GamePanel.HEIGHT / 3 + height);
		
		g.setFont(GamePanel.myFont.deriveFont(Font.PLAIN, 48));
		String author = "http:// k u o a . g i t h u b . i o";
		length = (int) g.getFontMetrics().getStringBounds(author, g).getWidth();		
		g.drawString(author, (GamePanel.WIDTH - length) / 2, GamePanel.HEIGHT / 3 + 200);
		
		
		
		gameDraw();
	}

	private void gameUpdate() {

		// WAVE MANAGEMENT

		if (waveStartTimer == 0 && enemies.size() == 0) {
			waveNumber++;
			waveStart = false;
			waveStartTimer = System.nanoTime();
		}

		else {
			waveTimeDiff = (System.nanoTime() - waveStartTimer) / 1000000;

			if (waveTimeDiff >= waveDelay) {
				waveStart = true;
				waveStartTimer = 0;
				waveTimeDiff = 0;
			}
		}

		// if no enemies on screen !
		if (waveStart && enemies.size() == 0) {
			// createEnemies();		
			Levels.newLevel(waveNumber);
			levelFinished.play();
		
		}

		// SHIELD MANAGEMENT

		double shieldActivationDiff = (System.nanoTime() - shieldActivationTimer) / 1000000;

		// if shield was on for "shielDelay" time, turn it off
		if (hasShield == true && shieldActivationDiff >= shieldDelay) {
			shieldOff.play();
			player.setShield(false);
			hasShield = false;
		}
		
		// is slowdown was on for "SlowDownTimer" time turn it off
		
		slowDownActivationDiff = (System.nanoTime() - slowDownActivationTimer) / 1000000;
		
		if (slowDown == true && slowDownActivationDiff >= slowDownDelay){
			
			powerSlowOff.play();
			
			for (int j = 0; j < enemies.size(); j++) {
							
				enemies.get(j).setSlowed(false);
			}
			slowDownActivationTimer = 0;
			slowDown = false;
		}

		// PLAYER UPDATE
		player.update();
		
		if (player.isDead()){
			// Game Over
			running = false;
		}

		// BULLETS UPDATE
		GameUpdateFunctions.bulletsUpdate();

		// ENEMIES UPDATE
		GameUpdateFunctions.enemiesUpdate();

		// POWERUP UPDATE
		GameUpdateFunctions.powerUpUpdate();

		// TEXT UPDATE
		GameUpdateFunctions.textUpdate();

		// BULLET ENEMY COLLISION
		GameUpdateFunctions.checkBulletEnemyCollision();

		// PLAYER ENEMY COLLISION

		boolean shield = GameUpdateFunctions.checkPlayerEnemyCollision();

		if (shield) {
			// activate shield if needed
			double shieldTimeDiff = (System.nanoTime() - shieldActivationTimer) / 1000000;

			if (shieldTimeDiff >= 2 * shieldDelay) {
				
				shieldOn.play();
				player.setShield(true);
				hasShield = true;
				shieldActivationTimer = System.nanoTime();
			}
		}
		
		
		
		GameUpdateFunctions.explosionUpdate();

		// PLAYER POWER UP COLISION DETECTION
		GameUpdateFunctions.checkPlayerPowerUpCollision();				
	}

	private void gameRender() {
		// draw everything to the off-screen image

		// background
		g.drawImage(background1, null, 0, 0);

		// STATS FOR GEEKS
		
		// g.setColor(Color.WHITE);
		// g.setFont(new Font("Arial", Font.PLAIN, 10));
		// g.drawString("FPS: " + averageFPS, 10, 50);
		// g.drawString("Bullets in memory: " + bullets.size(), 10, 60);
		// g.drawString("Enemies: " + enemies.size(), 10, 70);	

		// player draw
		player.draw(g);

		// bullets draw
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).draw(g);			
		}

		// enemies draw
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}

		// power up draw
		for (int i = 0; i < powerups.size(); i++) {
			powerups.get(i).draw(g);
		}

		// text draw
		for (int i = 0; i < text.size(); i++) {
			text.get(i).draw(g);
		}
		
		// explosions draw
		
		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).draw(g);
		}
						
		if (slowDown){
			// draw slowDown bar
			
			g.setColor(Color.WHITE);		
			g.drawRect(Player.pixelBorder, 67, 100, 15);
			g.fillRect(Player.pixelBorder, 67, (int) (100 - 100 * slowDownActivationDiff / slowDownDelay), 15);
		}
		

		// wave number draw

		if (waveNumber > 0) {
		
			String wave = "";
			int length = 0;
			
			g.setFont(myFont.deriveFont(Font.PLAIN, 48));
			
			// g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
			if (waveNumber % 10 != 0){
				wave = "L E V E L  " + waveNumber;
				length = (int) g.getFontMetrics().getStringBounds(wave, g).getWidth();
			}
			else{
				wave = "B O S S   L E V E L";
				length = (int) g.getFontMetrics().getStringBounds(wave, g).getWidth();				
			}			
			// sin 0 = 0 / sin pi = 1
			int alpha = (int) (255 * Math.sin(Math.PI * waveTimeDiff / waveDelay));

			if (alpha > 255) {
				alpha = 255;
			}			
			g.setColor(new Color(255, 255, 255, alpha));
			g.drawString(wave, (WIDTH - length) / 2, HEIGHT / 3);

		}
	}

	private void gameDraw() {
		// draw off-screen image to the screen

		// panel refference
		Graphics g2 = this.getGraphics();		

		g2.drawImage(image, 0, 0, null);				
		
		g2.dispose();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_LEFT) {
			player.setLeft(true);
		}
		if (keyCode == KeyEvent.VK_RIGHT) {
			player.setRight(true);
		}
		if (keyCode == KeyEvent.VK_UP) {
			player.setUp(true);
		}
		if (keyCode == KeyEvent.VK_DOWN) {
			player.setDown(true);
		}

		if (keyCode == KeyEvent.VK_Z) {
			player.setFiring(true);
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {

		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_LEFT) {
			player.setLeft(false);
		}
		if (keyCode == KeyEvent.VK_RIGHT) {
			player.setRight(false);
		}
		if (keyCode == KeyEvent.VK_UP) {
			player.setUp(false);
		}
		if (keyCode == KeyEvent.VK_DOWN) {
			player.setDown(false);
		}
		if (keyCode == KeyEvent.VK_Z) {
			player.setFiring(false);
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
			

	}
}
