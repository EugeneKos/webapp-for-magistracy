package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.model.filter.DataFilter;
import org.eugene.webapp.core.model.user.UserOperation;

import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;
import static org.eugene.webapp.core.utils.ScriptCreator.createDataFilter;

public class CreateFilter extends TotalUserCom implements Command {
    public CreateFilter(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "filter-cr";
    }

    @Override
    public void perform() {
        DataFilter dataFilter = createDataFilter(arguments.get(0));
        if(dataFilter != null){
            userOperation.addDataFilterIntoDB(dataFilter);
        } else {
            printSystemInformation("error create filter, filter was not created");
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
        return "filter-cr [file script name]";
    }
}
