package com.mielec.controllers;

import com.mielec.department.dao.DepartmentDaoImpl;
import com.mielec.department.model.Department;
import com.mielec.job.dao.JobDaoImpl;
import com.mielec.job.model.Job;
import com.mielec.project.dao.ProjectDaoImpl;
import com.mielec.project.model.Project;
import com.mielec.users.dao.UserDepsDaoImpl;
import com.mielec.users.dao.UserRoleDaoImpl;
import com.mielec.users.model.UserDeps;
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
import java.util.concurrent.TimeUnit;

@RestController
public class UserController {

    public static boolean isDateInCurrentWeek(Date date) {
        Date today=new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_YEAR, -7);
        Date historyDate = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 8);
        today=cal.getTime();
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

    @Autowired
    UserDepsDaoImpl UDDI;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public @ResponseBody ModelAndView index(@RequestParam("id") String id) {
        ModelAndView model=new ModelAndView("useradminpanel");
        model.addObject("user_id",id);
        User u=UDI.findByUserName(id);
        List<Job> jobs=JDI.findJobByUser(id);
        List<Department> adeps=DDI.getDepartments();
        double curr=0;
        double last=0;
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
        List<UserDeps> d=UDI.getDeps(id);
        List<Department> deps=new ArrayList<Department>();
        for(int i=0;i<d.size();i++) {
            for(int j=0;j<adeps.size();j++) {
                if(d.get(i).getDep().equals(adeps.get(j).getId()))
                    deps.add(adeps.get(j));
            }
        }
        model.addObject("deps",deps);
        model.addObject("adeps",adeps);
        return model;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/eraseuser", method = RequestMethod.POST)
    public @ResponseBody ModelAndView eraseuser(@RequestParam("id") String id) {
        ModelAndView model=new ModelAndView("/");
        URDI.eraseRoles(id);
        UDI.eraseUser(id);
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
    public @ResponseBody ModelAndView addjob(@RequestParam("date") String d,@RequestParam("time") Double t,@RequestParam("project") int p,@RequestParam("d_id") String d_id) {
        Date dat=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        try {
            dat=format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        boolean validDate=isDateInCurrentWeek(dat);
        boolean validProject = PDI.findProjectById(p)!=null;
        boolean validTime = t>=0;
        if((validDate || t==0) && validProject && validTime) {
            Job j=JDI.findSpecificJob(name,dat,d_id,p);
            if(j!=null)
                JDI.eraseJob(j.getId());
            JDI.addJob(name,p,t,dat,d_id);
            validDate = true;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(dat);
        while(c.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
            c.add(Calendar.DAY_OF_YEAR,-1);
        dat=c.getTime();
        ModelAndView model=userpanel(format.format(dat));
        model.addObject("vDate",validDate);
        model.addObject("vProj",validProject);
        model.addObject("vTime",validTime);
        return model;
    }

    @RequestMapping(value="/changeweek", method = RequestMethod.POST)
    public @ResponseBody ModelAndView erasejob(@RequestParam("dir") int dir,@RequestParam("date") String d) {
        Date dat=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        try {
            dat=format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dat);
        c.add(Calendar.DATE, dir*7);
        dat = c.getTime();
        ModelAndView model=userpanel(format.format(dat));
        return model;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value="/userpanel", method = RequestMethod.POST)
    public @ResponseBody ModelAndView userpanel(@RequestParam("date") String d) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Date dat=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        try {
            dat=format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dat);
        c.add(Calendar.DATE, 4);
        Date dat2 = c.getTime();

        User user=UDI.findByUserName(name);
        List<UserDeps> deps = UDI.getDeps(name);
        Map<String,Integer> depid = new HashMap<String,Integer>();
        for(int i=0;i<deps.size();i++) {
            depid.put(deps.get(i).getDep(),i);
        }
        List<Project> proj = PDI.getProjects();
        ModelAndView model=new ModelAndView("userpanel");
        List<Job> j= JDI.findJobByUserAndDateBetween(name,dat,dat2);
        if(j!=null) {
            List<String> depL = new ArrayList<String>();
            for(int i=0;i<5*deps.size();i++)
                depL.add(deps.get(i%deps.size()).getDep());
            model.addObject("depL",depL);
            Map<Integer,Project> idName = new HashMap<Integer,Project>();
            for(int i=0;i<proj.size();i++) {
                idName.put(proj.get(i).getId(),proj.get(i));
            }
            Map<Project,ArrayList<Job> > table = new TreeMap<Project,ArrayList<Job> >();
            Map<Integer,Double> projsum = new HashMap<Integer,Double>();
            List<Double> datesum = new ArrayList<Double>();
            for(int in=0;in<5;in++)
                datesum.add(0.0);
            Project p;
            String id;
            int ind;
            String dep;
            for(int i=0;i<j.size();i++) {
                c.setTime(dat);
                p=idName.get((j.get(i).getProject_id()));
                if(!table.containsKey(p)) {
                    projsum.put(p.getId(),0.0);
                    for(int ii=0;ii<proj.size();ii++)
                        if(proj.get(ii).getId()==p.getId())
                            proj.remove(ii);

                    ArrayList<Job> a=new ArrayList<Job>(deps.size()*5);
                    table.put(p,a);
                    for(int ii=0;ii<deps.size()*5;ii++) {
                        dep=deps.get(ii%deps.size()).getDep();
                        table.get(p).add(new Job(name,0,0,c.getTime(),dep));
                        if((ii+1)%deps.size()==0)
                            c.add(Calendar.DATE,1);
                    }
                }
                ind=(int) TimeUnit.DAYS.convert(j.get(i).getDate().getTime()-dat.getTime(), TimeUnit.MILLISECONDS);

                datesum.set(ind,datesum.get(ind)+j.get(i).getTime());
                ind= deps.size()*ind+depid.get(j.get(i).getDep());

                table.get(p).set(ind,j.get(i));

                projsum.put(p.getId(),projsum.get(p.getId())+j.get(i).getTime());
            }
            model.addObject("projsum",projsum);
            model.addObject("datesum",datesum);
            model.addObject("table",table);

        }

        ArrayList<Double> in=new ArrayList<Double>();
        for(int i=0;i<=48;i++)
            in.add(i/2.0);

        model.addObject("in",in);
        model.addObject("deps",deps);
        model.addObject("proj",proj);
        model.addObject("date",d);
        model.addObject("clas","two");
        return model;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value="/showrecords", method = RequestMethod.POST)
    public @ResponseBody ModelAndView showrecords() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        ModelAndView model=new ModelAndView("userpanel");
        List<Job> j= JDI.findJobByUser(name);
        List<Project> p = PDI.getProjects();

        model.addObject("jobs",j);


        return model;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/showadminrecords", method = RequestMethod.POST)
    public @ResponseBody ModelAndView showadminrecords(@RequestParam("user") String user_id) {
        ModelAndView model=index(user_id);
        List<Job> j= JDI.findJobByUserDateSorted(user_id);
        for (Iterator<Job> iter = j.listIterator(); iter.hasNext(); ) {
            Job a = iter.next();
            if (a.getTime()==0) {
                iter.remove();
            }
        }
        List<Project> p = PDI.getProjects();
        Map<Integer,String> pp = new HashMap<Integer,String>();

        for(int i=0;i<p.size();i++)
            pp.put(p.get(i).getId(),p.get(i).getName());
        model.addObject("projs",pp);
        model.addObject("jobs",j);
        return model;
    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/addadminjob", method = RequestMethod.POST)
    public @ResponseBody ModelAndView addadminjob(@RequestParam("date") String d,@RequestParam("time") Double t,@RequestParam("project") int p,@RequestParam("user") String user_id,@RequestParam("d_id") String d_id) {
        Date dat=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        try {
            dat=format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        boolean validProject = PDI.findProjectById(p)!=null;
        boolean validTime = t>=0;
        if(validProject && validTime) {
            JDI.addJob(user_id,p,t,dat,d_id);
        }
        ModelAndView model=index(user_id);
        model.addObject("vProj",validProject);
        model.addObject("vTime",validTime);
        model.addObject("user_id",user_id);


        return model;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/deleteadminjob", method = RequestMethod.POST)
    public @ResponseBody ModelAndView deletedminjob(@RequestParam("id") int id,@RequestParam("user") String user_id) {
        Date dat=new Date();
        JDI.eraseJob(id);
        ModelAndView model=index(user_id);

        return model;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/changesalary", method = RequestMethod.POST)
    public @ResponseBody ModelAndView changesalary(@RequestParam("salary") float salary,@RequestParam("user") String user_id) {
        UDI.changeSalary(user_id,salary);
        ModelAndView model=index(user_id);
        return model;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/addbranch", method = RequestMethod.POST)
    public @ResponseBody ModelAndView addbranch(@RequestParam("branch") String branch,@RequestParam("user") String user_id) {
        User u=UDI.findByUserName(user_id);
        if(UDDI.isAdded(user_id,branch)==false)
            UDDI.addUserDeps(u,branch);
        ModelAndView model=index(user_id);
        return model;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/erasebranch", method = RequestMethod.POST)
    public @ResponseBody ModelAndView erasebranch(@RequestParam("branch") String branch,@RequestParam("user") String user_id) {
        if(UDDI.isAdded(user_id,branch)==true)
            UDDI.eraseDep(user_id,branch);
        ModelAndView model=index(user_id);
        return model;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/adduser_added", method = RequestMethod.POST)
    public @ResponseBody ModelAndView adduser_added(@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("salary") Float salary,@RequestParam("priv") String priv,@RequestParam("department") String d_id) {

        ModelAndView model=new ModelAndView("adduser");
        User u=new User(username,password,true,salary);
        boolean added;
        if(UDI.findByUserName(username)==null) {
            UDI.addUser(u);
            URDI.addUserRole(u, "ROLE_USER");
            UDDI.addUserDeps(u,d_id);
            if (priv.equals("admin")) {
                URDI.addUserRole(u, "ROLE_ADMIN");
                URDI.addUserRole(u, "ROLE_SUBADMIN");
            }
            if (priv.equals("subadmin"))
                URDI.addUserRole(u, "ROLE_SUBADMIN");
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

        Department d=new Department(d_id,name);
        boolean added=DDI.isDepartment(d_id);
        if(!added)
            DDI.addDepartment(d);
        else
            DDI.renameDepartment(d_id,name);
        ModelAndView model=adddepartment();
        return model;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/erasedepartment", method = RequestMethod.POST)
    public @ResponseBody ModelAndView erasedepartment(@RequestParam("d_id") String d_id) {
        boolean added=DDI.isDepartment(d_id);
        if(added)
            DDI.eraseDepartment(d_id);
        ModelAndView model=adddepartment();
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



    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value="/", method = RequestMethod.GET)
    public @ResponseBody ModelAndView hello() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date dat=c.getTime();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        String date=format.format(dat);
        ModelAndView model=new ModelAndView("hello");
        model.addObject("date",date);
        model.addObject("name",name);
        return model;
    }
}

