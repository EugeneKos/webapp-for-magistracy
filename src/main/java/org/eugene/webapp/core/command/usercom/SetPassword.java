package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;

import java.util.List;

import static org.eugene.webapp.core.printer.PrintInformation.printSystemInformation;

public class SetPassword extends TotalUserCom implements Command {
    public SetPassword(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "password";
    }

    @Override
    public void perform() {
        User user = userOperation.getCurrentUser();
        if(user != null){
            if(arguments.get(0).equals(user.getPassword())){
                user.setPassword(arguments.get(1));
                //userOperation.easyUpdateIntoDB();
                userOperation.updateUser();
                printSystemInformation("password changed");
            } else {
                printSystemInformation("old password is incorrect");
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
        return "password [oldPassword, newPassword]";
    }
}
