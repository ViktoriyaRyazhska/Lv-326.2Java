package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.entity.Relation;
import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.entity.enums.RoleName;
import com.softserve.edu.cajillo.exception.RelationServiceException;
import com.softserve.edu.cajillo.repository.RelationRepository;
import com.softserve.edu.cajillo.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelationServiceImpl implements RelationService {

    private static final String RELATIONSERVICE_ID_NOT_FOUND_MESSAGE = "Could not find roleManagerService with id=";
    private static final String RELATIONSERVICE_USER_ID_NOT_FOUND_MESSAGE = "Could not find roleManagerService with user_id=";

    @Autowired
    private RelationRepository relationRepository;

    @Override
    public Relation getRelationService(Long id) {
        return relationRepository.findById(id).orElseThrow(
                () -> new RelationServiceException(RELATIONSERVICE_ID_NOT_FOUND_MESSAGE + id));
    }

    @Override
    public List<Relation> getAllRelationServicesByUserId(Long id) {
        return relationRepository.findAllByUserId(id);
//                .orElseThrow(
//                () -> new RelationServiceException(RELATIONSERVICE_USER_ID_NOT_FOUND_MESSAGE + id));
    }

    public Map<User, RoleName> getAllUsersInTeam(Long teamId) {
        List<Relation> allByTeamId = relationRepository.findAllByTeamId(teamId);
        Map<User, RoleName> userWithHisRole = new HashMap<>();
        for (Relation manager : allByTeamId) {
            User user = manager.getUser();
            if (!userWithHisRole.containsKey(user)) {
                userWithHisRole.put(user, manager.getRoleName());
            }
        }
        return userWithHisRole;
    }
}