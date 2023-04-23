package com.pf.config;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperFacadeConfig {

    @Bean
    public MapperFactory getFactory(){
        return new DefaultMapperFactory.Builder().build();
    }
}
