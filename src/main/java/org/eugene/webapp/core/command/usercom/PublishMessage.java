package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.model.user.User;
import org.eugene.webapp.core.model.user.UserOperation;

import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class PublishMessage extends TotalUserCom implements Command {
    public PublishMessage(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "publish";
    }

    @Override
    public void perform() {
        User user = userOperation.getCurrentUser();
        if(user != null){
            user.sendMessage(arguments.get(0),arguments.get(1),arguments.get(2));
        } else {
            printSystemInformation("user not found !!!");
        }
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        if(arguments.size() == 3){
            super.arguments = arguments;
            return true;
        }
        return false;
    }

    @Override
    public String getDescriptionCommand() {
        return "publish [mqttName, topic, content]";
    }
}
