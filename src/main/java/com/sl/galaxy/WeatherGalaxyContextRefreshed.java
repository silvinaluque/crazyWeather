package com.sl.galaxy;

 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.sl.galaxy.manager.WeatherManager;
 /**
  * Listener para cargar los datos al inicializar la app
  * @author Silvina_Luque
  *
  */
@Component
public class WeatherGalaxyContextRefreshed implements ApplicationListener<ContextRefreshedEvent>{
 
	
	@Autowired
    private WeatherManager manager;
 
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        manager.galaxyWeatherInitDB();
    }
}