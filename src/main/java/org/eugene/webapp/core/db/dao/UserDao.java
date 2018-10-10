package org.eugene.webapp.core.db.dao;

import org.eugene.webapp.core.parsing.device.Device;
import org.eugene.webapp.core.parsing.filter.DataFilter;
import org.eugene.webapp.core.user.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import java.util.List;

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
    public void removeFilterById(Long id){
        DataFilter dataFilter = entityManager.find(DataFilter.class, id);
        if(dataFilter != null){
            entityManager.remove(dataFilter);
        }
    }

    @Transactional
    public void removeDeviceById(Long id){
        Device device = entityManager.find(Device.class, id);
        if(device != null){
            entityManager.remove(device);
        }
    }

    @Transactional
    public List<User> findAll() {
        return entityManager.createQuery("SELECT p FROM User p").getResultList();
    }
}
