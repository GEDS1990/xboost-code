package com.mckinsey.ckc.sf.data;

public class Coordinate {
	private double x;
	private double y;
	
	public Coordinate(){
		this.x = 0;
		this.y = 0;
	}
	
	public Coordinate(double x, double y){
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public Coordinate addCoordinate(Coordinate a){
		return new Coordinate(a.getX()+x,a.getY()+y);
	}
	
	public Coordinate minusCoordinate(Coordinate a){
		return new Coordinate(x-a.getX(),y-a.getY());
	}
	
	public Coordinate multiply(double a){
		return new Coordinate(x*a,y*a);
	}
	
	public boolean equals(Coordinate coord){
		if(Math.abs(x-coord.getX()) <= 0.0001 &&
				Math.abs(y-coord.getY()) <= 0.0001){
			return true;
		}
		return false;
	}

}
