package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.model.user.UserOperation;

import java.util.List;

public class Filters extends TotalUserCom implements Command {
    public Filters(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "filters";
    }

    @Override
    public void perform() {
        userOperation.printAllDataFilters();
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        return arguments.size() == 0;
    }

    @Override
    public String getDescriptionCommand() {
        return "filters []";
    }
}
