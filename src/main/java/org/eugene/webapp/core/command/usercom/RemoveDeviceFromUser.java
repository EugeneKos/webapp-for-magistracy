package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.model.device.Device;
import org.eugene.webapp.core.model.user.User;
import org.eugene.webapp.core.model.user.UserOperation;

import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class RemoveDeviceFromUser extends TotalUserCom implements Command {
    public RemoveDeviceFromUser(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "device-usr-rm";
    }

    @Override
    public void perform() {
        User user = userOperation.getCurrentUser();
        if(user != null){
            Device device = user.removeDevice(arguments.get(0));
            if(device != null){
                userOperation.removeDeviceFromLinkTable(user.getLogin(),device);
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
        return "device-usr-rm [deviceName]";
    }
}
