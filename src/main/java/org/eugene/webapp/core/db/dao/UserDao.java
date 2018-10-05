package org.eugene.webapp.core.db.dao;

import org.eugene.webapp.core.db.model.user.ConverterDataEntity;
import org.eugene.webapp.core.db.model.user.UserEntity;
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
    public void persist(UserEntity userEntity){
        entityManager.persist(userEntity);
    }

    @Transactional
    public void update(UserEntity userEntity) {
        entityManager.merge(userEntity);
    }

    @Transactional
    public UserEntity findByLogin(String login) {
        UserEntity userEntity = null;
        try {
            userEntity = (UserEntity) entityManager.createQuery("SELECT p FROM UserEntity p WHERE p.login LIKE :login")
                    .setParameter("login",login)
                    .getSingleResult();
        } catch (NoResultException e){
            System.err.println("No result");
        }
        return userEntity;
    }

    @Transactional
    public void removeByLogin(String login) {
        UserEntity userEntity = findByLogin(login);
        if (userEntity != null) {
            entityManager.remove(userEntity);
        }
    }

    @Transactional
    public void removeConverterById(Long id){
        ConverterDataEntity converterDataEntity = entityManager.find(ConverterDataEntity.class, id);
        if(converterDataEntity != null){
            entityManager.remove(converterDataEntity);
        }
    }

    @Transactional
    public List<UserEntity> findAll() {
        return entityManager.createQuery("SELECT p FROM UserEntity p").getResultList();
    }
}
