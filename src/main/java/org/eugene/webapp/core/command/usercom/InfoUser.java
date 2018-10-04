package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;
import org.eugene.webapp.core.command.Command;

import java.util.List;

import static org.eugene.webapp.core.printer.PrintInformation.printSystemInformation;

public class InfoUser extends TotalUserCom implements Command {
    public InfoUser(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public void perform() {
        User user = userOperation.getCurrentUser();
        if(user != null){
            System.out.println(user);
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
        return "info []";
    }
}