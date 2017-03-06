package com.mielec.users.dao;

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
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public User findByUserName(String username) {
        Session session;
        List<User> users = new ArrayList<User>();
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        //"select u from User u where u.username = ?1"
        users = session.createQuery("from users where username=?")
                .setParameter(0, username)
                .list();

        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }
    @SuppressWarnings("unchecked")
    public void changeSalary(String username,Float salary) {
        Session session;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }

        User user=session.get(User.class, username); // 3 is ID of user.
        user.setSalary(salary);
        session.beginTransaction();
        session.saveOrUpdate(user);
        session.getTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        Session session;
        List<User> users = new ArrayList<User>();
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        //"select u from User u where u.username = ?1"
        users = session.createQuery("from users")
                .list();
        return users;
    }
    public void addUser(String username,String password,String department,Float salary) {
        User u=new User(username,password,true,department,salary);
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
    public void addUser(User user) {
        Session session;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
    }
}