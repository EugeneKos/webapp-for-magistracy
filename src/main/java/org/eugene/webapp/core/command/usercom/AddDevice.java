package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.parsing.ScriptCreator;
import org.eugene.webapp.core.parsing.device.Device;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;

import java.util.List;

import static org.eugene.webapp.core.printer.PrintInformation.printSystemInformation;

public class AddDevice extends TotalUserCom implements Command {
    public AddDevice(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "device-add";
    }

    @Override
    public void perform() {
        User user = userOperation.getCurrentUser();
        if(user != null){
            Device device = ScriptCreator.createDevice(user,arguments.get(0));
            if(device != null){
                userOperation.saveUsers();
                userOperation.addDeviceIntoDB(user.getLogin(),device);
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
        return "device-add [file script name]";
    }
}
