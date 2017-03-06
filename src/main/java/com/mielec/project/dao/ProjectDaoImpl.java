package com.mielec.project.dao;

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
public class ProjectDaoImpl implements  ProjectDao{

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public Project findProjectById(int id) {
        Session session;
        List<Project> projects = new ArrayList<Project>();
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        //"select u from User u where u.username = ?1"
        projects = session.createQuery("from project where id=?")
                .setParameter(0, id)
                .list();

        if (projects.size() > 0) {
            return projects.get(0);
        } else {
            return null;
        }
    }
    @SuppressWarnings("unchecked")
    public boolean isProject(int id) {
        Session session;
        List<Project> projects = new ArrayList<Project>();
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        //"select u from User u where u.username = ?1"
        projects = session.createQuery("from project where id=?")
                .setParameter(0, id)
                .list();

        if (projects.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
    @SuppressWarnings("unchecked")
    public List<Project> getProjects() {
        Session session;
        List<Project> projects = new ArrayList<Project>();
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        //"select u from User u where u.username = ?1"
        projects = session.createQuery("from project")
                .list();
        return projects;
    }
    public void addProject(int id,String name) {
        Project p=new Project(id,name);
        Session session;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        session.beginTransaction();
        session.save(p);
        session.getTransaction().commit();
    }

}
