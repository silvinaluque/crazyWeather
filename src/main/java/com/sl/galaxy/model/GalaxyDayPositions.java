package com.sl.galaxy.model;

import java.util.ArrayList;
import java.util.List;
/**
 * Lista de coordenadas de los planetas de la galaxia
 * Posiciones de los planetas en un dia
 * @author Silvina_Luque
 *
 */
public class GalaxyDayPositions {
	
	private List<Coordinates> positionsPlanets = new ArrayList<Coordinates>();

	public List<Coordinates> getPositionsPlanets() {
		return positionsPlanets;
	}

	public void setPositionsPlanets(List<Coordinates> positionsPlanets) {
		this.positionsPlanets = positionsPlanets;
	}
	
	
 
}
