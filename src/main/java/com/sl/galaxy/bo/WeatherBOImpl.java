package com.sl.galaxy.bo;

import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sl.galaxy.dao.WeatherRepository;
import com.sl.galaxy.exception.WeatherBOException;
import com.sl.galaxy.model.Coordinates;
import com.sl.galaxy.model.Galaxy;
import com.sl.galaxy.model.GalaxyDayPositions;
import com.sl.galaxy.model.Planet;
import com.sl.galaxy.model.Weather;

import com.sl.galaxy.model.WeatherType;

/**
 * Bussines implementation
 * @author Silvina_Luque
 *
 */
@Component
public class WeatherBOImpl implements WeatherBO {

	private static final Logger LOGGER = LoggerFactory.getLogger(WeatherBOImpl.class);
	
	@Autowired
	WeatherRepository weatherRepository;
	
	@Override
	public void galaxyWeatherInitDB(Galaxy galaxyWeather) throws WeatherBOException {
		LOGGER.debug("WeatherBO se van a inicializar los datos del clima");
		try {
			for(int day = 1; day <= galaxyWeather.getTotalDays(); day++ ) {
				LOGGER.debug("---Dia----: {}" , day);
				Weather weatherDay = new Weather();
				double perimeterTriangle = 0;
				GalaxyDayPositions galaxyPositions = calculatePositions(galaxyWeather, day);
				WeatherType wType = calculateWeatherType(galaxyPositions, galaxyWeather.getPrecision());
				LOGGER.info("El clima para el dia {} es {} ", day, wType.getDescription() );
				if(WeatherType.LLUVIA.equals(wType)) {
					perimeterTriangle = calculatePerimeterTriangle(galaxyPositions);
				}
				weatherDay.setDay(day);
				weatherDay.setWeatherType(wType.getDescription());
				weatherDay.setPerimeter(perimeterTriangle);
				weatherRepository.save(weatherDay);

			}

		}catch(Exception e) {
			LOGGER.error("Ocurrió un error en la carga de datos del clima");
			throw new WeatherBOException(e);
		}

	}
	
	/**
	 * Se calcula el tipo de clima de acuerdo a las posiciones de los planetas de la galaxia
	 * @param galaxyPositions
	 * @return
	 */
	private WeatherType calculateWeatherType(GalaxyDayPositions galaxyDayPositions, int precision) {
		LOGGER.debug("BO calculateWeatherType");
		Coordinates sunCoordinates = new Coordinates(0.0, 0.0);
		Coordinates cPlanet1 = galaxyDayPositions.getPositionsPlanets().get(0);
		Coordinates cPlanet2 = galaxyDayPositions.getPositionsPlanets().get(1);
		Coordinates cPlanet3 = galaxyDayPositions.getPositionsPlanets().get(2);
		
		if(arePlanetsAligned(cPlanet1, cPlanet2, cPlanet3, precision)) {
			if(arePlanetsAligned(sunCoordinates, cPlanet2,  cPlanet3, precision)) {
				LOGGER.debug("El sol y los planetas estan alineados, sequia");
				return WeatherType.SEQUIA;
			}else {
				LOGGER.debug("Se alinearon los planetas, clima optimo");
				return WeatherType.OPTIMO;
			}
		}else { 
			if(isTheSunInside(sunCoordinates, cPlanet1, cPlanet2, cPlanet3)) {
				LOGGER.debug("El sol esta en el triangulo");
				return WeatherType.LLUVIA;
			}
		}
		return WeatherType.INESTABLE;
	}
	
	/**
	 * Calcula el perimetro del triangulo que forman los planetas
	 * @param galaxyDayPositions
	 * @return
	 */
	private double calculatePerimeterTriangle(GalaxyDayPositions galaxyDayPositions) {
		LOGGER.debug("BO calculatePerimeterTriangle");
		
		List<Coordinates> positions = galaxyDayPositions.getPositionsPlanets();
		double p1p2 = distancePlanets(positions.get(0),positions.get(1));
		double p2p3 = distancePlanets(positions.get(1),positions.get(2));
		double p3p1 = distancePlanets(positions.get(2),positions.get(1));
		double perimeter = p1p2 + p2p3 + p3p1;
		return perimeter;
	}
	
	
	/**
	 * Calcula la distancia entre dos planetas
	 * @param p1
	 * @param p2
	 * @return
	 */
	private  double distancePlanets(Coordinates p1, Coordinates p2){
		LOGGER.debug("BO distancePlanets");
		
		return Math.sqrt( Math.pow( (p1.getX() -p2.getX()), 2 ) + Math.pow( (p1.getY() - p2.getY()), 2 ) );
	}
	

	/**
	 * Determina si el sol esta dentro del triangulo
	 * @param sunCoordinates
	 * @param cPlanet1
	 * @param cPlanet2
	 * @param cPlanet3
	 * @return
	 */
	private boolean isTheSunInside(Coordinates sunCoordinates, Coordinates cPlanet1, Coordinates cPlanet2,
			Coordinates cPlanet3) {
		LOGGER.debug("BO isTheSunInside");
		
		//se calcula de acuerdo a que lado del semiplano creado por los lados esta el sol
		int c1c2c3 = signOrientation(cPlanet1, cPlanet2, cPlanet3);
		int c1c2s = signOrientation(cPlanet1, cPlanet2, sunCoordinates);
		int c2c3s = signOrientation(cPlanet2, cPlanet3, sunCoordinates);
		int c3c1s = signOrientation(cPlanet3, cPlanet1, sunCoordinates);
		return (c1c2c3 == c1c2s) && (c1c2s == c2c3s) && (c2c3s == c3c1s) ;

	}
	
	
	private static int signOrientation(Coordinates p1, Coordinates p2, Coordinates p3){
		double x = ((p1.getX()-p3.getX())*(p2.getY()-p3.getY())) - ((p1.getY()-p3.getY())*(p2.getX()-p3.getX()));
		return (x >= 0.0 ? 1:-1);
	}
	

	/**
	 * Determina si los planetas de la galaxia estan alineados
	 * Por ahora solo los primeros tres planetas
	 * (El resto no modifica el clima...)
	 * @return
	 */
	private  boolean arePlanetsAligned(Coordinates cPlanet1, Coordinates cPlanet2, Coordinates cPlanet3 , int precision) {
		LOGGER.debug("BO arePlanetsAligned");
		
		double upTerm1 = cPlanet2.getX() - cPlanet1.getX();
		double downTerm1 = cPlanet2.getY() - cPlanet1.getY();
		double upTerm2 = cPlanet3.getX() - cPlanet2.getX() ;
		double downTerm2 = cPlanet3.getY() - cPlanet2.getY();

	    //caso division por cero
		if( downTerm1 == 0 || downTerm2 == 0) {
			return  downTerm1 == downTerm2;
		}
		double term1 = Precision.round(upTerm1/downTerm1, precision);
		double term2 = Precision.round(upTerm2/downTerm2, precision);
		return  term1 == term2; 
	}
	
	/**
	 * Se calculan las posiciones de todos los planetas de la galaxia
	 * @param galaxyWeather
	 * @param day
	 * @return
	 */
	private GalaxyDayPositions calculatePositions(Galaxy galaxyWeather, int day) {
		List<Planet> planetList = galaxyWeather.getPlanets();
		GalaxyDayPositions galaxyPositions = new GalaxyDayPositions();
		for (Planet planet: planetList){
			LOGGER.debug("Calculando posicion del planeta {} en el dia {}" , planet.getName(), day);
			Coordinates coordinates = calculateCoordinates(planet, day);
			galaxyPositions.getPositionsPlanets().add(coordinates);
		}
		return galaxyPositions;
	}

	/**
	 * Se calculan las coordinadas  x.y de un planeta en un dia
	 * @param planet
	 * @param day
	 * @return
	 */
	private Coordinates calculateCoordinates(Planet planet, int day) {
		LOGGER.debug("BO calculateCoordinates");
		double x = planet.getDistanceToSun() * Math.cos(planet.getVelocity() * planet.getDirection() * day);
		double y = planet.getDistanceToSun() * Math.sin(planet.getVelocity() * planet.getDirection() * day);

		return new Coordinates(x,y);
	}  


	@Override
	public void galaxyWeatherDeleteDB() throws WeatherBOException {
		LOGGER.debug("BO galaxyWeatherDeleteDB");
		try {
			weatherRepository.deleteAll();
		}catch(Exception e) {
			LOGGER.error("Ocurrió un error en la eliminacion  de datos del clima");
			throw new WeatherBOException(e);
		}
	}

	@Override
	public Weather getWeatherByDay(String day) throws WeatherBOException {
		LOGGER.debug("BO getWeatherByDay");
		try {
			return  weatherRepository.findByDay(Integer.parseInt(day));
		}catch(Exception e) {
			LOGGER.error("Ocurrió un error buscando los datos climaticos de un dia");
			throw new WeatherBOException(e);
		}

	}

	@Override
	public Collection<Weather> getWeatherByType(WeatherType wType) throws WeatherBOException {
		LOGGER.debug("BO getWeatherByType");
		try {
			return  weatherRepository.findByWeatherType(wType.getDescription());
		}catch(Exception e) {
			LOGGER.error("Ocurrió un error buscando los datos climaticos por tipo de clima");
			throw new WeatherBOException(e);
		}
	} 

}
