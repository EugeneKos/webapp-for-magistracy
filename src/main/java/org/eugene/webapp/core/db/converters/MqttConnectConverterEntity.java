package org.eugene.webapp.core.db.converters;

import org.eugene.webapp.core.db.model.mqtt.MqttConnectEntity;
import org.eugene.webapp.core.db.model.mqtt.SubscribeEntity;
import org.eugene.webapp.core.db.model.mqtt.UserNameEntity;
import org.eugene.webapp.core.mqtt.MqttConnect;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class MqttConnectConverterEntity {
    public MqttConnectEntity convertToMqttConnectEntity(MqttConnect mqttConnect){
        MqttConnectEntity mqttConnectEntity = new MqttConnectEntity();
        mqttConnectEntity.setMqttName(mqttConnect.getMqttName());
        mqttConnectEntity.setBroker(mqttConnect.getBroker());
        mqttConnectEntity.setClientId(mqttConnect.getClientId());
        mqttConnectEntity.setSubscribeEntities(convertToSubscribesEntity(mqttConnect.getSetSubscribes(),mqttConnectEntity));
        mqttConnectEntity.setUserNameEntities(convertToUserNamesEntity(mqttConnect.getUserNames(),mqttConnectEntity));
        return mqttConnectEntity;
    }

    private Set<SubscribeEntity> convertToSubscribesEntity(Set<String> subscribes, MqttConnectEntity mqttConnectEntity){
        Set<SubscribeEntity> subscribeEntities = new HashSet<>();
        for (String subscribe : subscribes){
            SubscribeEntity subscribeEntity = new SubscribeEntity();
            subscribeEntity.setSubscribe(subscribe);
            subscribeEntity.setMqttConnectEntity(mqttConnectEntity);
            subscribeEntities.add(subscribeEntity);
        }
        return subscribeEntities;
    }

    private Set<UserNameEntity> convertToUserNamesEntity(Set<String> userNames, MqttConnectEntity mqttConnectEntity){
        Set<UserNameEntity> userNameEntities = new HashSet<>();
        for (String userName : userNames){
            UserNameEntity userNameEntity = new UserNameEntity();
            userNameEntity.setUserName(userName);
            userNameEntity.setMqttConnectEntity(mqttConnectEntity);
            userNameEntities.add(userNameEntity);
        }
        return userNameEntities;
    }

    public MqttConnect convertToMqttConnect(MqttConnectEntity mqttConnectEntity){
        MqttConnect mqttConnect = new MqttConnect(mqttConnectEntity.getMqttName(),mqttConnectEntity.getBroker(),mqttConnectEntity.getClientId());
        mqttConnect.setSetSubscribes(convertToSubscribes(mqttConnectEntity.getSubscribeEntities()));
        mqttConnect.setUserNames(convertToUserNames(mqttConnectEntity.getUserNameEntities()));
        return mqttConnect;
    }

    private Set<String> convertToSubscribes(Set<SubscribeEntity> subscribesEntities){
        Set<String> subscribes = new HashSet<>();
        for (SubscribeEntity subscribeEntity : subscribesEntities){
            subscribes.add(subscribeEntity.getSubscribe());
        }
        return subscribes;
    }

    private Set<String> convertToUserNames(Set<UserNameEntity> userNameEntities){
        Set<String> userNames = new HashSet<>();
        for (UserNameEntity userNameEntity : userNameEntities){
            userNames.add(userNameEntity.getUserName());
        }
        return userNames;
    }
}
