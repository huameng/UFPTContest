class Point {
	int x, y; 
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return this.x + " " + this.y;
	}
	
	public boolean equals(Object o) {
		Point p = (Point)o;
		return (p.x == this.x && p.y == this.y);
	}
}
