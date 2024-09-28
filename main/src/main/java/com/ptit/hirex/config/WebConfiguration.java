package com.ptit.hirex.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfiguration {
    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}