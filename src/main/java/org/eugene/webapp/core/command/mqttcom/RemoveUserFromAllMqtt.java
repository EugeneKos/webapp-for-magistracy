package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;

import java.util.List;

import static org.eugene.webapp.core.printer.PrintInformation.printSystemInformation;

public class RemoveUserFromAllMqtt extends TotalMqttCom implements Command {
    public RemoveUserFromAllMqtt(MqttConnectOperations mqttConnectOperations) {
        super(mqttConnectOperations);
    }

    @Override
    public String getName() {
        return "usr-rm-all";
    }

    @Override
    public void perform() {
        UserOperation userOperation = mqttConnectOperations.getUserOperation();
        User user = userOperation.getUserByLogin(arguments.get(0));
        if(user != null){
            mqttConnectOperations.removeUserFromAllMqtt(user);
            mqttConnectOperations.saveMqttConnects();
            printSystemInformation("user with login < " +arguments.get(0)+ " > was deleted into all mqtt connects");
        } else {
            printSystemInformation("user with login < " +arguments.get(0)+ " > does not exist");
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
        return "usr-rm-all [loginUser]";
    }
}
