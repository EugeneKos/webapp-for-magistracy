package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.mqtt.MqttConnectOperations;

import java.util.List;

public class SaveData extends TotalMqttCom implements Command {
    public SaveData(MqttConnectOperations mqttConnectOperations) {
        super(mqttConnectOperations);
    }

    @Override
    public String getName() {
        return "save-all";
    }

    @Override
    public void perform() {
        if (arguments.size() == 0) {
            mqttConnectOperations.getUserOperation().saveUsers();
            mqttConnectOperations.saveMqttConnects();
        }
        if (arguments.size() == 1 && arguments.get(0).equals("base")) {
            mqttConnectOperations.getUserOperation().saveUsersIntoDB();
            mqttConnectOperations.saveMqttConnectsIntoDB();
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
        return "save-all [] [base]";
    }
}
