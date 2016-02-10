package kuoa.xShooter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PowerUp {

	// power up Type
	public enum Power {
		LIFE, SHIELD, STRENGTH, SLOWDOWN
	}
	// FIELDS

	private int x;
	private int y;
	private int r;
	private int speed;
	Power type;

	BufferedImage powerSprite;

	// CONSTRUCTOR

	public PowerUp(int x, int y, Power type, BufferedImage powerSprite) {

		this.x = x;
		this.y = y;
		this.type = type;
		this.powerSprite = powerSprite;

		r = powerSprite.getWidth() / 2;
		speed = 2;
	}

	// FUNCTIONS

	public boolean update() {

		y += speed;

		if (y > GamePanel.HEIGHT - r || x < 0) {
			return true;
		}

		return false;

	}

	public void draw(Graphics2D g) {

		g.drawImage(powerSprite, null, x, y);

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

	public Power getType() {
		return type;
	}

}
