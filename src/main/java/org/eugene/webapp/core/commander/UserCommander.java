package org.eugene.webapp.core.commander;

import org.eugene.webapp.core.command.usercom.*;
import org.eugene.webapp.core.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.user.UserOperation;
import org.springframework.stereotype.Component;

@Component
public class UserCommander extends Commander {
    public UserCommander(MqttConnectOperations mqttConnectOperations, UserOperation userOperation) {
        super(mqttConnectOperations, userOperation);
    }

    @Override
    protected void initialCommander() {
        addCommand(new CreateUser(userOperation));
        addCommand(new RemoveUser(userOperation));
        addCommand(new SelectUser(userOperation));
        addCommand(new AllUsers(userOperation));
        addCommand(new AddConverter(userOperation));
        addCommand(new RemoveConverter(userOperation));
        addCommand(new UserInfo(userOperation));
        addCommand(new StopLoopPrint(userOperation));
        addCommand(new PrintQueue(userOperation));
        addCommand(new OnOffConverters(userOperation));
        addCommand(new SetBuffer(userOperation));
        addCommand(new PublishMessage(userOperation));
        addCommand(new SetPassword(userOperation));
        addCommand(new SetRole(userOperation));
        addCommand(new ConverterInfo(userOperation));
        addCommand(new PrintParseDataConverter(userOperation));
        addCommand(new SetPathToScripts(userOperation));
        addCommand(new AddDevice(userOperation));
        addCommand(new RemoveDevice(userOperation));
        addCommand(new AddCommandToDevice(userOperation));
        addCommand(new RemoveCommandFromDevice(userOperation));
        addCommand(new DeviceInfo(userOperation));
    }
}
