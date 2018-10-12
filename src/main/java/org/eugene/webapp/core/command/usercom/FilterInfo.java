package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.filter.DataFilter;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;

import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.addMessagesIntoBuffer;
import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class FilterInfo extends TotalUserCom implements Command {
    public FilterInfo(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "filter-info";
    }

    @Override
    public void perform() {
        DataFilter dataFilter = userOperation.getDataFilterByName(arguments.get(0));
        if(dataFilter != null){
            System.out.println(dataFilter);
            addMessagesIntoBuffer(dataFilter.getFilterInfo());
        } else {
            printSystemInformation("filter bot found");
        }
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        if (arguments.size() == 1) {
            super.arguments = arguments;
            return true;
        }
        return false;
    }

    @Override
    public String getDescriptionCommand() {
        return "filter-info [filter name]";
    }
}
