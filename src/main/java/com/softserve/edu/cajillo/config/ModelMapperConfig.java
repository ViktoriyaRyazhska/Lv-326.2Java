package com.softserve.edu.cajillo.config;


import com.softserve.edu.cajillo.dto.TicketDto;
import com.softserve.edu.cajillo.entity.TableList;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.exception.ResourceNotFoundException;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.SprintRepository;
import com.softserve.edu.cajillo.repository.TableListRepository;
import com.softserve.edu.cajillo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TableListRepository tableListRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        mapEntitiesToDto(modelMapper);
        mapDtoToEntities(modelMapper);
        return modelMapper;
    }

    private void mapEntitiesToDto(ModelMapper modelMapper) {

        modelMapper.addMappings(new PropertyMap<Ticket, TicketDto>() {
            @Override
            protected void configure() {
                map().setAssignedToId(source.getAssignedTo().getId());
                map().setCreatedById(source.getCreatedBy().getId());
                map().setBoardId(source.getBoard().getId());
                map().setSprintId(source.getSprint().getId());
                map().setTableListId(source.getTableList().getId());
            }
        });
    }

    private void mapDtoToEntities(ModelMapper modelMapper) {
    }
}