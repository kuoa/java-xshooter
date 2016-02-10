package kuoa.xShooter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Text {

	// FIELDS

	private double x;
	private double y;

	private String text;
	private double textStart;
	private double textDelay;
	double textTimeDiff;

	public Text(double x, double y, String text) {

		this.x = x;
		this.y = y;
		this.text = text;
		this.textDelay = 1000;
		textStart = System.nanoTime();
		textTimeDiff = 0;
	}

	public boolean update() {

		// if true then remove

		textTimeDiff = (System.nanoTime() - textStart) / 1000000;

		if (textTimeDiff >= textDelay) {
			return true;
		}

		return false;
	}

	public void draw(Graphics2D g) {

		g.setFont(GamePanel.myFont.deriveFont(Font.PLAIN, 26));
		int length = (int) g.getFontMetrics().getStringBounds(text, g).getWidth();

		int alpha = (int) (255 * Math.sin(Math.PI * textTimeDiff / textDelay));

		if (alpha > 255) {
			alpha = 255;
		}

		g.setColor(new Color(255, 255, 255, alpha));

		g.drawString(text, (int) x - length / 2, (int) y);
	}
}
