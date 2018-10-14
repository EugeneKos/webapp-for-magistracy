package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.device.Device;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;

import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class AddDeviceIntoUser extends TotalUserCom implements Command {
    public AddDeviceIntoUser(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "device-usr-add";
    }

    @Override
    public void perform() {
        User user = userOperation.getCurrentUser();
        if(user != null){
            Device device = userOperation.getDeviceByName(arguments.get(0));
            if(device != null){
                user.addDevice(device);
                userOperation.addDeviceIntoUserAndUpdate(device);
            } else {
                printSystemInformation("device with name < "+arguments.get(0)+" > not found in data base");
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
        return "device-usr-add [device name]";
    }
}
