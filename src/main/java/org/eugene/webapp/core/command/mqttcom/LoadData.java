package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.command.Command;

import java.util.List;

public class LoadData extends TotalMqttCom implements Command {
    public LoadData(MqttConnectOperations mqttConnectOperations) {
        super(mqttConnectOperations);
    }

    @Override
    public String getName() {
        return "load";
    }

    @Override
    public void perform() {
        mqttConnectOperations.getUserOperation().loadUsersFromDB();
        mqttConnectOperations.loadMqttConnectsFromDB();
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        return arguments.size() == 0;
    }

    @Override
    public String getDescriptionCommand() {
        return "load []";
    }
}
