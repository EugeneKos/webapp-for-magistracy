package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.model.user.UserOperation;
import org.eugene.webapp.core.command.Command;

import java.util.List;

public class SelectUser extends TotalUserCom implements Command {
    public SelectUser(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "select";
    }

    @Override
    public void perform() {
        userOperation.selectUser(arguments.get(0));
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        if(arguments.size() == 1){
            super.arguments = arguments;
            return true;
        }
        return false;
    }

    @Override
    public String getDescriptionCommand() {
        return "select [userName]";
    }
}
