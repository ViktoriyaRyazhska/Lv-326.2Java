package com.softserve.edu.cajillo.config;

import com.softserve.edu.cajillo.repository.BoardRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        mapEntitiesToDto(modelMapper);
        mapDtoToEntities(modelMapper);
        return modelMapper;
    }

    BoardRepository boardRepository;

    private void mapEntitiesToDto(ModelMapper modelMapper) {
    }

    private void mapDtoToEntities(ModelMapper modelMapper) {
    }
}