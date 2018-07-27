package com.sl.galaxy.se;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sl.galaxy.manager.WeatherManager;

import com.sl.galaxy.model.WeatherResponse;

/**
 * Rest controller
 * @author Silvina_Luque
 *
 */
@RestController
@RequestMapping(value = "/")
public class WeatherServiceEndpoint {

	private final Logger LOGGER = LoggerFactory.getLogger(WeatherServiceEndpoint.class);
	
	@Autowired
	WeatherManager weatherManager;


	/**
	 * Cargar datos del clima
	 * @return
	 */
	@RequestMapping(value = "/galaxyWeatherInitDB", method = RequestMethod.GET)
	public WeatherResponse galaxyWeatherInitDB() {
		LOGGER.info("Se van a inicializar los datos del clima.......");
		return   weatherManager.galaxyWeatherInitDB();

	}
	
	/**
	 * Borrar datos del clima
	 * @return
	 */
	@RequestMapping(value = "/galaxyWeatherDeleteDB", method = RequestMethod.GET)
	public WeatherResponse galaxyWeatherDeleteDB() {
		LOGGER.info("Se van a eliminar todos los datos del clima.......");
		return   weatherManager.galaxyWeatherDeleteDB();

	}

	/**
	 * Bienvendia
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "--------------GALAXY WEATHER----------SIL LUQUE------------------";

	}
	
	/**
	 * Clima en un dia en particular
	 * @param day
	 * @return
	 */
	@RequestMapping(value = "/clima/{day}", method = RequestMethod.GET)
	public WeatherResponse getWeatherByDay(@PathVariable String day) {
		LOGGER.info("Obteniendo el clima para el dia: {}.", day);
		return weatherManager.galaxyWeatherByDay(day);
	}
	
	/**
	 * Dias con un tipo de lluvia
	 * @param weatherType
	 * @return
	 */
	@RequestMapping(value = "/periodos/{weatherType}", method = RequestMethod.GET)
	public WeatherResponse getWeatherByWeatherType(@PathVariable String weatherType) {
		LOGGER.info("Obteniendo el clima para el dia: {}.", weatherType);
		return weatherManager.galaxyWeatherByType(weatherType);
	}
	
	/**
	 * Dia pico de lluvia
	 * @return
	 */
	@RequestMapping(value = "/diaMaxLluvia", method = RequestMethod.GET)
	public WeatherResponse getDayMoreRained() {
		LOGGER.info("Se va a buscar el dia de lluvia máxima");
		return   weatherManager.galaxyWeatherDayMoreRained();

	}


}