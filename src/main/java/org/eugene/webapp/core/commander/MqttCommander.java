package org.eugene.webapp.core.commander;

import org.eugene.webapp.core.command.mqttcom.*;
import org.eugene.webapp.core.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.user.UserOperation;
import org.springframework.stereotype.Component;

@Component
public class MqttCommander extends Commander {
    public MqttCommander(MqttConnectOperations mqttConnectOperations, UserOperation userOperation) {
        super(mqttConnectOperations, userOperation);
    }

    @Override
    protected void initialCommander() {
        addCommand(new AllMqttConnections(mqttConnectOperations));
        addCommand(new CreateMqtt(mqttConnectOperations));
        addCommand(new DisconnectMqtt(mqttConnectOperations));
        addCommand(new InformMqtt(mqttConnectOperations));
        addCommand(new PrintDataMqtt(mqttConnectOperations));
        addCommand(new RemoveMqtt(mqttConnectOperations));
        addCommand(new SelectMqtt(mqttConnectOperations));
        addCommand(new SetUserNameAndPass(mqttConnectOperations));
        addCommand(new StartMqtt(mqttConnectOperations));
        addCommand(new Subscribe(mqttConnectOperations));
        addCommand(new Unsubscribe(mqttConnectOperations));
        addCommand(new StopLoopPrint(mqttConnectOperations));
        addCommand(new AddUser(mqttConnectOperations));
        addCommand(new RemoveUser(mqttConnectOperations));
        addCommand(new LoadData(mqttConnectOperations));
        addCommand(new RemoveUserFromAllMqtt(mqttConnectOperations));
        addCommand(new StartMqttAll(mqttConnectOperations));
        addCommand(new DisconnectMqttAll(mqttConnectOperations));
    }
}
