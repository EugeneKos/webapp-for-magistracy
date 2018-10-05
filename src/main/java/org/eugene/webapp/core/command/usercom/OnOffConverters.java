package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;

import java.util.List;

import static org.eugene.webapp.core.printer.PrintInformation.printSystemInformation;

public class OnOffConverters extends TotalUserCom implements Command {
    public OnOffConverters(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "converters";
    }

    @Override
    public void perform() {
        User user = userOperation.getCurrentUser();
        if(user != null){
            if(arguments.get(0).equals("on")) {
                user.setIsConverters(true);
                printSystemInformation("converters included");
            }
            if(arguments.get(0).equals("off")) {
                user.setIsConverters(false);
                printSystemInformation("converters are disabled");
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
        return "converters [param on/off]";
    }
}
