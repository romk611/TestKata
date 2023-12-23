package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try(Session session = Util.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS USERS " +
                    "(ID BIGINT NOT NULL AUTO_INCREMENT, " +
                    "NAME VARCHAR(255) NOT NULL, " +
                    "LASTNAME VARCHAR(255) NOT NULL, " +
                    "AGE TINYINT NOT NULL, " +
                    "PRIMARY KEY (ID))").executeUpdate();
            transaction.commit();
        }
    }

    @Override
    public void dropUsersTable() {
        try(Session session = Util.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS USERS").executeUpdate();
            transaction.commit();
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try(Session session = Util.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            transaction.commit();
        }
        finally {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try(Session session = Util.getSessionFactory().openSession()){
            User user = session.get(User.class, id);
            session.delete(user);
        }

    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = null;
        Transaction transaction = null;
        try(Session session = Util.getSessionFactory().openSession()){
            userList = session.createQuery("FROM User").getResultList();
            for (User user : userList) {
                userList.add(user);
            }
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        try(Session session = Util.getSessionFactory().openSession()){
            session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        }

    }
}
