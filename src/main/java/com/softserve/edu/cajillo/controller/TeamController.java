package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.converter.TeamConverter;
import com.softserve.edu.cajillo.dto.TeamDTO;
import com.softserve.edu.cajillo.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    TeamService teamService;

    @Autowired
    private TeamConverter teamConverter;

    @GetMapping("/{id}")
    public TeamDTO getTeam(@PathVariable("id") Long id){
        return teamConverter.convertToDto(teamService.getTeam(id));
    }

//- /   GET - get all teams by userId for current user


//    //- /createTeam POST - create new team
//    @PostMapping("/createTeam")
//    public TeamDTO createTeam(){}
//
//    //- /updateTeam/{teamId} PUT - update current team
//    @PutMapping("/updateTeam/{teamId}")
//    public TeamDTO updateTeam(@PathVariable("teamId") Long id){
//        return null;
//    }
//
//    //- /deleteTeam/{teamId} DELETE - delete current team
//    @DeleteMapping("/deleteTeam/{teamId}")
//    public void deleteTeam(){}

}
