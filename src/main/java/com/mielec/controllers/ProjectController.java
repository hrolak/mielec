package com.mielec.controllers;

import com.mielec.users.dao.UserDaoImpl;
import com.mielec.users.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.mielec.users.model.User;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    UserDaoImpl userD;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody String index(@PathVariable int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        return name;
    }

}

