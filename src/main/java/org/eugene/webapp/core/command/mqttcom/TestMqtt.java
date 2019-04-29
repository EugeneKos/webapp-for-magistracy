package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.model.mqtt.MqttConnect;
import org.eugene.webapp.core.model.mqtt.MqttConnectOperations;

import java.util.List;

public class TestMqtt extends TotalMqttCom implements Command {
    public TestMqtt(MqttConnectOperations mqttConnectOperations) {
        super(mqttConnectOperations);
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public void perform() {
        MqttConnect mqttConnect = new MqttConnect("test", arguments.get(0), "test");
        mqttConnect.startMqtt();
        mqttConnect.subscribe("#", 2);
        if(mqttConnect.isConnected().equals("true")){
            mqttConnect.closeMqtt();
        }
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
        return "test [mqtt broker address]";
    }
}
