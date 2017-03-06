package com.mielec.controllers;

import com.mielec.department.dao.DepartmentDaoImpl;
import com.mielec.department.model.Department;
import com.mielec.job.dao.JobDaoImpl;
import com.mielec.job.model.Job;
import com.mielec.project.dao.ProjectDaoImpl;
import com.mielec.project.model.Project;
import com.mielec.users.dao.UserDaoImpl;
import com.mielec.users.model.UserRole;
import javafx.util.Pair;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ProjectController {

    public static boolean isDateInCurrentMonth(Date date) {
        Date today=new Date();
        return today.getMonth()==date.getMonth();
    }
    public static boolean isDateInLastMonth(Date date) {
        Date today=new Date();
        return today.getMonth()-1==date.getMonth();
    }

    @Autowired
    ProjectDaoImpl PDI;

    @Autowired
    UserDaoImpl UDI;

    @Autowired
    JobDaoImpl JDI;

    @Autowired
    DepartmentDaoImpl DDI;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/project", method = RequestMethod.POST)
    public @ResponseBody ModelAndView index(@RequestParam("id") int id) {
        ModelAndView model=new ModelAndView("showproject");
        List<Job> jobs=JDI.findJobByProject(id);
        Map<String,Pair<Integer,Float>> map=new HashMap<String,Pair<Integer,Float>>();
        Map<String,Pair<Integer,Float>> map2=new HashMap<String,Pair<Integer,Float>>();
        int total=0;
        float totalp=0;
        if(jobs!=null) {
            List<User> users = UDI.getUsers();

            Map<String, String> us = new HashMap<String, String>();
            Map<String, Float> prius = new HashMap<String, Float>();

            for (int i = 0; i < users.size(); i++) {
                us.put(users.get(i).getUsername(), users.get(i).getD_id());
                prius.put(users.get(i).getUsername(), users.get(i).getSalary());
            }

            for (int i = 0; i < jobs.size(); i++) {
                total += jobs.get(i).getTime();
                totalp += jobs.get(i).getTime() * prius.get(jobs.get(i).getUser_id());
                if (map.containsKey(us.get(jobs.get(i).getUser_id()))) {
                    map.put(us.get(jobs.get(i).getUser_id()), new Pair<Integer, Float>(map.get(us.get(jobs.get(i).getUser_id())).getKey() + jobs.get(i).getTime(), map.get(us.get(jobs.get(i).getUser_id())).getValue() + jobs.get(i).getTime() * prius.get(jobs.get(i).getUser_id())));
                } else {
                    map.put(us.get(jobs.get(i).getUser_id()), new Pair<Integer, Float>(jobs.get(i).getTime(), jobs.get(i).getTime() * prius.get(jobs.get(i).getUser_id())));
                }
                if (map2.containsKey(jobs.get(i).getUser_id())) {
                    map2.put(jobs.get(i).getUser_id(), new Pair<Integer, Float>(map2.get(jobs.get(i).getUser_id()).getKey() + jobs.get(i).getTime(), map2.get(jobs.get(i).getUser_id()).getValue() + jobs.get(i).getTime() * prius.get(jobs.get(i).getUser_id())));
                } else {
                    map2.put(jobs.get(i).getUser_id(), new Pair<Integer, Float>(jobs.get(i).getTime(), jobs.get(i).getTime() * prius.get(jobs.get(i).getUser_id())));
                }
            }
        }
        model.addObject("total",total);
        model.addObject("totalp",totalp);
        model.addObject("deps",map);
        model.addObject("deps2",map2);
        return model;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/project", method = RequestMethod.GET)
    public @ResponseBody ModelAndView index2() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        ModelAndView model=new ModelAndView("project");
        List<Project> p=PDI.getProjects();
        model.addObject("projects",p);
        return model;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/addproject_added", method = RequestMethod.POST)
    public @ResponseBody ModelAndView addproject(@RequestParam("project_id") int id,@RequestParam("name") String n) {

        ModelAndView model=new ModelAndView("addproject");
        boolean isAdded=PDI.isProject(id);
        if(!isAdded)
            PDI.addProject(id,n);
        model.addObject("added",isAdded);
        return model;
    }
}

