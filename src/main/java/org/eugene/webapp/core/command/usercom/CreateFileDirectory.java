package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.model.user.UserOperation;
import org.eugene.webapp.core.utils.ScriptCreator;

import java.io.File;
import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class CreateFileDirectory extends TotalUserCom implements Command {
    public CreateFileDirectory(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "directory-cr";
    }

    @Override
    public void perform() {
        if(ScriptCreator.getPathToScripts() == null){
            printSystemInformation("files path is not specified");
            return;
        }
        File file = new File(ScriptCreator.getPathToScripts()+File.separator+arguments.get(0));
        if(file.mkdir()){
            printSystemInformation("directory created");
        } else {
            printSystemInformation("error creating directory");
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
        return "directory-cr [directory name]";
    }
}
