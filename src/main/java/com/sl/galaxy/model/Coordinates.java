package com.sl.galaxy.model;

/**
 * Coordinate
 * @author Silvina_Luque
 *
 */
public class Coordinates {
	
	private Double x;
	
	private Double y;
	

	public Coordinates(double x2, double y2) {
		this.x = x2;
		this.y = y2;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Coordinates [x=" + x + ", y=" + y + "]";
	}



}
