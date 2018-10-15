package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.model.user.UserOperation;
import org.eugene.webapp.core.utils.ScriptCreator;

import java.io.File;
import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printCap;
import static org.eugene.webapp.core.utils.PrintInformation.printFormatInformation;
import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class ScriptFiles extends TotalUserCom implements Command {
    public ScriptFiles(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "script-files";
    }

    @Override
    public void perform() {
        if(ScriptCreator.getPathToScripts() == null){
            printSystemInformation("script files path is not specified");
            return;
        }
        File[] files = new File(ScriptCreator.getPathToScripts()).listFiles();
        if(files != null){
            if(files.length == 0){
                printSystemInformation("script files not found !!!");
                return;
            }
            printCap();
            for (File file : files) {
                printFormatInformation(file.getName());
            }
            printCap();
        } else {
            printSystemInformation("script files path is not correct");
        }
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        return arguments.size() == 0;
    }

    @Override
    public String getDescriptionCommand() {
        return "script-files []";
    }
}
