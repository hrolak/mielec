package com.mielec.controllers;

import com.mielec.models.User;
import com.mielec.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    UserRepository userRep;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody User index(@PathVariable int id) {

        return userRep.findByUsername("alex");
    }

}

