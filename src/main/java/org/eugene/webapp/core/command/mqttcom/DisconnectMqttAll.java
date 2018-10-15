package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.model.mqtt.MqttConnectOperations;

import java.util.List;

public class DisconnectMqttAll extends TotalMqttCom implements Command {
    public DisconnectMqttAll(MqttConnectOperations mqttConnectOperations) {
        super(mqttConnectOperations);
    }

    @Override
    public String getName() {
        return "disconnect-all";
    }

    @Override
    public void perform() {
        mqttConnectOperations.disconnectAllMqtt();
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        return arguments.size() == 0;
    }

    @Override
    public String getDescriptionCommand() {
        return "disconnect-all []";
    }
}
