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

}