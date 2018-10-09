package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.parsing.filter.DataFilter;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;

import java.util.List;

import static org.eugene.webapp.core.printer.PrintInformation.addMessagesIntoBuffer;
import static org.eugene.webapp.core.printer.PrintInformation.printSystemInformation;

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
        if (arguments.size() == 1) {
            User user = userOperation.getCurrentUser();
            if (user != null) {
                DataFilter dataFilter = user.getFilters().get(arguments.get(0));
                if (dataFilter != null) {
                    System.out.println(dataFilter);
                    addMessagesIntoBuffer(dataFilter.getFilterInfo());
                    userOperation.setDataFilter(dataFilter);
                } else {
                    printSystemInformation("filter not found");
                }
            } else {
                printSystemInformation("user not found !!!");
            }
        } else if (arguments.size() == 0) {
            DataFilter dataFilter = userOperation.getDataFilter();
            if (dataFilter != null) {
                System.out.println(dataFilter);
                addMessagesIntoBuffer(dataFilter.getFilterInfo());
            } else {
                printSystemInformation("filter not found");
            }
        }
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        if (arguments.size() <= 1) {
            super.arguments = arguments;
            return true;
        }
        return false;
    }

    @Override
    public String getDescriptionCommand() {
        return "filter-info [] [filter name]";
    }
}
