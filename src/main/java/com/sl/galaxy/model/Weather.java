package com.sl.galaxy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Weather {
	
	@Id 
	private String  idWeather;
	
	/** numero de dia galactico */
	private int day;
	
	/** tipo de clima */
	private String weatherType;
	
	/** perimetro del triangulo- si no hay triangulo sera 0 */
	private Double perimeter;
	
	
	public String getWeatherType() {
		return weatherType;
	}

	public void setWeatherType(String weatherType) {
		this.weatherType = weatherType;
	}



	public String getIdWeather() {
		return idWeather;
	}

	public void setIdWeather(String idWeather) {
		this.idWeather = idWeather;
	}



	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}



	public Double getPerimeter() {
		return perimeter;
	}

	public void setPerimeter(Double perimeter) {
		this.perimeter = perimeter;
	}
	
	

}
