package org.eugene.webapp.core.db.dao;

import org.eugene.webapp.core.mqtt.MqttConnect;
import org.eugene.webapp.core.mqtt.Subscribe;
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
    public void persist(MqttConnect mqttConnect){
        entityManager.persist(mqttConnect);
    }

    @Transactional
    public void update(MqttConnect mqttConnect) {
        entityManager.merge(mqttConnect);
    }

    @Transactional
    public MqttConnect findByMqttName(String mqttName) {
        MqttConnect mqttConnect = null;
        try {
            mqttConnect = (MqttConnect) entityManager.createQuery("SELECT p FROM MqttConnect p WHERE p.mqttName LIKE :mqttName")
                    .setParameter("mqttName",mqttName)
                    .getSingleResult();
        } catch (NoResultException e){
            System.err.println("No result");
        }
        return mqttConnect;
    }

    @Transactional
    public void removeByMqttName(String mqttName) {
        MqttConnect mqttConnect = findByMqttName(mqttName);
        if (mqttConnect != null) {
            entityManager.remove(mqttConnect);
        }
    }

    @Transactional
    public void removeSubscribeById(Long id){
        Subscribe subscribe = entityManager.find(Subscribe.class,id);
        if(subscribe != null){
            entityManager.remove(subscribe);
        }
    }

    @Transactional
    public List<MqttConnect> findAll() {
        return entityManager.createQuery("SELECT p FROM MqttConnect p").getResultList();
    }
}
