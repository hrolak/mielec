package com.mielec.users.dao;

        import java.util.ArrayList;
        import java.util.List;

        import com.mielec.users.model.User;
        import com.mielec.users.model.UserRole;
        import org.hibernate.HibernateException;
        import org.hibernate.Session;
        import org.hibernate.SessionFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Repository;
        import org.springframework.transaction.annotation.EnableTransactionManagement;
        import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserRoleDaoImpl {

    @Autowired
    private SessionFactory sessionFactory;

    public void addUserRole(User user,String role) {
        UserRole u=new UserRole(user,role);
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
    public UserRole findRoleByUser(String username) {
        Session session;
        List<UserRole> projects = new ArrayList<UserRole>();
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        //"select u from User u where u.username = ?1"
        projects = session.createQuery("from user_roles where username=?")
                .setParameter(0, username)
                .list();

        if (projects.size() > 0) {
            return projects.get(0);
        } else {
            return null;
        }
    }

    public void eraseRoles(String username) {
        UserRole u=new UserRole();
        while((u=findRoleByUser(username))!=null)
        {
            Session session;
            try {
                session = sessionFactory.getCurrentSession();
            } catch (HibernateException e) {
                session = sessionFactory.openSession();
            }
            session.beginTransaction();
            session.delete(u);
            session.getTransaction().commit();
        }
    }

}