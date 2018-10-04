package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.parsing.ConverterData;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;
import org.eugene.webapp.core.command.Command;

import java.util.List;

import static org.eugene.webapp.core.parsing.CreateConverterData.createConverter;
import static org.eugene.webapp.core.printer.PrintInformation.printSystemInformation;

public class AddConverter extends TotalUserCom implements Command {
    public AddConverter(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "converter-cr";
    }

    @Override
    public void perform() {
        User user = userOperation.getCurrentUser();
        if(user != null){
            ConverterData converterData = createConverter(arguments.get(0));
            if(converterData != null){
                user.addConverter(converterData);
                userOperation.saveUsers();
                userOperation.addConverterDataIntoDB(user.getLogin(),converterData);
            } else {
                printSystemInformation("error creating the converter, the converter was not created");
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
        return "converter-cr [file script name]";
    }
}
