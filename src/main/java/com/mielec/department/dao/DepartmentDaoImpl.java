package com.mielec.department.dao;

import com.mielec.department.model.Department;
import com.mielec.job.model.Job;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class DepartmentDaoImpl {

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public List<Department> getDepartments() {
        Session session;
        List<Department> deps = new ArrayList<Department>();
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        //"select u from User u where u.username = ?1"
        deps = session.createQuery("from department")
                .list();
        return deps;
    }

    @SuppressWarnings("unchecked")
    public void addDepartment(Department d) {
        Session session;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }

        session.beginTransaction();
        session.save(d);
        session.getTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    public boolean isDepartment(String id) {
        Session session;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        List<Department> d;
        d = session.createQuery("from department where id=?")
                .setParameter(0, id)
                .list();

        if (d.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
