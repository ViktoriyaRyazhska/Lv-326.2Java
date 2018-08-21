package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.entity.Relation;
import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.entity.enums.RoleName;

import java.util.List;
import java.util.Map;

public interface RelationService {

    Relation getRelationService(Long id);

    List<Relation> getAllRelationServicesByUserId(Long id);

    Map<User, RoleName> getAllUsersInTeam(Long teamId);
}