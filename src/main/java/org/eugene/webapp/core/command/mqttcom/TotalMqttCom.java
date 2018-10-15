package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.model.mqtt.MqttConnectOperations;

import java.util.List;

public class TotalMqttCom {
    protected List<String> arguments;
    protected MqttConnectOperations mqttConnectOperations;

    public TotalMqttCom(MqttConnectOperations mqttConnectOperations) {
        this.mqttConnectOperations = mqttConnectOperations;
    }
}
