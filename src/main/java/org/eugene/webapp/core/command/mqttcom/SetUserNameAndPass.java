package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.mqtt.MqttConnect;
import org.eugene.webapp.core.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.command.Command;

import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class SetUserNameAndPass extends TotalMqttCom implements Command {

    public SetUserNameAndPass(MqttConnectOperations mqttConnectOperations) {
        super(mqttConnectOperations);
    }

    @Override
    public String getName() {
        return "setusrpass";
    }

    @Override
    public void perform() {
        MqttConnect currentMqttConnect = mqttConnectOperations.getCurrentMqttConnect();
        if(currentMqttConnect != null){
            currentMqttConnect.setUserNameAndPass(arguments.get(0),arguments.get(1));
        } else {
            printSystemInformation("mqtt connection does not exist");
        }
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        if(arguments.size() == 2){
            super.arguments = arguments;
            return true;
        }
        return false;
    }

    @Override
    public String getDescriptionCommand() {
        return "setusrpass [userName password]";
    }
}
