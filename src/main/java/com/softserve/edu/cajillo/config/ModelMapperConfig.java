package com.softserve.edu.cajillo.config;


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
//        modelMapper.createTypeMap(Ticket.class, TicketDto.class)
//                .addMapping(ticket -> ticket.getAssignedTo().getId(), TicketDto::setAssignedToId)
//                .addMapping(ticket -> ticket.getCreatedBy().getId(), TicketDto::setCreatedById);
//                .addMapping(ticket -> ticket.getSprint().getId(), TicketDto::setSprintId);
//                .addMapping(ticket -> ticket.getBoard().getId(), TicketDto::setBoardId)
//                .addMapping(ticket -> ticket.getTableList().getId(), TicketDto::setTableListId);
    }

    private void mapDtoToEntities(ModelMapper modelMapper) {
    }
}