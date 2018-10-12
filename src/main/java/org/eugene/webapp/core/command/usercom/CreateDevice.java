package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.device.Device;
import org.eugene.webapp.core.user.UserOperation;
import org.eugene.webapp.core.utils.ScriptCreator;

import java.util.List;

public class CreateDevice extends TotalUserCom implements Command {
    public CreateDevice(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "device-cr";
    }

    @Override
    public void perform() {
        Device device = ScriptCreator.createDevice(arguments.get(0));
        if(device != null){
            userOperation.addDeviceIntoDB(device);
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
        return "device-cr [file script name]";
    }
}
