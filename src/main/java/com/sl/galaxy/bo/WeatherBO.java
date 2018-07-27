package com.sl.galaxy.bo;

import java.util.Collection;
import com.sl.galaxy.exception.WeatherBOException;
import com.sl.galaxy.model.Galaxy;
import com.sl.galaxy.model.Weather;
import com.sl.galaxy.model.WeatherType;
/**
 * Bussines Interface
 * @author Silvina_Luque
 *
 */
public interface WeatherBO {
	/**
	 * Inicializacion de datos del clima
	 * @param galaxyWeather
	 * @return
	 */
	void galaxyWeatherInitDB(Galaxy galaxyWeather) throws WeatherBOException;

	/**
	 * Delete de datos del clima
	 * @return
	 */
	void galaxyWeatherDeleteDB() throws WeatherBOException;

	/**
	 * Clima del dia
	 * @param day
	 * @return
	 */
	Weather getWeatherByDay(String day) throws WeatherBOException;

	/**
	 * 	
	 * @param wType
	 * @return
	 */
	Collection<Weather> getWeatherByType(WeatherType wType) throws WeatherBOException;
	
}
