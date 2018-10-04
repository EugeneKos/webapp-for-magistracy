package org.eugene.webapp.core.db.model.mqtt;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "mqtt_connects")
public class MqttConnectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "mqtt_name", unique = true)
    private String mqttName;
    @Column(name = "broker_name")
    private String broker;
    @Column(name = "clientId")
    private String clientId;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "mqttConnectEntity")
    private Set<UserNameEntity> userNameEntities;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "mqttConnectEntity")
    private Set<SubscribeEntity> subscribeEntities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMqttName() {
        return mqttName;
    }

    public void setMqttName(String mqttName) {
        this.mqttName = mqttName;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void addUserNameEntity(UserNameEntity userNameEntity){
        userNameEntities.add(userNameEntity);
    }

    public Long getUserNameEntityId(String userName){
        for (UserNameEntity userNameEntity : userNameEntities){
            if(userNameEntity.getUserName().equals(userName)){
                return userNameEntity.getId();
            }
        }
        return null;
    }

    public Set<UserNameEntity> getUserNameEntities() {
        return userNameEntities;
    }

    public void setUserNameEntities(Set<UserNameEntity> userNameEntities) {
        this.userNameEntities = userNameEntities;
    }

    public void addSubscribeEntity(SubscribeEntity subscribeEntity){
        subscribeEntities.add(subscribeEntity);
    }

    public Long getSubscribeEntityId(String subscribes){
        for (SubscribeEntity subscribeEntity : subscribeEntities){
            if(subscribeEntity.getSubscribe().equals(subscribes)){
                return subscribeEntity.getId();
            }
        }
        return null;
    }

    public Set<SubscribeEntity> getSubscribeEntities() {
        return subscribeEntities;
    }

    public void setSubscribeEntities(Set<SubscribeEntity> subscribeEntities) {
        this.subscribeEntities = subscribeEntities;
    }
}
