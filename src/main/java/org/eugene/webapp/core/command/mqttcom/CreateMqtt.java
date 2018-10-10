package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.mqtt.MqttConnect;
import org.eugene.webapp.core.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.command.Command;

import java.util.List;

public class CreateMqtt extends TotalMqttCom implements Command {

    public CreateMqtt(MqttConnectOperations mqttConnectOperations) {
        super(mqttConnectOperations);
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public void perform() {
        mqttConnectOperations.addMqtt(new MqttConnect(arguments.get(0),arguments.get(1),arguments.get(2)));
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        if(arguments.size() == 3){
            super.arguments = arguments;
            return true;
        }
        return false;
    }

    @Override
    public String getDescriptionCommand() {
        return "create [nameMqtt, broker, clientId]";
    }
}
