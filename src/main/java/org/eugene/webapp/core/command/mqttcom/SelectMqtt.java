package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.command.Command;

import java.util.List;

public class SelectMqtt extends TotalMqttCom implements Command {

    public SelectMqtt(MqttConnectOperations mqttConnectOperations) {
        super(mqttConnectOperations);
    }

    @Override
    public String getName() {
        return "select";
    }

    @Override
    public void perform() {
        mqttConnectOperations.selectMqtt(arguments.get(0));
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        if(arguments.size() == 1){
            super.arguments = arguments;
            return true;
        }
        return false;
    }

    @Override
    public String getDescriptionCommand() {
        return "select [nameMqtt]";
    }
}
