package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.parsing.ConverterData;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;

import java.util.List;

import static org.eugene.webapp.core.printer.PrintInformation.addMessagesIntoBuffer;
import static org.eugene.webapp.core.printer.PrintInformation.printSystemInformation;

public class ConverterInfo extends TotalUserCom implements Command {
    public ConverterInfo(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "converter-info";
    }

    @Override
    public void perform() {
        if (arguments.size() == 1) {
            User user = userOperation.getCurrentUser();
            if (user != null) {
                ConverterData converterData = user.getConverters().get(arguments.get(0));
                if (converterData != null) {
                    System.out.println(converterData);
                    addMessagesIntoBuffer(converterData.getConverterInfo());
                    userOperation.setConverterData(converterData);
                } else {
                    printSystemInformation("converter not found");
                }
            } else {
                printSystemInformation("user not found !!!");
            }
        } else if (arguments.size() == 0) {
            ConverterData converterData = userOperation.getConverterData();
            if (converterData != null) {
                System.out.println(converterData);
                addMessagesIntoBuffer(converterData.getConverterInfo());
            } else {
                printSystemInformation("converter not found");
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
        return "converter-info [] [converter name]";
    }
}
