package org.eugene.webapp.core.dao;

import org.eugene.webapp.core.user.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Transactional
public class UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void persist(User user){
        entityManager.persist(user);
    }

    @Transactional
    public void update(User user) {
        entityManager.merge(user);
    }

    @Transactional
    public User findByLogin(String login) {
        User user = null;
        try {
            user = (User) entityManager.createQuery("SELECT p FROM User p WHERE p.login LIKE :login")
                    .setParameter("login",login)
                    .getSingleResult();
        } catch (NoResultException e){
            System.err.println("No result");
        }
        return user;
    }

    @Transactional
    public void removeByLogin(String login) {
        User user = findByLogin(login);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    @Transactional
    public Set<User> findAll() {
        List<User> users = entityManager.createQuery("SELECT p FROM User p").getResultList();
        return new HashSet<>(users);
    }
}
