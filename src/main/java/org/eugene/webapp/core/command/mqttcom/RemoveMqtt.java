package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.command.Command;

import java.util.List;

public class RemoveMqtt extends TotalMqttCom implements Command {

    public RemoveMqtt(MqttConnectOperations mqttConnectOperations) {
        super(mqttConnectOperations);
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public void perform() {
        mqttConnectOperations.removeMqtt();
        mqttConnectOperations.saveMqttConnects();
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        return arguments.size() == 0;
    }

    @Override
    public String getDescriptionCommand() {
        return "remove []";
    }
}
