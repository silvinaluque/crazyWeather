package com.sl.galaxy.model;
/**
 * 
 * @author Silvina_Luque
 *
 */
public class Planet {

	private String name; 
	
	/** velocidad  grados por dia */
	private int velocity;
	
	/** direccion de giro */
	private int direction; 
	
	/** radio en km */
	private int distanceToSun;

	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getVelocity() {
		return velocity;
	}
	

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getDistanceToSun() {
		return distanceToSun;
	}

	public void setDistanceToSun(int distanceToSun) {
		this.distanceToSun = distanceToSun;
	}
	
	
}
