package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.model.user.UserOperation;
import org.eugene.webapp.core.utils.ScriptCreator;

import java.io.File;
import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class FolderChange extends TotalUserCom implements Command {
    public FolderChange(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "file";
    }

    @Override
    public void perform() {
        if(ScriptCreator.getPathToScripts() == null){
            printSystemInformation("files path is not specified");
            return;
        }
        if(arguments.get(0).equals("..")){
            File file = new File(ScriptCreator.getPathToScripts());
            String path = file.getParent();
            if(path != null){
                ScriptCreator.setPathToScripts(path);
                printSystemInformation("change path to scripts: "+path);
            } else {
                printSystemInformation("file or directory does not exist");
            }
        } else {
            String path = ScriptCreator.getPathToScripts()+File.separator+arguments.get(0);
            File file = new File(path);
            if(file.exists()){
                ScriptCreator.setPathToScripts(path);
                printSystemInformation("change path to scripts: "+path);
            } else {
                printSystemInformation("file or directory does not exist");
            }
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
        return "file [..] [folder name]";
    }
}
