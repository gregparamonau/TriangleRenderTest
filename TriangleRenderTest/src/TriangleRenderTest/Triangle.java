package TriangleRenderTest;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;

public class Triangle {
	int id;
	Vector2 a, b, c;
	Vector2 at, bt, ct;
	int min_x, max_x, min_y, max_y;
	//Rect bounds;
	BufferedImage tex;
	Color fill = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
	
	public Triangle(Vector2 a, Vector2 b, Vector2 c, int id) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.bounds();
		this.id = id;
	}
	
	public void add_texture(Vector2 a, Vector2 b, Vector2 c, BufferedImage texture) {
		this.tex = texture;
		
		this.at = new Vector2(a);
		this.bt = new Vector2(b);
		this.ct = new Vector2(c);
	}
	
	public void stretch(double in) {
		this.a.x *= in;
		this.b.x *= in;
		this.c.x *= in;
		
		this.bounds();
	}
	
	
	public boolean intersect(int x, int y, boolean allow_zero) {
		
		double ab = (x - this.a.x) * (this.b.y - this.a.y) + (y - this.a.y) * (this.a.x - this.b.x);
		double bc = (x - this.b.x) * (this.c.y - this.b.y) + (y - this.b.y) * (this.b.x - this.c.x);
		double ca = (x - this.c.x) * (this.a.y - this.c.y) + (y - this.c.y) * (this.c.x - this.a.x);
		
		return (((ab >= 0 && allow_zero) || (ab > 0)) && ((bc >= 0 && allow_zero) || (bc > 0)) && ((ca >= 0 && allow_zero) || (ca > 0)));
	}
	
	
	public static Triangle[] add_to_tri_arr(Triangle[] arr, Triangle in) {
		Triangle[] out = new Triangle[arr.length + 1];
		
		for (int x = 0; x<arr.length; x++) {
			out[x] = arr[x];
		}
		
		out[arr.length] = in;
		
		return out;
	}
	
	public double find_area() {
		
		return 0.5 * ((this.c.y - this.a.y) * (this.b.x - this.a.x) - (this.c.x - this.a.x) * (this.b.y - this.a.y));
		
	}
	
	public void bounds() {
		this.min_x = (int)Math.min(Math.min(this.a.x, this.b.x), this.c.x) - 1;
		this.min_y = (int)Math.min(Math.min(this.a.y, this.b.y), this.c.y) - 1;
		this.max_x = (int)Math.max(Math.max(this.a.x, this.b.x), this.c.x) + 1;
		this.max_y = (int)Math.max(Math.max(this.a.y, this.b.y), this.c.y) + 1;
	}
	
	public static double clamp(double in, double min, double max) {
		return Math.max(min, Math.min(in, max));
	}
	
	public void draw_triangle(BufferedImage img) {
		this.bounds();
		
		//prep the inverse slopes of all lines, start and end ys, and x at highest point
		//x = my + b
		//m -> dx/dy hence minv
		//b -> x-int hence binv
		
		//AB
		double minvAB = (this.a.y == this.b.y ? Double.NaN : (this.b.x - this.a.x) / (this.b.y - this.a.y));
		double binvAB = (this.a.y == this.b.y ? Double.NaN : (this.b.x - minvAB * this.b.y));
		double min_AB = Math.min(this.a.y, this.b.y), max_AB = Math.max(this.a.y, this.b.y);
		
		//BC
		double minvBC = (this.b.y == this.c.y ? Double.NaN : (this.c.x - this.b.x) / (this.c.y - this.b.y));
		double binvBC = (this.b.y == this.c.y ? Double.NaN : (this.c.x - minvBC * this.c.y));
		double min_BC = Math.min(this.b.y, this.c.y), max_BC = Math.max(this.b.y, this.c.y);
		
		//CA
		double minvCA = (this.c.y == this.a.y ? Double.NaN : (this.a.x - this.c.x) / (this.a.y - this.c.y));
		double binvCA = (this.c.y == this.a.y ? Double.NaN : (this.a.x - minvCA * this.a.y));
		double min_CA = Math.min(this.c.y, this.a.y), max_CA = Math.max(this.c.y, this.a.y);
		
		
		int w = img.getWidth(), h = img.getHeight();
		int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		
		//suggestion to replace all doubles with floats (faster?)
		double inv_area = 1 / this.find_area();
		
		//precompute all calculations
		int texW = tex.getWidth() - 1, texH = tex.getHeight() - 1;
		
		for (int y = this.min_y ; y < this.max_y ; y++) {
			
			if (y < 0 || y >= h) continue;
			
			//determine the start and end points of the region to shade
			
			double start = Double.POSITIVE_INFINITY, end = Double.NEGATIVE_INFINITY;
			
			//AB first
			if (y >= min_AB && y <= max_AB) {
				double temp = minvAB * y + binvAB;
				start = temp < start ? temp : start;
				end = temp > end ? temp : end;
			}
			
			//BC
			if (y >= min_BC && y <= max_BC) {
				double temp = minvBC * y + binvBC;
				start = temp < start ? temp : start;
				end = temp > end ? temp : end;
			}
			
			//CA
			if (y >= min_CA && y <= max_CA) {
				double temp = minvCA * y + binvCA;
				start = temp < start ? temp : start;
				end = temp > end ? temp : end;
			}
			
			for (int x = (int)start; x < (int)(end + 1.0); x++) {
				if (x < 0 || x >= w) continue;
				
				double t1 = 0.5 * ((y - this.a.y) * (this.b.x - this.a.x) - (x - this.a.x) * (this.b.y - this.a.y)) * inv_area;
				double t2 = 0.5 * ((y - this.b.y) * (this.c.x - this.b.x) - (x - this.b.x) * (this.c.y - this.b.y)) * inv_area;
				double t3 = 0.5 * ((y - this.c.y) * (this.a.x - this.c.x) - (x - this.c.x) * (this.a.y - this.c.y)) * inv_area;
				
				//can replace clamp with a faster function (maybe inline it...?)
				//pre-compute texture width and height									VVVV here
				int u = (int)clamp(this.ct.x * t1 + this.at.x * t2 + this.bt.x * t3, 0, texW);
				int v = (int)clamp(this.ct.y * t1 + this.at.y * t2 + this.bt.y * t3, 0, texH);
				
				int col = tex.getRGB(u, v);
				
				//next line is where you can apply any shaders wanted
				pixels[y * w + x] = col;// | (col / 2);// ^ 0b00000000000000001111111100000000;
				
			}
		}
		
		int[] xpos = {(int)this.a.x, (int)this.b.x, (int)this.c.x};
		int[] ypos = {(int)this.a.y, (int)this.b.y, (int)this.c.y};
		Graphics g = img.getGraphics();
		
		g.setColor(Color.red);
		
		g.drawPolygon(xpos, ypos, 3);
		
	}
}
