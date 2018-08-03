package com.softserve.edu.cajillo.config;

import com.softserve.edu.cajillo.dto.GetSingleTicketResponseDto;
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
//        modelMapper.createTypeMap(Ticket.class, GetSingleTicketResponseDto.class)
//                .addMapping(ticket -> ticket.getAssignedTo().getId(), GetSingleTicketResponseDto::setAssignedToId)
//                .addMapping(ticket -> ticket.getCreatedBy().getId(), GetSingleTicketResponseDto::setCreatedById);
//                .addMapping(ticket -> ticket.getSprint().getId(), GetSingleTicketResponseDto::setSprintId);
//                .addMapping(ticket -> ticket.getBoard().getId(), GetSingleTicketResponseDto::setBoardId)
//                .addMapping(ticket -> ticket.getTableList().getId(), GetSingleTicketResponseDto::setTableListId);
    }

    private void mapDtoToEntities(ModelMapper modelMapper) {
    }
}