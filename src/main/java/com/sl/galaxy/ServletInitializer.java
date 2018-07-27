package com.sl.galaxy;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * ServletInitilizar. Needed for the gcloud container
 * @author Silvina_Luque
 *
 */

public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WeatherGalaxyApp.class);
    }
} 
