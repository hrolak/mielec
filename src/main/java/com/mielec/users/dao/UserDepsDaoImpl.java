package com.mielec.users.dao;

import java.util.ArrayList;
import java.util.List;

import com.mielec.job.model.Job;
import com.mielec.users.model.User;
import com.mielec.users.model.UserDeps;
import com.mielec.users.model.UserRole;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDepsDaoImpl {

    @Autowired
    private SessionFactory sessionFactory;

    public void addUserDeps(User user,String d_id) {
        UserDeps u=new UserDeps(user,d_id);
        Session session;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        session.beginTransaction();
        session.save(u);
        session.getTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    public void eraseDep(String user_id,String dep_id) {
        Session session ;
        Job j;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        List<UserDeps> deps=new ArrayList<UserDeps>();
        deps = session.createQuery("from user_deps where username=? and d_id=?")
                .setParameter(0, user_id)
                .setParameter(1, dep_id)
                .list();
        session.beginTransaction();
        session.delete(deps.get(0));
        session.getTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    public List<UserDeps> findDepByUser(String user_id) {
        Session session;
        List<UserDeps> deps = new ArrayList<UserDeps>();
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        //"select u from User u where u.username = ?1"
        deps = session.createQuery("from user_deps where username=?")
                .setParameter(0, user_id)
                .list();

        if (deps.size() > 0) {
            return deps;
        } else {
            return null;
        }
    }
    @SuppressWarnings("unchecked")
    public boolean isAdded(String user_id,String dep_id) {
        Session session;
        List<UserDeps> deps = new ArrayList<UserDeps>();
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        //"select u from User u where u.username = ?1"
        deps = session.createQuery("from user_deps where username=? and d_id=?")
                .setParameter(0, user_id)
                .setParameter(1, dep_id)
                .list();

        if (deps.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}