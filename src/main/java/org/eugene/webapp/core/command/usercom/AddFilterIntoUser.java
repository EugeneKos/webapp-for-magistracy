package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.model.filter.DataFilter;
import org.eugene.webapp.core.model.user.User;
import org.eugene.webapp.core.model.user.UserOperation;
import org.eugene.webapp.core.command.Command;

import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class AddFilterIntoUser extends TotalUserCom implements Command {
    public AddFilterIntoUser(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "filter-usr-add";
    }

    @Override
    public void perform() {
        User user = userOperation.getCurrentUser();
        if(user != null){
            DataFilter dataFilter = userOperation.getDataFilterByName(arguments.get(0));
            if(dataFilter != null){
                user.addFilter(dataFilter);
                userOperation.addFilterIntoUserAndUpdate(dataFilter);
            } else {
                printSystemInformation("filter with name < "+arguments.get(0)+" > not found in data base");
            }
        } else {
            printSystemInformation("user not found !!!");
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
        return "filter-usr-add [filter name]";
    }
}
