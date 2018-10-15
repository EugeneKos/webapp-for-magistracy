package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.model.user.UserOperation;
import org.eugene.webapp.core.utils.ScriptCreator;

import java.io.File;
import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.*;

public class RemoveScriptFile extends TotalUserCom implements Command {
    public RemoveScriptFile(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "script-rm";
    }

    @Override
    public void perform() {
        if(ScriptCreator.getPathToScripts() == null){
            printSystemInformation("script files path is not specified");
            return;
        }
        File[] files = new File(ScriptCreator.getPathToScripts()).listFiles();
        if(files != null){
            for (File file : files){
                if(file.getName().equals(arguments.get(0))){
                    if(file.delete()){
                        printSystemInformation("script file deleted");
                    } else {
                        printSystemInformation("script file delete error");
                    }
                    return;
                }
            }
            printSystemInformation("script file with name < "+arguments.get(0)+" > not found");
        } else {
            printSystemInformation("script files path is not correct");
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
        return "script-rm [script file name]";
    }
}
