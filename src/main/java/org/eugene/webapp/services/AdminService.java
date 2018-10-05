package org.eugene.webapp.services;

import org.eugene.webapp.core.commander.HandlerCommand;
import org.eugene.webapp.core.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.parsing.CreateConverterData;
import org.eugene.webapp.core.save.WriterReaderFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final HandlerCommand handlerCommand;
    private final MqttConnectOperations mqttConnectOperations;

    @Autowired
    public AdminService(HandlerCommand handlerCommand, MqttConnectOperations mqttConnectOperations) {
        this.handlerCommand = handlerCommand;
        this.mqttConnectOperations = mqttConnectOperations;
    }

    public List<String> executeCommand(String command){
        return handlerCommand.handleCommand(command);
    }

    public void setPathToDB(String path){
        WriterReaderFileUtil.setPathToDB(path);
    }

    public void setPathToScripts(String path){
        CreateConverterData.setPathToScripts(path);
    }
}
