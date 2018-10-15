package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.model.user.UserOperation;

import java.util.List;

public class RemoveFilter extends TotalUserCom implements Command {
    public RemoveFilter(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "filter-rm";
    }

    @Override
    public void perform() {
        userOperation.removeDataFilterFromDB(arguments.get(0));
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
        return "filter-rm [filter name]";
    }
}
