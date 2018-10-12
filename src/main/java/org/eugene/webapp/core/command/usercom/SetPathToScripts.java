package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.utils.ScriptCreator;
import org.eugene.webapp.core.user.UserOperation;

import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.*;

public class SetPathToScripts extends TotalUserCom implements Command {
    public SetPathToScripts(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "scripts-path";
    }

    @Override
    public void perform() {
        ScriptCreator.setPathToScripts(arguments.get(0));
        printSystemInformation("change path to scripts: "+arguments.get(0));
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
        return "scripts-path [path to scripts]";
    }
}
