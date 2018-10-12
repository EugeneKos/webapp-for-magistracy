package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.filter.DataFilter;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;

import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class PrintParseDataFilter extends TotalUserCom implements Command {
    public PrintParseDataFilter(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "filter-print";
    }

    @Override
    public void perform() {
        if (arguments.size() == 1) {
            User user = userOperation.getCurrentUser();
            if (user != null) {
                DataFilter dataFilter = user.getFilterByName(arguments.get(0));
                // todo: проверить метод
                if (dataFilter != null) {
                    dataFilter.setResolutionPrint(true);
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
                dataFilter.setResolutionPrint(true);
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
        return "filter-print [] [filter name]";
    }
}
