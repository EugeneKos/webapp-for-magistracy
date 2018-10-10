package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;
import org.eugene.webapp.core.command.Command;

import java.util.List;

import static org.eugene.webapp.core.printer.PrintInformation.printSystemInformation;

public class CreateUser extends TotalUserCom implements Command {
    public CreateUser(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public void perform() {
        try {
            userOperation.addUser(new User(arguments.get(0),arguments.get(1),arguments.get(2),Integer.parseInt(arguments.get(3))));
        } catch (NumberFormatException e){
            printSystemInformation("Input buffer size error!");
        }
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        if(arguments.size() == 4){
            super.arguments = arguments;
            return isCorrectRole();
        }
        return false;
    }

    private boolean isCorrectRole(){
        return arguments.get(2).equals("ROLE_ADMIN") || arguments.get(2).equals("ROLE_USER");
    }

    @Override
    public String getDescriptionCommand() {
        return "create [userName, password, role, bufferSize]";
    }
}
