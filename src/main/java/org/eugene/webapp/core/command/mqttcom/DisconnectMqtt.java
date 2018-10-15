package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.model.mqtt.MqttConnect;
import org.eugene.webapp.core.model.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.command.Command;

import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class DisconnectMqtt extends TotalMqttCom implements Command {

    public DisconnectMqtt(MqttConnectOperations mqttConnectOperations) {
        super(mqttConnectOperations);
    }

    @Override
    public String getName() {
        return "disconnect";
    }

    @Override
    public void perform() {
        if(arguments.size() > 0){
            for (int i=0; i<arguments.size(); i++){
                MqttConnect mqttConnect = mqttConnectOperations.getMqttConnectByName(arguments.get(i));
                disconnectCurrentMqtt(mqttConnect);
            }
        } else {
            MqttConnect currentMqttConnect = mqttConnectOperations.getCurrentMqttConnect();
            disconnectCurrentMqtt(currentMqttConnect);
        }
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        super.arguments = arguments;
        return true;
    }

    @Override
    public String getDescriptionCommand() {
        return "disconnect [] [mqtt 1, ... mqtt n]";
    }

    private void disconnectCurrentMqtt(MqttConnect currentMqttConnect){
        if(currentMqttConnect != null){
            currentMqttConnect.closeMqtt();
        } else {
            printSystemInformation("mqtt connection does not exist");
        }
    }
}
