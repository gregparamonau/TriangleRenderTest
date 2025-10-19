package TriangleRenderTest;

import java.awt.Color;
import java.awt.Graphics;

public class Rect {
	Vector2 pos;
	double width, height;
	
	public Rect(double a, double b, double c, double d) {
		this.pos = new Vector2(a, b);
		this.width = c;
		this.height = d;
	}
	
	public boolean intersect_node(Vector2 in) {
		return (in.x > this.pos.x - this.width / 2 && in.x < this.pos.x + this.width / 2 &&
				in.y > this.pos.y - this.height / 2 && in.y < this.pos.y + this.height / 2);
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.red);
		g.drawRect((int)(this.pos.x - this.width / 2), (int)(this.pos.y - this.height / 2), (int)this.width, (int)this.height);
	}
	
	public double p() {
		return 2 * this.width + 2 * this.height;
	}
}
