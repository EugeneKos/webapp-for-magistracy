package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;

import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class SetRole extends TotalUserCom implements Command {
    public SetRole(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "role";
    }

    @Override
    public void perform() {
        User user = userOperation.getCurrentUser();
        if(user != null){
            user.setRole(arguments.get(0));
            userOperation.updateUserInDB();
            printSystemInformation("role changed");
        } else {
            printSystemInformation("current user not found !!!");
        }
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        if(arguments.size() == 1){
            super.arguments = arguments;
            return isCorrectRole();
        }
        return false;
    }

    private boolean isCorrectRole(){
        return arguments.get(0).equals("ROLE_ADMIN") || arguments.get(0).equals("ROLE_USER");
    }

    @Override
    public String getDescriptionCommand() {
        return "role [role {ROLE_ADMIN / ROLE_USER} ]";
    }
}
