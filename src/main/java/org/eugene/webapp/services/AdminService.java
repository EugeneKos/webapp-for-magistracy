package org.eugene.webapp.services;

import org.eugene.webapp.core.commander.HandlerCommand;
import org.eugene.webapp.core.utils.ScriptCreator;
import org.eugene.webapp.core.utils.PrintInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class AdminService {
    private final HandlerCommand handlerCommand;

    @Autowired
    public AdminService(HandlerCommand handlerCommand) {
        this.handlerCommand = handlerCommand;
    }

    public List<String> executeCommand(String command){
        return handlerCommand.handleCommand(command);
    }

    public LinkedList<String> getOperationBuffer(){
        return PrintInformation.getOperationBuffer();
    }

    public void setPathToScripts(String path){
        ScriptCreator.setPathToScripts(path);
    }
}
