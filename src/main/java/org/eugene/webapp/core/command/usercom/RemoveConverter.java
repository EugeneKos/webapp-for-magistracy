package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.parsing.ConverterData;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;
import org.eugene.webapp.core.command.Command;

import java.util.List;

import static org.eugene.webapp.core.printer.PrintInformation.printSystemInformation;

public class RemoveConverter extends TotalUserCom implements Command {
    public RemoveConverter(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "converter-rm";
    }

    @Override
    public void perform() {
        User user = userOperation.getCurrentUser();
        if(user != null){
            ConverterData converterData = user.removeConverter(arguments.get(0));
            userOperation.saveUsers();
            userOperation.removeConverterDataFromDB(user.getLogin(),converterData);
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
        return "converter-rm [mqttName@topicName]";
    }
}
