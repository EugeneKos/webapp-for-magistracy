package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.user.UserOperation;

import java.util.List;

public class RemoveDevice extends TotalUserCom implements Command {
    public RemoveDevice(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "device-rm";
    }

    @Override
    public void perform() {
        userOperation.removeDeviceFromDB(arguments.get(0));
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
        return "device-rm [deviceName]";
    }
}
