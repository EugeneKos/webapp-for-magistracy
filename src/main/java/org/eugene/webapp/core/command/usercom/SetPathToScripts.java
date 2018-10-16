package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.utils.ScriptCreator;
import org.eugene.webapp.core.model.user.UserOperation;

import java.io.File;
import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.*;

public class SetPathToScripts extends TotalUserCom implements Command {
    public SetPathToScripts(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "scripts-setpath";
    }

    @Override
    public void perform() {
        File file = new File(arguments.get(0));
        if(file.exists()){
            ScriptCreator.setPathToScripts(arguments.get(0));
            printSystemInformation("change path to scripts: "+arguments.get(0));
        } else {
            printSystemInformation("file or directory does not exist");
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
        return "scripts-setpath [path to scripts]";
    }
}
