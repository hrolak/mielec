package com.mielec.job.dao;


import com.mielec.department.model.Department;
import com.mielec.project.dao.ProjectDao;
import com.mielec.project.model.Project;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

import com.mielec.job.model.Job;
import com.mielec.project.model.Project;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

import com.mielec.users.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class JobDaoImpl implements JobDao {

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public Job findJobById(int id) {
        Session session;
        List<Job> jobs = new ArrayList<Job>();
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        //"select u from User u where u.username = ?1"
        jobs = session.createQuery("from job where id=?")
                .setParameter(0, id)
                .list();

        if (jobs.size() > 0) {
            return jobs.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Job> getJobs() {
        Session session;
        List<Job> jobs = new ArrayList<Job>();
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        //"select u from User u where u.username = ?1"
        jobs = session.createQuery("from project")
                .list();
        return jobs;
    }


    public void addJob(String user_id,int project_id,Double time,Date date, String d_id) {
        Job j=new Job(user_id,project_id,time,date,d_id);
        Session session;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        session.beginTransaction();
        session.save(j);
        session.getTransaction().commit();
    }
    @SuppressWarnings("unchecked")
    public void eraseJob(int job_id) {
        Session session ;
        Job j;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        j = (Job)session.load(Job.class,job_id);
        session.beginTransaction();
        session.delete(j);
        session.getTransaction().commit();
    }
    @SuppressWarnings("unchecked")
    public List<Job> findJobByProject(int project_id) {
        Session session;
        List<Job> jobs = new ArrayList<Job>();
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        //"select u from User u where u.username = ?1"
        jobs = session.createQuery("from job where project_id=?")
                .setParameter(0, project_id)
                .list();

        if (jobs.size() > 0) {
            return jobs;
        } else {
            return null;
        }
    }
    @SuppressWarnings("unchecked")
    public List<Job> findJobByProjectNotNull(int project_id) {
        Session session;
        List<Job> jobs = new ArrayList<Job>();
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        //"select u from User u where u.username = ?1"
        jobs = session.createQuery("from job where project_id=? and time!=0")
                .setParameter(0, project_id)
                .list();

        if (jobs.size() > 0) {
            return jobs;
        } else {
            return null;
        }
    }


    @SuppressWarnings("unchecked")
    public List<Job> findJobByUser(String user_id) {
        Session session;
        List<Job> jobs = new ArrayList<Job>();
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        //"select u from User u where u.username = ?1"
        jobs = session.createQuery("from job where user_id=?")
                .setParameter(0, user_id)
                .list();

        if (jobs.size() > 0) {
            return jobs;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Job> findJobByUserDateSorted(String user_id) {
        Session session;
        List<Job> jobs = new ArrayList<Job>();
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        //"select u from User u where u.username = ?1"
        jobs = session.createQuery("from job where user_id=? order by date desc")
                .setParameter(0, user_id)
                .list();

        if (jobs.size() > 0) {
            return jobs;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Job> findJobByUserAndDate(String user_id,Date date) {
        Session session;
        List<Job> jobs = new ArrayList<Job>();
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        //"select u from User u where u.username = ?1"
        jobs = session.createQuery("from job where user_id=? and date=?")
                .setParameter(0, user_id)
                .setParameter(1, date)
                .list();

        if (jobs.size() > 0) {
            return jobs;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Job> findJobByUserAndDateAndDep(String user_id,Date date,Department dep) {
        Session session;
        List<Job> jobs = new ArrayList<Job>();
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        //"select u from User u where u.username = ?1"
        jobs = session.createQuery("from job where user_id=? and date=? and d_id=?")
                .setParameter(0, user_id)
                .setParameter(1, date)
                .setParameter(2, dep)
                .list();

        if (jobs.size() > 0) {
            return jobs;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Job findSpecificJob(String user_id,Date date,String dep,int proj) {
        Session session;
        List<Job> jobs = new ArrayList<Job>();
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        //"select u from User u where u.username = ?1"
        jobs = session.createQuery("from job where user_id=? and date=? and d_id=? and project_id=?")
                .setParameter(0, user_id)
                .setParameter(1, date)
                .setParameter(2, dep)
                .setParameter(3, proj)
                .list();

        if (jobs.size() > 0) {
            return jobs.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Job> findJobByUserAndDateBetween(String user_id,Date date,Date date2) {
        Session session;
        List<Job> jobs = new ArrayList<Job>();
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        //"select u from User u where u.username = ?1"
        jobs = session.createQuery("from job where user_id=? and date>=? and date<=? order by project_id desc")
                .setParameter(0, user_id)
                .setParameter(1, date)
                .setParameter(2, date2)
                .list();

        if (jobs.size() > 0) {
            return jobs;
        } else {
            return null;
        }
    }

}
