package TriangleRenderTest;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Polygon {
	public Vector2[] pnts = new Vector2[0];
	public Vector2[] pntst = new Vector2[0];
	public BufferedImage tex = new BufferedImage(1, 1, 1);
	
	static String file = "/smile.png";
	
	public Polygon(Vector2[] in) {
		
		this.pnts = new Vector2[in.length];
		this.pntst = new Vector2[in.length];
		
		for (int x = 0; x<in.length; x++) {
			this.pnts[x] = new Vector2(in[x]);
			this.pntst[x] = new Vector2(in[x]);
		}
		
		//this.rotate(Math.PI / 2);
		//this.stretch(1);
		//this.translate(2000);
		
		for (int x = 0; x<in.length; x++) {
			System.out.println("IN: " + in[x] + " PNTS: " + this.pntst[x] + " PNTST: " + this.pntst[x]);
		}
		
		this.start_texture();
	}
	
	public void start_texture() {
		try {
			this.tex = ImageIO.read(getClass().getResource(file));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Triangle[] triangulate() {
		//TODO: rewrite triangulation method so that the triangles are assigned vertices 
		//that can be modified by the polygon method. This allows dynamic alteration of an image
		//without the need to set them manually
		//can be done by creating an int[X][3], where each cell of int[] holds three indices which
		//correspond to three vertices
		
		long start = System.nanoTime();
		
		int[][] indices = new int[0][3];
		//ear clipping core
		Triangle[] out = new Triangle[0];
		
		int[] temp_indices = new int[this.pnts.length];
		
		for (int x = 0; x<temp_indices.length; x++) temp_indices[x] = x;
		
		boolean done = temp_indices.length == 3;
		int index = 1;
		
		while(!done) {
			System.out.println(index + " out: " + temp_indices.length);// + " vecs: " + vecs.length);
			
			//check if points are wound CW
			System.out.println((index - 1 + temp_indices.length) % temp_indices.length + " " + index);
			Vector2 a = Vector2.sub(this.pnts[temp_indices[(index - 1 + temp_indices.length) % temp_indices.length]], this.pnts[temp_indices[index]]);
			Vector2 b = Vector2.sub(this.pnts[temp_indices[(index + 1) % temp_indices.length]], this.pnts[temp_indices[index]]);
			
			if (Vector2.dot(a.rotate(0.0001), b) < Vector2.dot(a, b)) {
				index = (index + 1) % temp_indices.length;
				continue;
			}
			
			//check if any polygon points inside the created triangle
			
			int[] tri_indices = new int[] {
				    temp_indices[(index - 1 + temp_indices.length) % temp_indices.length],
				    temp_indices[index],
				    temp_indices[(index + 1) % temp_indices.length]
				};
			Triangle tri = new Triangle(this.pnts[temp_indices[(index - 1 + temp_indices.length) % temp_indices.length]], this.pnts[temp_indices[index]], this.pnts[temp_indices[(index + 1) % temp_indices.length]], 0);
			//tri.add_texture(vecst[(index - 1 + vecst.length) % vecst.length], vecst[index], vecst[(index + 1) % vecst.length], tex);
			
			
			boolean brk = false;
			for (int x = 0; x<this.pnts.length; x++) {
				
				if (tri.a.equals(this.pnts[x]) || tri.b.equals(this.pnts[x]) || tri.c.equals(this.pnts[x])) continue;
				if (tri.intersect((int)this.pnts[x].x, (int)this.pnts[x].y, false)) {
					index = (index + 1) % temp_indices.length;
					brk = true;
					break;
				}
			}
			if (brk) continue;
			
			indices = this.add_to_indices(indices, tri_indices);
			
			temp_indices = this.remove_from_int_arr(temp_indices, index);
			
			done = temp_indices.length == 2;
			
		}	
		
		for (int x = 0; x<indices.length; x++) {
			Triangle tri = new Triangle(this.pnts[indices[x][0]], this.pnts[indices[x][1]], this.pnts[indices[x][2]], x);
			tri.add_texture(this.pnts[indices[x][0]], this.pnts[indices[x][1]], this.pnts[indices[x][2]], tex);
			out = Triangle.add_to_tri_arr(out, tri);
		}
		
		long end = System.nanoTime();
		
		System.out.println("TRIANGULATE: " + (double)(end - start) / 1000000);
		return out;
	}
	
	public int[][] add_to_indices(int[][] in, int[] add) {
		int[][] out = new int[in.length + 1][3];
		
		for (int x = 0; x<in.length; x++) {
			for (int y = 0; y<3; y++) {
				out[x][y] = in[x][y];
			}
		}
		
		out[in.length] = add;
		
		return out;
	}
	public int[] remove_from_int_arr(int[] in, int index) {
		int[] out = new int[in.length - 1];
		
		for (int x = 0; x<out.length; x++) {
			out[x] = in[(x < index ? x : x + 1)];
		}
		return out;
	}
	
	public void stretch(double in) {
		for (int x = 0; x<this.pnts.length; x++) {
			this.pnts[x].set(this.pnts[x].mult(in));
			//this.pnts[x] = this.pnts[x].mult(in);
		}
	}
	public void rotate(double in) {
		Vector2 center = new Vector2(0, 0);
		for (int x = 0; x<this.pnts.length; x++) {
			center = Vector2.add(center, this.pnts[x]);
			//System.out.println("CENTER: " + center);
		}
		center = center.mult(1.0 / this.pnts.length);
		//System.out.println("CENTER: " + center);
		
		// c -s
		// s c
		for (int x = 0; x<this.pnts.length; x++) {
			this.pnts[x].set(Vector2.add(center, Vector2.sub(this.pnts[x], center).rotate(in)));
			
			//System.out.println("INDEX: " + x + " " + this.pnts[x]);
		}
		//for (int x =)
	}
	
	public void translate(Vector2 in) {
		for (int x = 0; x<this.pnts.length; x++) {
			this.pnts[x].x += in.x;
			this.pnts[x].y += in.y;
			//this.pnts[x].y *= in;
			//this.pnts[x] = this.pnts[x].mult(in);
		}
	}
	
	public int[][] to_polygon(Vector2[] in) {
		int[][] out = new int[2][this.pnts.length];
		
		for (int x = 0; x<out[0].length; x++) {
			out[0][x] = (int)in[x].x;
			out[1][x] = (int)in[x].y;

		}
		
		return out;
	}
	
	public void draw_polygon(Graphics g) {
		int[][] temp = this.to_polygon(this.pnts);
		
		g.setColor(Color.red);
		g.drawPolygon(temp[0], temp[1], temp[0].length);
	}
	
}
