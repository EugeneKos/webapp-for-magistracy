package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.model.user.UserOperation;
import org.eugene.webapp.core.utils.ScriptCreator;

import java.io.File;
import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.printCap;
import static org.eugene.webapp.core.utils.PrintInformation.printFormatInformation;
import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

public class Files extends TotalUserCom implements Command {
    public Files(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "files";
    }

    @Override
    public void perform() {
        if(ScriptCreator.getPathToScripts() == null){
            printSystemInformation("files path is not specified");
            return;
        }
        File file = new File(ScriptCreator.getPathToScripts());
        if(!file.exists()){
            printSystemInformation("file or directory does not exist");
            return;
        }
        printSystemInformation("current path: "+ScriptCreator.getPathToScripts());
        File[] files = file.listFiles();
        if(files != null){
            if(files.length == 0){
                printSystemInformation("files not found !!!");
                return;
            }
            printCap();
            for (File file1 : files) {
                if(file1.listFiles() != null){
                    printFormatInformation(file1.getName()+" [d]");
                } else {
                    printFormatInformation(file1.getName()+" [f]");
                }
            }
            printCap();
        } else {
            printSystemInformation("the specified path is a file and not a directory");
        }
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        return arguments.size() == 0;
    }

    @Override
    public String getDescriptionCommand() {
        return "files []";
    }
}
