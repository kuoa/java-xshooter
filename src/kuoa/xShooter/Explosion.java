package kuoa.xShooter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Explosion {

	// FIELDS
	
	private double x;
	private double y;

	private int r;
	private int maxR;	
	private Color color1;
	private int red;
	private int green;
	private int blue;
	
	public Explosion (double x, double y, int r){
		
		this.x = x;
		this.y = y;
		this.r = r;
		maxR = r + 35;
		
		color1 = Color.WHITE;
		red =  (int) (Math.random() * 128 + 128);
		green = (int) (Math.random() * 128 + 128);
		blue = (int) (Math.random() * 128 + 128);
	}
	 
	
	public boolean update() {
		
		r++;		
		return (r >= maxR);
			
	}

	public void draw(Graphics2D g) {
		
		// use alpha it may look coolor (sin r / max R)
		int alpha = (int) (255 * Math.sin(Math.PI * r / maxR));
		
		if (alpha > 255){
			alpha = 255;
		}
		
			
		color1 = new Color(red, green, blue, alpha);
		g.setColor(color1);	
		g.setStroke(new BasicStroke(2));
		g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
		g.setStroke(new BasicStroke(1));
		
	}			
}
