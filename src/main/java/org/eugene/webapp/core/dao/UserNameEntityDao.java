package org.eugene.webapp.core.dao;

import org.eugene.webapp.core.mqtt.UserNameEntity;
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
public class UserNameEntityDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void persist(UserNameEntity userNameEntity){
        entityManager.persist(userNameEntity);
    }

    @Transactional
    public void update(UserNameEntity userNameEntity) {
        entityManager.merge(userNameEntity);
    }

    @Transactional
    public UserNameEntity findByUserName(String userName) {
        UserNameEntity userNameEntityClass = null;
        try {
            userNameEntityClass = (UserNameEntity) entityManager.createQuery("SELECT p FROM UserNameEntity p WHERE p.userName LIKE :userName")
                    .setParameter("userName",userName)
                    .getSingleResult();
        } catch (NoResultException e){
            System.err.println("No result");
        }
        return userNameEntityClass;
    }

    @Transactional
    public void removeByUserName(String name) {
        UserNameEntity userNameEntity = findByUserName(name);
        if (userNameEntity != null) {
            entityManager.remove(userNameEntity);
        }
    }

    @Transactional
    public Set<UserNameEntity> findAll() {
        List<UserNameEntity> users = entityManager.createQuery("SELECT p FROM UserName p").getResultList();
        return new HashSet<>(users);
    }
}
