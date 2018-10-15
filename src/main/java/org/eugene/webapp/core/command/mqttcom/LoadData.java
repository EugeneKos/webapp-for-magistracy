package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.model.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.command.Command;

import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

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
        printSystemInformation("loading is complete");
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
