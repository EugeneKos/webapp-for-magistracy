package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.parsing.filter.DataFilter;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;
import org.eugene.webapp.core.command.Command;

import java.util.List;

import static org.eugene.webapp.core.printer.PrintInformation.printSystemInformation;

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
        User user = userOperation.getCurrentUser();
        if(user != null){
            DataFilter dataFilter = user.removeFilter(arguments.get(0));
            //userOperation.removeDataFilterFromDB(user.getLogin(), dataFilter);
            userOperation.updateUser();
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
        return "filter-rm [mqttName@topicName]";
    }
}
