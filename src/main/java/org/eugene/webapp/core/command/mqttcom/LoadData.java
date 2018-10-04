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
        if (arguments.size() == 0) {
            mqttConnectOperations.getUserOperation().loadUsers();
            mqttConnectOperations.loadMqttConnects();
        }
        if (arguments.size() == 1 && arguments.get(0).equals("base")) {
            mqttConnectOperations.getUserOperation().loadUsersFromDB();
            mqttConnectOperations.loadMqttConnectsFromDB();
        }
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        if (arguments.size() == 1 && arguments.get(0).equals("base")) {
            super.arguments = arguments;
            return true;
        }
        if (arguments.size() == 0) {
            super.arguments = arguments;
            return true;
        }
        return false;
    }

    @Override
    public String getDescriptionCommand() {
        return "load [] [base]";
    }
}
