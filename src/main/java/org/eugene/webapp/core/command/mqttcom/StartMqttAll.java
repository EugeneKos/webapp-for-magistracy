package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.model.mqtt.MqttConnectOperations;

import java.util.List;

public class StartMqttAll extends TotalMqttCom implements Command {
    public StartMqttAll(MqttConnectOperations mqttConnectOperations) {
        super(mqttConnectOperations);
    }

    @Override
    public String getName() {
        return "start-all";
    }

    @Override
    public void perform() {
        mqttConnectOperations.startAllMqtt();
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        return arguments.size() == 0;
    }

    @Override
    public String getDescriptionCommand() {
        return "start-all []";
    }
}
