package org.eugene.webapp.core.commander;

import org.eugene.webapp.core.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.eugene.webapp.core.command.Commands.*;
import static org.eugene.webapp.core.utils.PrintInformation.*;

@Component
public class HandlerCommand {
    private final ControllerCommander controllerCommander;

    @Autowired
    public HandlerCommand(ControllerCommander controllerCommander) {
        this.controllerCommander = controllerCommander;
    }

    public List<String> handleCommand(String command){
        parseComString(command);
        callCommand();
        return getResultCommand();
    }

    private void callCommand() {
        if (getNameSection().equals("")) {
            callSpecialCommand();
            return;
        }
        if (!containsSection(getNameSection())) {
            printAllSections();
            return;
        }
        Command command = controllerCommander.getCommander(getNameSection()).getCommand(getNameCommand());
        if (command != null) {
            if (command.checkArgs(getArguments())) {
                command.perform();
            } else {
                printSystemInformation("incorrect command < incorrect arguments >");
            }
        }

    }

    private void callSpecialCommand() {
        for (String section : controllerCommander.getSections()) {
            Commander commander = controllerCommander.getCommander(section);
            Command command = commander.getCommand("stop.loop.print");
            if(command != null){
                command.perform();
            }
        }
    }

    private void printAllSections(){
        printCap();
        for (String section : controllerCommander.getSections()) {
            printFormatInformation(section);
        }
        printCap();
    }

    private boolean containsSection(String nameSection) {
        return controllerCommander.getSections().contains(nameSection);
    }

    private List<String> getResultCommand(){
        List<String> messageBuffer = new ArrayList<>(getMessageBuffer());
        clearMessageBuffer();
        return messageBuffer;
    }
}
