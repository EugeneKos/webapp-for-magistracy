package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.user.UserOperation;
import org.eugene.webapp.core.utils.ScriptCreator;

import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class PathToScriptsInfo extends TotalUserCom implements Command {
    public PathToScriptsInfo(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "scripts-path-info";
    }

    @Override
    public void perform() {
        String pathToScripts = ScriptCreator.getPathToScripts();
        printSystemInformation("path to scripts: "+pathToScripts);
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        return arguments.size() == 0;
    }

    @Override
    public String getDescriptionCommand() {
        return "scripts-path-info []";
    }
}
