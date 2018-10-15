package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.model.mqtt.MqttConnect;
import org.eugene.webapp.core.model.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.command.Command;

import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.addMessagesIntoBuffer;
import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class InformMqtt extends TotalMqttCom implements Command {

    public InformMqtt(MqttConnectOperations mqttConnectOperations) {
        super(mqttConnectOperations);
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public void perform() {
        MqttConnect currentMqttConnect = mqttConnectOperations.getCurrentMqttConnect();
        if(currentMqttConnect != null){
            System.out.println(currentMqttConnect);
            addMessagesIntoBuffer(currentMqttConnect.getMqttInfo());
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
        return "info []";
    }
}
