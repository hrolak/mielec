package com.mielec.controllers;

import com.mielec.department.dao.DepartmentDaoImpl;
import com.mielec.department.model.Department;
import com.mielec.job.dao.JobDaoImpl;
import com.mielec.job.model.Job;
import com.mielec.project.dao.ProjectDaoImpl;
import com.mielec.project.model.Project;
import com.mielec.users.dao.UserRoleDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.mielec.job.dao.JobDaoImpl;
import com.mielec.job.model.Job;
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
import sun.util.calendar.LocalGregorianCalendar;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    public static boolean isDateInCurrentWeek(Date date) {
        Date today=new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_YEAR, -6);
        Date historyDate = cal.getTime();
        return date.after(historyDate) && date.before(today);
    }
    public static boolean isDateInCurrentMonth(Date date) {
        Date today=new Date();
        return today.getMonth()==date.getMonth();
    }
    public static boolean isDateInLastMonth(Date date) {
        Date today=new Date();
        return today.getMonth()-1==date.getMonth();
    }
    @Autowired
    UserDaoImpl UDI;

    @Autowired
    JobDaoImpl JDI;

    @Autowired
    ProjectDaoImpl PDI;

    @Autowired
    UserRoleDaoImpl URDI;

    @Autowired
    DepartmentDaoImpl DDI;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public @ResponseBody ModelAndView index(@RequestParam("id") String id) {
        ModelAndView model=new ModelAndView("useradminpanel");
        model.addObject("user_id",id);
        User u=UDI.findByUserName(id);
        List<Job> jobs=JDI.findJobByUser(id);
        int curr=0;
        int last=0;
        if(jobs!=null) {
            for(int i=0;i<jobs.size();i++) {
                if(isDateInCurrentMonth(jobs.get(i).getDate()))
                    curr+=jobs.get(i).getTime();
                if(isDateInLastMonth(jobs.get(i).getDate()))
                    last+=jobs.get(i).getTime();
            }
        }
        model.addObject("user",u);
        model.addObject("curr",curr);
        model.addObject("last",last);
        return model;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public @ResponseBody ModelAndView index2() {
        ModelAndView model=new ModelAndView("user");
        List<User> p=UDI.getUsers();
        model.addObject("users",p);
        return model;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value="/addjob", method = RequestMethod.POST)
    public @ResponseBody ModelAndView addjob(@RequestParam("date") String d,@RequestParam("time") int t,@RequestParam("project") int p) {
        Date dat=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        try {
            dat=format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        ModelAndView model=new ModelAndView("userpanel");
        boolean validDate=isDateInCurrentWeek(dat);
        boolean validProject = PDI.findProjectById(p)!=null;
        boolean validTime = t>=0;
        if(validDate && validProject && validTime) {
            JDI.addJob(name,p,t,dat);
        }
        model.addObject("vDate",validDate);
        model.addObject("vProj",validProject);
        model.addObject("vTime",validTime);
        return model;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value="/showrecords", method = RequestMethod.POST)
    public @ResponseBody ModelAndView showrecords() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        ModelAndView model=new ModelAndView("userpanel");
        List<Job> j= JDI.findJobByUser(name);
        model.addObject("jobs",j);
        return model;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/showadminrecords", method = RequestMethod.POST)
    public @ResponseBody ModelAndView showadminrecords(@RequestParam("user") String user_id) {
        ModelAndView model=new ModelAndView("useradminpanel");
        List<Job> j= JDI.findJobByUser(user_id);
        model.addObject("jobs",j);
        model.addObject("user_id",user_id);
        User u=UDI.findByUserName(user_id);
        model.addObject("user",u);
        return model;
    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/addadminjob", method = RequestMethod.POST)
    public @ResponseBody ModelAndView addadminjob(@RequestParam("date") String d,@RequestParam("time") int t,@RequestParam("project") int p,@RequestParam("user") String user_id) {
        Date dat=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        try {
            dat=format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ModelAndView model=new ModelAndView("useradminpanel");
        boolean validProject = PDI.findProjectById(p)!=null;
        boolean validTime = t>=0;
        if(validProject && validTime) {
            JDI.addJob(user_id,p,t,dat);
        }
        model.addObject("vProj",validProject);
        model.addObject("vTime",validTime);
        model.addObject("user_id",user_id);
        User u=UDI.findByUserName(user_id);
        model.addObject("user",u);
        return model;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/deleteadminjob", method = RequestMethod.POST)
    public @ResponseBody ModelAndView deletedminjob(@RequestParam("id") int id,@RequestParam("user") String user_id) {
        Date dat=new Date();
        ModelAndView model=new ModelAndView("useradminpanel");
        JDI.eraseJob(id);
        model.addObject("user_id",user_id);
        User u=UDI.findByUserName(user_id);
        model.addObject("user",u);
        return model;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/changesalary", method = RequestMethod.POST)
    public @ResponseBody ModelAndView changesalary(@RequestParam("salary") float salary,@RequestParam("user") String user_id) {
        Date dat=new Date();
        ModelAndView model=new ModelAndView("useradminpanel");
        UDI.changeSalary(user_id,salary);
        model.addObject("user_id",user_id);
        User u=UDI.findByUserName(user_id);
        model.addObject("user",u);
        return model;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/adduser_added", method = RequestMethod.POST)
    public @ResponseBody ModelAndView adduser_added(@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("department") String dep,@RequestParam("salary") Float salary,@RequestParam("priv") String priv) {

        ModelAndView model=new ModelAndView("adduser");
        User u=new User(username,password,true,dep,salary);
        boolean added;
        if(UDI.findByUserName(username)==null) {
            UDI.addUser(u);
            URDI.addUserRole(u, "ROLE_USER");
            if (priv.equals("admin"))
                URDI.addUserRole(u, "ROLE_ADMIN");
            added = true;
        }
        else {
            added=false;
        }
        List<Department> deps=DDI.getDepartments();
        model.addObject("deps",deps);
        model.addObject("added",added);
        return model;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/adduser", method = RequestMethod.GET)
    public @ResponseBody ModelAndView adduser() {

        ModelAndView model=new ModelAndView("adduser");
        List<Department> deps=DDI.getDepartments();
        model.addObject("deps",deps);
        return model;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/adddepartment_added", method = RequestMethod.POST)
    public @ResponseBody ModelAndView adddepartment_added(@RequestParam("d_id") String d_id,@RequestParam("name") String name) {

        ModelAndView model=new ModelAndView("adddepartment");
        Department d=new Department(d_id,name);
        boolean added=DDI.isDepartment(d_id);
        if(!added)
            DDI.addDepartment(d);
        model.addObject("added",added);
        List<Department> x=DDI.getDepartments();
        model.addObject("deps",x);
        return model;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/adddepartment", method = RequestMethod.GET)
    public @ResponseBody ModelAndView adddepartment() {

        ModelAndView model=new ModelAndView("adddepartment");
        List<Department> x=DDI.getDepartments();
        model.addObject("deps",x);
        return model;
    }
}

