package com.mielec.controllers;

import com.mielec.job.dao.JobDaoImpl;
import com.mielec.project.dao.ProjectDaoImpl;
import com.mielec.project.model.Project;
import com.mielec.users.dao.UserDaoImpl;
import com.mielec.users.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.mielec.users.model.User;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    ProjectDaoImpl PDI;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public @ResponseBody ModelAndView index(@PathVariable int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        ModelAndView model=new ModelAndView("project");
        List<Project> p=PDI.getProjects();
        List<Integer> integ=new ArrayList<Integer>();
        for(Project x: p) {
            integ.add(x.getId());
            System.out.println(x.getId());
        }
        model.addObject("projects",integ);
        return model;
    }

}

