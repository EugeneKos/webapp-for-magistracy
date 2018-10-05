package org.eugene.webapp.core.db.services;

import org.eugene.webapp.core.db.converters.MqttConnectConverterEntity;
import org.eugene.webapp.core.db.dao.MqttConnectDao;
import org.eugene.webapp.core.db.model.mqtt.MqttConnectEntity;
import org.eugene.webapp.core.db.model.mqtt.SubscribeEntity;
import org.eugene.webapp.core.db.model.mqtt.UserNameEntity;
import org.eugene.webapp.core.mqtt.MqttConnect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DbMqttConnectService {
    private final MqttConnectDao mqttConnectDao;
    private final MqttConnectConverterEntity mqttConnectConverterEntity;

    @Autowired
    public DbMqttConnectService(MqttConnectDao mqttConnectDao, MqttConnectConverterEntity mqttConnectConverterEntity) {
        this.mqttConnectDao = mqttConnectDao;
        this.mqttConnectConverterEntity = mqttConnectConverterEntity;
    }

    public void persist(MqttConnect mqttConnect){
        MqttConnectEntity mqttConnectEntity = mqttConnectConverterEntity.convertToMqttConnectEntity(mqttConnect);
        mqttConnectDao.persist(mqttConnectEntity);
    }

    public MqttConnectEntity findByMqttName(String mqttName){
        return mqttConnectDao.findByMqttName(mqttName);
    }

    public void removeByMqttName(String mqttName){
        mqttConnectDao.removeByMqttName(mqttName);
    }

    public void addSubscribeAndUpdate(String mqttName, String subscribe){
        MqttConnectEntity mqttConnectEntity = findByMqttName(mqttName);
        if(mqttConnectEntity == null) return;
        SubscribeEntity subscribeEntity = new SubscribeEntity();
        subscribeEntity.setSubscribe(subscribe);
        subscribeEntity.setMqttConnectEntity(mqttConnectEntity);
        mqttConnectEntity.addSubscribeEntity(subscribeEntity);
        mqttConnectDao.update(mqttConnectEntity);
    }

    public void removeSubscribeAndUpdate(String mqttName, String subscribe){
        MqttConnectEntity mqttConnectEntity = findByMqttName(mqttName);
        if(mqttConnectEntity == null) return;
        Long id = mqttConnectEntity.getSubscribeEntityId(subscribe);
        if(id != null){
            mqttConnectDao.removeSubscribeById(id);
        }
    }

    public void addUserNameAndUpdate(String mqttName, String userLogin){
        MqttConnectEntity mqttConnectEntity = findByMqttName(mqttName);
        if(mqttConnectEntity == null) return;
        UserNameEntity userNameEntity = new UserNameEntity();
        userNameEntity.setUserName(userLogin);
        userNameEntity.setMqttConnectEntity(mqttConnectEntity);
        mqttConnectEntity.addUserNameEntity(userNameEntity);
        mqttConnectDao.update(mqttConnectEntity);
    }

    public void removeUserNameAndUpdate(String mqttName, String userLogin){
        MqttConnectEntity mqttConnectEntity = findByMqttName(mqttName);
        if(mqttConnectEntity == null) return;
        Long id = mqttConnectEntity.getUserNameEntityId(userLogin);
        if(id != null){
            mqttConnectDao.removeUserNameById(id);
        }
    }

    public Set<MqttConnect> findAll(){
        List<MqttConnectEntity> mqttConnectEntities = mqttConnectDao.findAll();
        Set<MqttConnect> mqttConnects = new HashSet<>();
        for (MqttConnectEntity mqttConnectEntity : mqttConnectEntities){
            MqttConnect mqttConnect = mqttConnectConverterEntity.convertToMqttConnect(mqttConnectEntity);
            mqttConnects.add(mqttConnect);
        }
        return mqttConnects;
    }
}
