package org.eugene.webapp.core.db.dao;

import org.eugene.webapp.core.db.model.mqtt.MqttConnectEntity;
import org.eugene.webapp.core.db.model.mqtt.SubscribeEntity;
import org.eugene.webapp.core.db.model.mqtt.UserNameEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
@Transactional
public class MqttConnectDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void persist(MqttConnectEntity mqttConnectEntity){
        entityManager.persist(mqttConnectEntity);
    }

    @Transactional
    public void update(MqttConnectEntity mqttConnectEntity) {
        entityManager.merge(mqttConnectEntity);
    }

    @Transactional
    public MqttConnectEntity findByMqttName(String mqttName) {
        MqttConnectEntity mqttConnectEntity = null;
        try {
            mqttConnectEntity = (MqttConnectEntity) entityManager.createQuery("SELECT p FROM MqttConnectEntity p WHERE p.mqttName LIKE :mqttName")
                    .setParameter("mqttName",mqttName)
                    .getSingleResult();
        } catch (NoResultException e){
            System.err.println("No result");
        }
        return mqttConnectEntity;
    }

    @Transactional
    public void removeByMqttName(String mqttName) {
        MqttConnectEntity mqttConnectEntity = findByMqttName(mqttName);
        if (mqttConnectEntity != null) {
            entityManager.remove(mqttConnectEntity);
        }
    }

    @Transactional
    public void removeSubscribeById(Long id){
        SubscribeEntity subscribeEntity = entityManager.find(SubscribeEntity.class,id);
        if(subscribeEntity != null){
            entityManager.remove(subscribeEntity);
        }
    }

    @Transactional
    public void removeUserNameById(Long id){
        UserNameEntity userNameEntity = entityManager.find(UserNameEntity.class,id);
        if(userNameEntity != null){
            entityManager.remove(userNameEntity);
        }
    }

    @Transactional
    public List<MqttConnectEntity> findAll() {
        return entityManager.createQuery("SELECT p FROM MqttConnectEntity p").getResultList();
    }
}
