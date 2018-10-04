package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.user.UserOperation;
import org.eugene.webapp.core.command.Command;

import java.util.List;

public class AllUsers extends TotalUserCom implements Command {
    public AllUsers(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "*";
    }

    @Override
    public void perform() {
        userOperation.printAllUsers();
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        return arguments.size() == 0;
    }

    @Override
    public String getDescriptionCommand() {
        return "* []";
    }
}
