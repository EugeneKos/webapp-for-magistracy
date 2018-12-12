package org.eugene.webapp.core.command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Commands {
    private Commands(){}

    public static CommandContent parseComString(String command){
        List<String> list = new ArrayList<>(10);
        CommandContent commandContent = new CommandContent();
        Pattern pattern = Pattern.compile("\\s*(\\S+)");
        Matcher matcher = pattern.matcher(command);
        while(matcher.find()){
            list.add(matcher.group(1));
        }
        setSectionName(commandContent, list);
        setCommandName(commandContent, list);
        setArguments(commandContent, list);
        return commandContent;
    }

    private static void setSectionName(CommandContent commandContent, List<String> list){
        String sectionName = "";
        if(!list.isEmpty()){
            sectionName = list.get(0);
        }
        commandContent.setSectionName(sectionName);
    }

    private static void setCommandName(CommandContent commandContent, List<String> list){
        String commandName = "";
        if(list.size() > 1){
            commandName = list.get(1);
        }
        commandContent.setCommandName(commandName);
    }

    private static void setArguments(CommandContent commandContent, List<String> list){
        List<String> arguments = new ArrayList<>(10);
        if(list.size() > 2){
            for (int i=2; i<list.size(); i++){
                arguments.add(list.get(i));
            }
        }
        commandContent.setArguments(arguments);
    }
}
