package com.softserve.edu.cajillo.converter.impl;

import com.softserve.edu.cajillo.converter.TableListConverter;
import com.softserve.edu.cajillo.dto.TableListDto;
import com.softserve.edu.cajillo.entity.TableList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TableListConverterImpl implements TableListConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TableList convertToEntity(TableListDto dto) {
        return null;
    }

    @Override
    public TableListDto convertToDto(TableList entity) {
        return modelMapper.map(entity, TableListDto.class);
    }
}
