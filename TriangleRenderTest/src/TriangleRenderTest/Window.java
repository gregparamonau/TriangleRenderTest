package TriangleRenderTest;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window {
	public static int screen_width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static int screen_height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	public static JFrame frame = null;
	public static JPanel pane = null;
	public static Graphics2D g = null;
	
	static BufferedImage img = new BufferedImage(screen_width, screen_height, BufferedImage.TYPE_INT_ARGB);
	
	
	public static void main(String[] args) {
		init();
		
		/*Vector2[] vecs = {
				new Vector2(100, 100),
				new Vector2(100, 300),
				new Vector2(150, 400),
				new Vector2(300, 450),
				new Vector2(450, 400),
				new Vector2(500, 300),
				new Vector2(500, 100),
				new Vector2(400, 100),
				new Vector2(400, 300),
				new Vector2(300, 350),
				new Vector2(200, 300),
				new Vector2(200, 100)
		};*/
		
		Vector2[] vecs = {
			new Vector2(150, 200),
			new Vector2(150, 350),
			new Vector2(200, 450),
			new Vector2(300, 500),
			new Vector2(400, 500),
			new Vector2(475, 450),
			new Vector2(525, 400),
			new Vector2(450, 350),
			new Vector2(400, 400),
			new Vector2(300, 400),
			new Vector2(250, 350),
			new Vector2(250, 300),
			new Vector2(275, 225),
			new Vector2(275, 200),
			new Vector2(300, 175),
			new Vector2(350, 175),
			new Vector2(375, 175),
			new Vector2(400, 200),
			new Vector2(375, 250),
			new Vector2(300, 250),
			new Vector2(275, 225),
			new Vector2(250, 300),
			new Vector2(300, 325),
			new Vector2(400, 325),
			new Vector2(475, 300), 
			new Vector2(500, 250),
			new Vector2(475, 150),
			new Vector2(400, 100),
			new Vector2(300, 100),
			new Vector2(200, 150)
		};
		
		/*Vector2[] vecs = {
				new Vector2(100, 100),
				new Vector2(100, 300),
				new Vector2(400, 300),
				new Vector2(400, 100)
		};*/
		
		/*Vector2[] vecs = {
				new Vector2(100, 100),
				new Vector2(200, 200),
				new Vector2(100, 300),
				new Vector2(100, 400),
				new Vector2(200, 500),
				new Vector2(300, 500),
				new Vector2(400, 400),
				new Vector2(400, 300),
				new Vector2(300, 200),
				new Vector2(400, 100)
		};*/
		
		//pol.draw_polygon(g);
		
		long a = System.nanoTime();
		
		Polygon pol = new Polygon(vecs);
		
		Triangle[] tris = pol.triangulate();
		
		int num = 1000;
		
		//2.4821474589999997 :: 2.39491995
		//2.330441666 :: 2.1888324832999997
		
		for (int y = 0; y< num; y++) {
			
			for (int i = 0; i < 100; i++) {
				//double area = 0;
				long tempa = System.nanoTime();
				//a = System.nanoTime();
				for (int x = 0; x<tris.length; x++) {
					//tris[x].stretch(1.5);
					tris[x].draw_triangle(img);
					
					//area += tris[x].find_area();
				}
				
				long tempb = System.nanoTime();
				
				System.out.println(y + ", " + (double)(tempb - tempa) / 1000000);
				//pol.draw_polygon(g);
				//long b = System.nanoTime();
				//pol.rotate(2 * Math.PI / 100);
				//pol.translate(new Vector2(10, 0));
				//pol.stretch(1.01);
				//System.out.println(i + ", AREA: " + area + " DT: " + (double)(b - a) / 1000000);
			}
		}
		
		/*
		for (int i = 0; i < 100; i++) {
			double area = 0;
			//a = System.nanoTime();
			for (int x = 0; x<tris.length; x++) {
				//tris[x].stretch(1.5);
				tris[x].draw_triangle(img);
				
				area += tris[x].find_area();
			}
			
			pol.draw_polygon(g);
			//long b = System.nanoTime();
			pol.rotate(2 * Math.PI / 100);
			pol.translate(new Vector2(10, 0));
			pol.stretch(1.01);
			//System.out.println(i + ", AREA: " + area + " DT: " + (double)(b - a) / 1000000);
		}*/
		long b = System.nanoTime();
		
		System.out.println("DT TOTAL: " + (double)(b - a) / 1000000 / num);
		
		g.drawImage(img, 0, 0, null);
	}
	
	public static void init() {
		frame = new JFrame();
		frame.setSize(screen_width, screen_height);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		pane = new JPanel();
		pane.setSize(frame.getWidth(), frame.getHeight() - frame.getInsets().top - frame.getInsets().bottom);
		frame.add(pane);
		
		System.out.println("f: " + frame.getWidth() + " " + frame.getHeight());
		System.out.println("p: " + pane.getWidth() + " " + pane.getHeight());
		
		try {Thread.sleep(2000);}catch(Exception e) {e.printStackTrace();}
		g = (Graphics2D) pane.getGraphics();
	}
}
