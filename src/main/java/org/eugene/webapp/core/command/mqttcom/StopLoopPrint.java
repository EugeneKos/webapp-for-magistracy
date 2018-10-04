package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.mqtt.MqttConnect;
import org.eugene.webapp.core.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.command.StopPrintCommand;

import java.util.List;

public class StopLoopPrint extends TotalMqttCom implements Command, StopPrintCommand {

    public StopLoopPrint(MqttConnectOperations mqttConnectOperations) {
        super(mqttConnectOperations);
    }

    @Override
    public String getName() {
        return "stop.loop.print";
    }

    @Override
    public void perform() {
        MqttConnect currentMqttConnect = mqttConnectOperations.getCurrentMqttConnect();
        if(currentMqttConnect != null){
            currentMqttConnect.setResolutionPrint(false);
        }
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        return true;
    }

    @Override
    public String getDescriptionCommand() {
        return null;
    }
}
