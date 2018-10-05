package org.eugene.webapp.core.dto;

import java.util.HashSet;
import java.util.Set;

public class MqttConnectDto {
    private String mqttName;
    private String broker;
    private String clientId;
    private Set<String> userNames = new HashSet<>();
    private Set<String> setSubscribes = new HashSet<>();

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

    public Set<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(Set<String> userNames) {
        this.userNames = userNames;
    }

    public Set<String> getSetSubscribes() {
        return setSubscribes;
    }

    public void setSetSubscribes(Set<String> setSubscribes) {
        this.setSubscribes = setSubscribes;
    }
}
