package org.eugene.webapp.core.commander;

import org.eugene.webapp.core.command.usercom.*;
import org.eugene.webapp.core.model.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.model.user.UserOperation;
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
        addCommand(new CreateFilter(userOperation));
        addCommand(new AddFilterIntoUser(userOperation));
        addCommand(new RemoveFilterFromUser(userOperation));
        addCommand(new RemoveFilter(userOperation));
        addCommand(new UserInfo(userOperation));
        addCommand(new StopLoopPrint(userOperation));
        addCommand(new PrintQueue(userOperation));
        addCommand(new FiltersOnOff(userOperation));
        addCommand(new SetBuffer(userOperation));
        addCommand(new PublishMessage(userOperation));
        addCommand(new SetPassword(userOperation));
        addCommand(new SetRole(userOperation));
        addCommand(new FilterInfo(userOperation));
        addCommand(new PrintParseDataFilter(userOperation));
        addCommand(new SetPathToScripts(userOperation));
        addCommand(new CreateDevice(userOperation));
        addCommand(new AddDeviceIntoUser(userOperation));
        addCommand(new RemoveDeviceFromUser(userOperation));
        addCommand(new RemoveDevice(userOperation));
        addCommand(new DeviceInfo(userOperation));
        addCommand(new Devices(userOperation));
        addCommand(new Filters(userOperation));
        addCommand(new PathToScriptsInfo(userOperation));
    }
}
