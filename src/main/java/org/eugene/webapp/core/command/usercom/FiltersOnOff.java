package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;

import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class FiltersOnOff extends TotalUserCom implements Command {
    public FiltersOnOff(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "filters";
    }

    @Override
    public void perform() {
        User user = userOperation.getCurrentUser();
        if(user != null){
            if(arguments.get(0).equals("on")) {
                user.setIsFilters(true);
                printSystemInformation("filters included");
            }
            if(arguments.get(0).equals("off")) {
                user.setIsFilters(false);
                printSystemInformation("filters are disabled");
            }
        } else {
            printSystemInformation("current user not found !!!");
        }
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
        return "filters [param on/off]";
    }
}
