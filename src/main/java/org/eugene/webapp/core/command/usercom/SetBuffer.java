package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;

import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class SetBuffer extends TotalUserCom implements Command {
    public SetBuffer(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "buffer";
    }

    @Override
    public void perform() {
        User user = userOperation.getCurrentUser();
        if(user != null){
            try {
                user.setBufferSize(Integer.parseInt(arguments.get(0)));
                userOperation.updateUserInDB();
                printSystemInformation("buffer size changed");
            } catch (NumberFormatException e){
                printSystemInformation("Input buffer size error!");
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
        return "buffer [size]";
    }
}
