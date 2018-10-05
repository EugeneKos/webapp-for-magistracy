package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.parsing.ConverterData;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;
import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.command.StopPrintCommand;

import java.util.List;

public class StopLoopPrint extends TotalUserCom implements Command, StopPrintCommand {
    public StopLoopPrint(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "stop.loop.print";
    }

    @Override
    public void perform() {
        User user = userOperation.getCurrentUser();
        if(user != null){
            user.setResolutionPrint(false);
        }
        ConverterData converterData = userOperation.getConverterData();
        if(converterData != null){
            converterData.setResolutionPrint(false);
        }
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        return true;
    }

    @Override
    public String getDescriptionCommand() {
        return null;
    }
}
