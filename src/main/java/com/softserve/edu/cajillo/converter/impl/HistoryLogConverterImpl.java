package com.softserve.edu.cajillo.converter.impl;

import com.softserve.edu.cajillo.converter.HistoryLogConverter;
import com.softserve.edu.cajillo.dto.HistoryLogDto;
import com.softserve.edu.cajillo.entity.HistoryLog;
import com.softserve.edu.cajillo.exception.BoardNotFoundException;
import com.softserve.edu.cajillo.exception.UserNotFoundException;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.HistoryLogsRepository;
import com.softserve.edu.cajillo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HistoryLogConverterImpl implements HistoryLogConverter {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public HistoryLog convertToEntity(HistoryLogDto dto) {
        HistoryLog historyLog = modelMapper.map(dto, HistoryLog.class);
        historyLog.setBoard(boardRepository.findById(dto.getBoardId()).orElseThrow(
                () -> new BoardNotFoundException(String.format("Board with id %d not found", dto.getBoardId()))));
        historyLog.setUser(userRepository.findById(dto.getUserId()).orElseThrow(
                () -> new UserNotFoundException(String.format("User with id %d not found", dto.getUserId()))));
        return historyLog;
    }

    @Override
    public HistoryLogDto convertToDto(HistoryLog entity) {
        HistoryLogDto historyLogDto = modelMapper.map(entity, HistoryLogDto.class);
        historyLogDto.setBoardId(entity.getBoard().getId());
        historyLogDto.setUserId(entity.getUser().getId());
        historyLogDto.setUsername(entity.getUser().getUsername());
        parseCreatedDate(historyLogDto);
        return historyLogDto;
    }

    private void parseCreatedDate(HistoryLogDto historyLogDto) {
        String parsedCreatedDate = historyLogDto.getCreateTime()
                .replace("T", " ").replace("Z", " ").substring(0, 19);
        historyLogDto.setCreateTime(parsedCreatedDate);
    }
}
