package com.sl.galaxy.manager;



import java.util.Collection;
import java.util.Comparator;

import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import com.sl.galaxy.bo.WeatherBO;
import com.sl.galaxy.exception.WeatherBOException;
import com.sl.galaxy.model.Galaxy;
import com.sl.galaxy.model.Planet;
import com.sl.galaxy.model.Weather;
import com.sl.galaxy.model.WeatherResponse;
import com.sl.galaxy.model.WeatherType;


/**
 * Weather Manager 
 * @author Silvina_Luque
 *
 */
@Component
public class WeatherManagerImpl implements WeatherManager {
	

	private static final Logger LOGGER = LoggerFactory.getLogger(WeatherManagerImpl.class);
	
	@Autowired
	WeatherBO weatherBO;
	
    @Value("${galaxy.precision}")
	private int precision;
	
    @Value("${galaxy.planets}")
	private String galaxyPlanets;
    
    @Value("${galaxy.totalDays}")
    private  int totalDays;
    
	@Override
	public WeatherResponse galaxyWeatherInitDB() {
		LOGGER.debug("WeatherManager se van a cargar los datos de la galaxia");
		WeatherResponse response = new WeatherResponse();
		try {
			Galaxy galaxyWeather = loadGalaxy();
			weatherBO.galaxyWeatherDeleteDB();
			weatherBO.galaxyWeatherInitDB(galaxyWeather);
			response.setCode("OK");
			response.setMessage("Finalizó la carga de datos inciales correctamente");
		} catch (WeatherBOException e) {
			LOGGER.error("Error inicializando galaxia: {}" , e.getMessage());
			response.setCode(WeatherResponse.ERROR_GENERICO);;
			response.setMessage(WeatherResponse.MESSAGE_ERROR_DB);
		}

		return response;
		
		
	}
	
	@Override
	public WeatherResponse galaxyWeatherDeleteDB() {
		LOGGER.debug("WeatherBO Se van a eliminar todos los registros del clima....");
		WeatherResponse response = new WeatherResponse();
		try {
			weatherBO.galaxyWeatherDeleteDB();
			response.setCode("OK");
			response.setMessage("Se eliminaron todos los registros del clima");
			return response;
		} catch (WeatherBOException e) {
			LOGGER.error("Error inicializando galaxia: {}" , e.getMessage());
			response.setCode(WeatherResponse.ERROR_GENERICO);;
			response.setMessage(WeatherResponse.MESSAGE_ERROR_DB);
			return response;
		}		

		
	}

	@Override
	public WeatherResponse galaxyWeatherByDay(String day) {
		WeatherResponse response = new WeatherResponse();
		LOGGER.debug("WeatherBO buscando datos climaticos del dia: {}" , day);

		try {
			if( Integer.parseInt(day) < 0 || Integer.parseInt(day) >  totalDays) {
				LOGGER.debug("WeatherBO dia invalido: {}" , day);
				response.setCode(WeatherResponse.ERROR_INPUT_PARAM);
				response.setMessage(WeatherResponse.MESSAGE_ERROR_INPUT_DAY);
				return response;
			}
			Weather weather = weatherBO.getWeatherByDay(day);
			response.setCode(WeatherResponse.OK);
			response.setMessage("Dia:" + day + "   Clima:" +  weather.getWeatherType());
		}catch(WeatherBOException boE){
			LOGGER.error("Error buscando el clima del dia: {} . Mensaje:", day, boE.getMessage());
			response.setCode(WeatherResponse.ERROR_GENERICO);;
			response.setMessage(WeatherResponse.MESSAGE_ERROR_DB);
		}catch(Exception e) {
			LOGGER.error("Error buscando el clima del dia: {} . Mensaje:", day, e.getMessage());
			response.setCode(WeatherResponse.ERROR_GENERICO);;
			response.setMessage(WeatherResponse.MESSAGE_ERROR_INPUT_PARAM);
		}
			
		return response;
	}

	@Override
	public WeatherResponse galaxyWeatherByType(String weatherType) {
		WeatherResponse response = new WeatherResponse();
		LOGGER.debug("WeatherBO buscando datos climaticos tipo: {}" , weatherType);
		WeatherType wType = WeatherType.getWeatherTypeByDescription(weatherType);
		if(weatherType != null) {
			try {
				Collection<Weather> weatherList = weatherBO.getWeatherByType(wType);
				response.setCode(WeatherResponse.OK);
				response.setMessage(buildResponseWeatherByType(weatherList, wType.getDescription()));
			}catch(WeatherBOException boE){
				LOGGER.error("Error buscando clima por tipo: {} .Mensaje: {}", weatherType, boE.getMessage());
				response.setCode(WeatherResponse.ERROR_GENERICO);;
				response.setMessage(WeatherResponse.MESSAGE_ERROR_DB);
			}
		
		}else {
			LOGGER.debug("WeatherBO tipo clima invalido invalido: {}" , weatherType);
			response.setCode(WeatherResponse.ERROR_INPUT_PARAM);
			response.setMessage(WeatherResponse.MESSAGE_ERROR_INPUT_PERIOD);
			return response;
		}	
		
		
		return response;
	}

	/**
	 * Genera el mensaje con los dias de un clima determinado
	 * @param weatherList
	 * @param type
	 * @return
	 */
	private String buildResponseWeatherByType(Collection<Weather> weatherList, String type) {
		String countDays = String.valueOf(weatherList.size());
		String responseMessage = " " + countDays +  "  dias de " + type + ": ";
		String joinedDays = weatherList.stream()
				 .map(w -> String.valueOf(w.getDay()) )
				 .collect(Collectors.joining(", ")); 
		

		return responseMessage.concat(joinedDays);
	}

	/**
	 * Se cargan datos de la galaxia (planetas, cantidad de dias de calculo)
	 * Estos datos se obtienen de application.properties
	 * @return
	 */
	private Galaxy loadGalaxy() {
		Galaxy galaxy = new Galaxy();
        StringTokenizer tokenizer = new StringTokenizer(galaxyPlanets, "|");
        while (tokenizer.hasMoreTokens()) {
            StringTokenizer tokenPlanet = new StringTokenizer(tokenizer.nextToken(), "_");
            Planet planet = new Planet();
            planet.setName(tokenPlanet.nextToken());
            planet.setDistanceToSun(Integer.parseInt(tokenPlanet.nextToken()));
            planet.setVelocity(Integer.parseInt(tokenPlanet.nextToken()));
            planet.setDirection(Integer.parseInt(tokenPlanet.nextToken()));
            LOGGER.debug("Se agrego planeta:" + planet.getName());
            galaxy.getPlanets().add(planet);
        }
        LOGGER.debug("Se va a procesar el clima para la cantidad de dias: {}" , totalDays);
        galaxy.setTotalDays(totalDays);
        LOGGER.debug("Se va a procesar el clima con una precision de {} decimales" , precision );
        galaxy.setPrecision(precision);
        return galaxy;
	}

	@Override
	public WeatherResponse galaxyWeatherDayMoreRained() {
		WeatherResponse response = new WeatherResponse();
		LOGGER.debug("WeatherBO buscando el dia más lluvioso");
		try {
			Collection<Weather> weatherList = weatherBO.getWeatherByType(WeatherType.LLUVIA);
			int dayMoreRained = findDayMoreRained(weatherList);
			response.setCode(WeatherResponse.OK);
			response.setMessage("Día mas lluvioso: " + dayMoreRained);
		}catch(WeatherBOException boE){
			LOGGER.error("Error buscando el pico máximo de lluvia .Mensaje: {}",boE.getMessage());
			response.setCode(WeatherResponse.ERROR_GENERICO);;
			response.setMessage(WeatherResponse.MESSAGE_ERROR_DB);
		}
		return response;
	}

	/**
	 * Busca el dia mas lluvioso entre los dias lluviosos
	 * @param weatherList
	 * @return
	 */
	private int findDayMoreRained(Collection<Weather> weatherList) {
		Comparator<Weather> comp = (p1, p2) -> Double.compare( p1.getPerimeter(), p2.getPerimeter());
		Weather wMaxPerimeter = weatherList.stream().max(comp).get();
		return wMaxPerimeter.getDay();
	}




	
	

}
