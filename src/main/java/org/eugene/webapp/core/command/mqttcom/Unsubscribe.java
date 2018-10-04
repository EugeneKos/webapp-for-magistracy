package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.mqtt.MqttConnect;
import org.eugene.webapp.core.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.command.Command;

import java.util.List;

import static org.eugene.webapp.core.printer.PrintInformation.printSystemInformation;

public class Unsubscribe extends TotalMqttCom implements Command {

    public Unsubscribe(MqttConnectOperations mqttConnectOperations) {
        super(mqttConnectOperations);
    }

    @Override
    public String getName() {
        return "unsubscribe";
    }

    @Override
    public void perform() {
        String[] arguments = new String[super.arguments.size()];
        for (int i=0; i<arguments.length; i++){
            arguments[i] = super.arguments.get(i);
        }
        MqttConnect currentMqttConnect = mqttConnectOperations.getCurrentMqttConnect();
        if(currentMqttConnect != null){
            currentMqttConnect.unsubscribe(arguments);
            mqttConnectOperations.saveMqttConnects();
            mqttConnectOperations.removeSubscribesFromDB(currentMqttConnect.getMqttName(),arguments);
        } else {
            printSystemInformation("mqtt connection does not exist");
        }
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        if(arguments.size() >= 1){
            super.arguments = arguments;
            return true;
        }
        return false;
    }

    @Override
    public String getDescriptionCommand() {
        return "unsubscribe [topic 1, ..., topic n]";
    }
}
