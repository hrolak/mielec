package com.mielec.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
@RequestMapping("/project")
public class ProjectController {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody HashMap<Integer, Integer> index(@PathVariable int id) {
        HashMap<Integer,Integer> d=new HashMap<Integer,Integer>();
        d.put(14,id);
        d.put(11,95);
        return d;
    }

}