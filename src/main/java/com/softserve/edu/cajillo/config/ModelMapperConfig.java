package com.softserve.edu.cajillo.config;

import com.softserve.edu.cajillo.dto.GetSingleTicketResponse;
import com.softserve.edu.cajillo.entity.BaseEntity;
import com.softserve.edu.cajillo.entity.Ticket;
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

    private void mapEntitiesToDto(ModelMapper modelMapper) {
//        modelMapper.createTypeMap(Ticket.class, GetSingleTicketResponse.class)
//                .addMapping(Ticket::getId, GetSingleTicketResponse::setIdBoard);
    }

    private void mapDtoToEntities(ModelMapper modelMapper) {

    }

}
