package kuoa.xShooter;

import java.awt.Color;
import java.awt.Graphics2D;

public class Bullet {

	// FIELDS
	private double x;
	private double y;
	private double rad;

	private int r;
	private int speed;

	private double dx;
	private double dy;

	private Color color1;

	// CONSTRUCTOR
	public Bullet(double x, double y, double angle) {

		this.x = x;
		this.y = y;

		rad = Math.toRadians(angle);

		r = 2;
		speed = 15;

		dx = Math.cos(rad) * speed;
		dy = Math.sin(rad) * speed;

		color1 = new Color(241, 240, 255);

	}

	public boolean update() {
		// return true if bulet needs to be removed

		x += dx;
		y += dy;

		if (x < 0 || x > GamePanel.WIDTH || y < 0 || y > GamePanel.HEIGHT) {
			return true;
		}

		return false;
	}

	public void draw(Graphics2D g) {

		g.setColor(color1);
		g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r); // center
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

}
