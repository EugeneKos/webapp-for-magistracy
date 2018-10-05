package org.eugene.webapp.core.commander;

import org.eugene.webapp.core.user.UserOperation;
import org.eugene.webapp.core.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.command.StopPrintCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.eugene.webapp.core.printer.PrintInformation.printFormatInformation;
import static org.eugene.webapp.core.printer.PrintInformation.printCap;

@Component
public abstract class Commander {
    private List<Command> commandList = new ArrayList<>();
    protected final MqttConnectOperations mqttConnectOperations;
    protected final UserOperation userOperation;

    @Autowired
    public Commander(MqttConnectOperations mqttConnectOperations, UserOperation userOperation) {
        this.mqttConnectOperations = mqttConnectOperations;
        this.userOperation = userOperation;
    }

    public void loadCommander(){
        initialCommander();
    }

    protected void addCommand(Command command){
        commandList.add(command);
    }

    public Command getCommand(String name){
        for (Command command : commandList){
            if(command.getName().equals(name)){
                return command;
            }
        }
        printCommandNames();
        return null;
    }

    protected abstract void initialCommander();

    protected void printCommandNames(){
        printCap();
        for(Command command : commandList){
            if(!(command instanceof StopPrintCommand)){
                printFormatInformation(command.getDescriptionCommand());
            }
        }
        printCap();
    }
}
