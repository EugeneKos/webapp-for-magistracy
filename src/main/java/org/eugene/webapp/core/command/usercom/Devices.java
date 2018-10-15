package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.user.UserOperation;

import java.util.List;

public class Devices extends TotalUserCom implements Command {
    public Devices(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "devices";
    }

    @Override
    public void perform() {
        userOperation.printAllDevices();
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        return arguments.size() == 0;
    }

    @Override
    public String getDescriptionCommand() {
        return "devices []";
    }
}
