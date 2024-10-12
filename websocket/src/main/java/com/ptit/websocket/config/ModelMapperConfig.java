package com.ptit.websocket.config;

import com.ptit.websocket.mapper.MessageDtoToMessage;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new MessageDtoToMessage());

        return modelMapper;
    }
}
