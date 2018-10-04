package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.mqtt.MqttConnect;
import org.eugene.webapp.core.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.command.Command;

import java.util.List;

import static org.eugene.webapp.core.printer.PrintInformation.printSystemInformation;

public class StartMqtt extends TotalMqttCom implements Command {

    public StartMqtt(MqttConnectOperations mqttConnectOperations) {
        super(mqttConnectOperations);
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public void perform() {
        if(arguments.size() > 0){
            for (int i=0; i<arguments.size(); i++){
                MqttConnect mqttConnect = mqttConnectOperations.getMqttConnectByName(arguments.get(i));
                startCurrentMqtt(mqttConnect);
            }
        } else {
            MqttConnect currentMqttConnect = mqttConnectOperations.getCurrentMqttConnect();
            startCurrentMqtt(currentMqttConnect);
        }
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        super.arguments = arguments;
        return true;
    }

    @Override
    public String getDescriptionCommand() {
        return "start [] [mqtt 1, ... mqtt n]";
    }

    private void startCurrentMqtt(MqttConnect currentMqttConnect){
        if(currentMqttConnect != null){
            currentMqttConnect.startMqtt();
        } else {
            printSystemInformation("mqtt connection does not exist");
        }
    }
}
