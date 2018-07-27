package com.sl.galaxy.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Weather galaxy
 * @author Silvina_Luque
 *
 */
public class Galaxy {

	private List<Planet> planets = new ArrayList<Planet>();

	private int totalDays;
	
	private int precision;
	
	
	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
	}

	public List<Planet> getPlanets() {
		return planets;
	}

	public void setPlanets(List<Planet> planets) {
		this.planets = planets;
	}
	
	
	
	
}
