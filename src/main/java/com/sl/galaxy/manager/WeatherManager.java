package com.sl.galaxy.manager;



import com.sl.galaxy.model.WeatherResponse;

/**
 * WeatherManager
 * @author Silvina_Luque
 *
 */
public interface WeatherManager {
	
	/**
	 * Inicia db
	 * @return
	 */
	WeatherResponse galaxyWeatherInitDB();

	/**
	 * Elimina bd
	 * @return
	 */
	WeatherResponse galaxyWeatherDeleteDB();

	/**
	 * Obtiene el clima de un dia en particular
	 * @param day
	 * @return
	 */
	WeatherResponse galaxyWeatherByDay(String day);

	/**
	 * Retorna los dias de un tipo de clima
	 * @param weatherType
	 * @return
	 */
	WeatherResponse galaxyWeatherByType(String weatherType);

	/**
	 * Dia pico de lluvia
	 * @return
	 */
	WeatherResponse galaxyWeatherDayMoreRained();

}
