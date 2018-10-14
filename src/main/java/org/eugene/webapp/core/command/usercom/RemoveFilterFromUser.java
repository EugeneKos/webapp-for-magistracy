package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.filter.DataFilter;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;
import org.eugene.webapp.core.command.Command;

import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class RemoveFilterFromUser extends TotalUserCom implements Command {
    public RemoveFilterFromUser(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "filter-usr-rm";
    }

    @Override
    public void perform() {
        User user = userOperation.getCurrentUser();
        if(user != null){
            DataFilter dataFilter = user.removeFilter(arguments.get(0));
            if(dataFilter != null){
                userOperation.removeDataFilterFromLinkTable(user.getLogin(),dataFilter);
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
        return "filter-usr-rm [filter name]";
    }
}
