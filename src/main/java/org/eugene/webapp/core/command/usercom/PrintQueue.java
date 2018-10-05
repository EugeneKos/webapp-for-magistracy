package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;

import java.util.List;

import static org.eugene.webapp.core.printer.PrintInformation.printSystemInformation;

public class PrintQueue extends TotalUserCom implements Command {
    public PrintQueue(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "print";
    }

    @Override
    public void perform() {
        User user = userOperation.getCurrentUser();
        if(user != null){
            user.setResolutionPrint(true);
        } else {
            printSystemInformation("current user not found !!!");
        }
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        return arguments.size() == 0;
    }

    @Override
    public String getDescriptionCommand() {
        return "print []";
    }
}
