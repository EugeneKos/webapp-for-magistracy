package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.model.mqtt.MqttConnect;
import org.eugene.webapp.core.model.user.User;
import org.eugene.webapp.core.model.user.UserOperation;
import org.eugene.webapp.core.model.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.command.Command;

import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class RemoveUser extends TotalMqttCom implements Command {
    public RemoveUser(MqttConnectOperations mqttConnectOperations) {
        super(mqttConnectOperations);
    }

    @Override
    public String getName() {
        return "usr-rm";
    }

    @Override
    public void perform() {
        UserOperation userOperation = mqttConnectOperations.getUserOperation();
        User user = userOperation.getUserByLogin(arguments.get(0));
        if(user != null){
            MqttConnect currentMqttConnect = mqttConnectOperations.getCurrentMqttConnect();
            if(currentMqttConnect != null){
                currentMqttConnect.removeUser(user);
                mqttConnectOperations.removeUserNameFromLinkTable(currentMqttConnect.getMqttName(),user.getLogin());
            } else {
                printSystemInformation("mqtt connection does not exist");
            }
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
        return "usr-rm [loginUser]";
    }
}
