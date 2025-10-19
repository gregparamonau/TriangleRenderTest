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
		//Vector2 ac = Vector2.sub(this.c, this.a);
		//Vector2 ab = Vector2.sub(this.b, this.a);
		
		return 0.5 * ((this.c.y - this.a.y) * (this.b.x - this.a.x) - (this.c.x - this.a.x) * (this.b.y - this.a.y));
		
		//return 0.5 * Math.sqrt((ac.l() * ab.l()) * (ac.l() * ab.l()) - Vector2.dot(ac, ab) * Vector2.dot(ac, ab));
	}
	
	public void bounds() {
		this.min_x = (int)Math.min(Math.min(this.a.x, this.b.x), this.c.x) - 1;
		this.min_y = (int)Math.min(Math.min(this.a.y, this.b.y), this.c.y) - 1;
		this.max_x = (int)Math.max(Math.max(this.a.x, this.b.x), this.c.x) + 1;
		this.max_y = (int)Math.max(Math.max(this.a.y, this.b.y), this.c.y) + 1;
	}

	public int give_color_from_tex(int x, int y, double inv_area) {
		
		double t1 = 0.5 * ((y - this.a.y) * (this.b.x - this.a.x) - (x - this.a.x) * (this.b.y - this.a.y)) * inv_area;
		double t2 = 0.5 * ((y - this.b.y) * (this.c.x - this.b.x) - (x - this.b.x) * (this.c.y - this.b.y)) * inv_area;
		double t3 = 0.5 * ((y - this.c.y) * (this.a.x - this.c.x) - (x - this.c.x) * (this.a.y - this.c.y)) * inv_area;
				
				//new Triangle(p, this.a, this.b, 0).area() / area;
		//double t2 = new Triangle(p, this.b, this.c, 0).area() / area;
		//double t3 = new Triangle(p, this.c, this.a, 0).area() / area;
		
		//System.out.println("AT: " + this.at + " BT: " + this.bt + " CT: " + this.ct);
		//System.out.println("t1: " + t1 + " t2: " + t2 + " t3: " + t3 + " SUM: " + (t1 + t2 + t3));
		
		int u = (int)clamp(this.ct.x * t1 + this.at.x * t2 + this.bt.x * t3, 0, tex.getWidth() -1);
		int v = (int)clamp(this.ct.y * t1 + this.at.y * t2 + this.bt.y * t3, 0, tex.getHeight() -1);
		
		//Vector2 uv = Vector2.add(Vector2.add(this.ct.mult(t1), this.at.mult(t2)), this.bt.mult(t3));
		
		return this.tex.getRGB(u, v);
	}
	
	public static double clamp(double in, double min, double max) {
		return Math.max(min, Math.min(in, max));
	}
	
	public void draw_triangle(BufferedImage img) {
		this.bounds();
		
		int w = img.getWidth(), h = img.getHeight();
		int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		
		long a = System.nanoTime();
		
		double inv_area = 1 / this.find_area();
		
		for (int y = this.min_y ; y < this.max_y ; y++) {
			
			if (y < 0 || y >= h) continue;
			for (int x = this.min_x; x < this.max_x; x++) {
				if (x < 0 || x >= w) continue;
				
				if ((x - this.a.x) * (this.b.y - this.a.y) + (y - this.a.y) * (this.a.x - this.b.x) >= 0 &&
					(x - this.b.x) * (this.c.y - this.b.y) + (y - this.b.y) * (this.b.x - this.c.x) >= 0 &&
					(x - this.c.x) * (this.a.y - this.c.y) + (y - this.c.y) * (this.c.x - this.a.x) >= 0) {
					
					double t1 = 0.5 * ((y - this.a.y) * (this.b.x - this.a.x) - (x - this.a.x) * (this.b.y - this.a.y)) * inv_area;
					double t2 = 0.5 * ((y - this.b.y) * (this.c.x - this.b.x) - (x - this.b.x) * (this.c.y - this.b.y)) * inv_area;
					double t3 = 0.5 * ((y - this.c.y) * (this.a.x - this.c.x) - (x - this.c.x) * (this.a.y - this.c.y)) * inv_area;
					
					int u = (int)clamp(this.ct.x * t1 + this.at.x * t2 + this.bt.x * t3, 0, tex.getWidth() -1);
					int v = (int)clamp(this.ct.y * t1 + this.at.y * t2 + this.bt.y * t3, 0, tex.getHeight() -1);
					
					int col = tex.getRGB(u, v);
					
					//next line is where you can apply any shaders wanted
					pixels[y * w + x] = col;// | (col / 2);// ^ 0b00000000000000001111111100000000;
				}
				
			}
		}
		long b = System.nanoTime();
		
		System.out.println("ID: " + this.id + " AREA: " + this.find_area() + " DT: " + (double)(b - a) / 1000000);
	}
}
