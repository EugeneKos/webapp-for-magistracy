package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.model.user.UserOperation;
import org.eugene.webapp.core.command.Command;

import java.util.List;

public class RemoveUser extends TotalUserCom implements Command {
    public RemoveUser(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public void perform() {
        userOperation.removeUser();
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        return arguments.size() == 0;
    }

    @Override
    public String getDescriptionCommand() {
        return "remove";
    }
}
