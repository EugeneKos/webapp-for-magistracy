package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.device.Device;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;

import java.util.List;

import static org.eugene.webapp.core.printer.PrintInformation.printSystemInformation;

public class RemoveCommandFromDevice extends TotalUserCom implements Command {
    public RemoveCommandFromDevice(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "device-com-rm";
    }

    @Override
    public void perform() {
        User user = userOperation.getCurrentUser();
        if(user != null){
            Device device = user.getDeviceByName(arguments.get(0));
            if(device != null){
                device.removeControlCommand(arguments.get(1));
            } else {
                printSystemInformation("device with name < "+arguments.get(0)+" > not found");
            }
        } else {
            printSystemInformation("user not found !!!");
        }
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        if(arguments.size() == 2){
            super.arguments = arguments;
            return true;
        }
        return false;
    }

    @Override
    public String getDescriptionCommand() {
        return "device-com-rm [deviceName, commandName]";
    }
}
