package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.model.user.UserOperation;
import org.eugene.webapp.core.utils.ScriptCreator;

import java.io.File;
import java.util.List;

import static org.eugene.webapp.core.utils.PrintInformation.*;

public class RemoveFile extends TotalUserCom implements Command {
    public RemoveFile(UserOperation userOperation) {
        super(userOperation);
    }

    @Override
    public String getName() {
        return "file-rm";
    }

    @Override
    public void perform() {
        if(ScriptCreator.getPathToScripts() == null){
            printSystemInformation("files path is not specified");
            return;
        }
        File file = new File(ScriptCreator.getPathToScripts());
        if(!file.exists()){
            printSystemInformation("current file or directory does not exist");
            return;
        }
        File[] files = new File(ScriptCreator.getPathToScripts()).listFiles();
        if(files != null){
            for (File file1 : files){
                if(file1.getName().equals(arguments.get(0))){
                    if(file1.delete()){
                        printSystemInformation("file "+file1.getName()+" deleted");
                    } else {
                        deleteAllFiles(file1);
                    }
                    return;
                }
            }
            printSystemInformation("file with name < "+arguments.get(0)+" > not found");
        } else {
            printSystemInformation("the specified path is a file and not a directory");
        }
    }

    private void deleteAllFiles(File file){
        if(file.listFiles() != null){
            for (File f : file.listFiles()){
                if(!f.delete()){
                    deleteAllFiles(f);
                } else {
                    printSystemInformation("file "+f.getName()+" deleted");
                }
            }
            if(!file.delete()){
                printSystemInformation("error deleting file "+file.getName());
            } else {
                printSystemInformation("file "+file.getName()+" deleted");
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
        return "file-rm [file name]";
    }
}
