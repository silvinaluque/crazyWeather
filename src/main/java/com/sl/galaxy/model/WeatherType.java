package com.sl.galaxy.model;
/**
 * 
 * @author Silvina_Luque
 *
 */
public enum WeatherType {

	OPTIMO("OPTIMO"),
	
	LLUVIA("LLUVIA"),
	
	SEQUIA("SEQUIA"),
	
	INESTABLE("INESTABLE");
	
	private String description;
	
	WeatherType(String description){
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public static WeatherType getWeatherTypeByDescription(String weatherType) {
		WeatherType[] values = WeatherType.values();
        for (WeatherType type : values) {
            if (weatherType.trim().equalsIgnoreCase(type.getDescription())) {
                return type;
            }
        }
        return null;
		
	}

	
	
}
