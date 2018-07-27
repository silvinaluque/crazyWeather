package com.sl.galaxy.dao;

import java.util.Collection;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.sl.galaxy.model.Weather;


/**
 * WeatherRepository
 * @author Silvina_Luque
 *
 */
@Repository
public interface WeatherRepository  extends MongoRepository<Weather, Long> { 
	
	Weather findByDay(int day);
	
	Collection<Weather> findByWeatherType(String weatherType);

}
