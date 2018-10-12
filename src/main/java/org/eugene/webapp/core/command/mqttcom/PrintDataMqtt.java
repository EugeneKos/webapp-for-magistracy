package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.mqtt.MqttConnect;
import org.eugene.webapp.core.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.command.Command;

import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class PrintDataMqtt extends TotalMqttCom implements Command {

    public PrintDataMqtt(MqttConnectOperations mqttConnectOperations) {
        super(mqttConnectOperations);
    }

    @Override
    public String getName() {
        return "print";
    }

    @Override
    public void perform() {
        MqttConnect currentMqttConnect = mqttConnectOperations.getCurrentMqttConnect();
        if(currentMqttConnect != null){
            currentMqttConnect.setResolutionPrint(true);
        } else {
            printSystemInformation("mqtt connection does not exist");
        }
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
       return arguments.size() == 0;
    }

    @Override
    public String getDescriptionCommand() {
        return "print []";
    }
}
