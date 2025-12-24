package TriangleRenderTest;

public class Vector2 {
	double x, y;
	
	public Vector2() {}
	
	public Vector2(double a, double b) {
		this.x = a;
		this.y = b;
	}
	public Vector2(Vector2 in ) {
		this.x = in.x;
		this.y = in.y;
	}
	
	public void set(Vector2 in) {
		this.x = in.x;
		this.y = in.y;
	}
	public double angle() {
		double a = Math.atan2(this.y, this.x);
		
		if (a >= 0) return a;
		
		return 2 * Math.PI + a;
	}
	//static methods
	
	public static Vector2 add(Vector2 a, Vector2 b) {
		return new Vector2(a.x + b.x, a.y + b.y);
	}
	public static Vector2 sub(Vector2 a, Vector2 b) {
		return new Vector2(a.x - b.x, a.y - b.y);
	}
	
	public static Vector2 mult(Vector2 in, double a) {
		return new Vector2(in.x * a, in.y * a);
	}
	
	public static double angle(Vector2 a, Vector2 b) {
		//return a.angle() - b.angle();
		
		double theta = Math.acos(clamp(Vector2.dot(a, b) / a.l() / b.l(), -1, 1));
		
		if (Math.acos(clamp(Vector2.dot(a.rotate(0.00000000001), b) / a.l() / b.l(), -1, 1)) > theta) {
			return -theta;
		}
		return theta;
		//return Math.atan2(b.y, b.x) - Math.atan2(a.y, a.x);
		//return Math.acos(clamp(Vector2.dot(a, b) / a.l() / b.l(), -1, 1));
	}
	
	public static double dot(Vector2 a, Vector2 b) {
		return (a.x * b.x + a.y * b.y);
	}
	
	public static Vector2 reflect(Vector2 in, Vector2 norm) {
		return Vector2.sub(Vector2.mult(norm, 2 * Vector2.dot(in, norm)), in);
	}
	
	public static double dist(Vector2 a, Vector2 b) {
		return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
	}
	public static double clamp(double in, double min, double max) {
		return Math.max(min, Math.min(in, max));
	}
	
	
	//instance methods
	public void add(Vector2 in) {
		this.x += in.x;
		this.y += in.y;
	}
	public void sub(Vector2 in) {
		this.x -= in.x;
		this.y -= in.y;
	}
	
	public Vector2 mult(double in) {
		return new Vector2(this.x * in, this.y * in);
	}
	public Vector2 norm() {
		double l = this.l();
		
		if (l == 0) return new Vector2(0, 0);
		return new Vector2(this.x / l, this.y / l);
	}
	public double l() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	public Vector2 perp() {	
		Vector2 temp = Vector2.mult(this, (double)1 / this.l());
		
		return new Vector2(temp.y, -temp.x);
	}
	
	public Vector2 rotate(double theta) {
	    double cos = Math.cos(theta);
	    double sin = Math.sin(theta);
	    return new Vector2(this.x * cos - this.y * sin,
	                       this.x * sin + this.y * cos);
	}
	
	public static Vector2[] remove_from_vec_arr(Vector2[] arr, int index) {
		Vector2[] out = new Vector2[arr.length - 1];
		
		for (int x = 0; x<out.length; x++) {
			out[x] = arr[(x < index ? x : x + 1)];
		}
		
		return out;
		
	}
	
	public String toString() {
		return "x: " + this.x + " y: " + this.y;
	}
}
