package org.eugene.webapp.core.command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commands {
    private static List<String> list = new ArrayList<>(10);
    /**
     * На вход подается строка с командой
     * @param command
     * @return List с распарсенной строкой первый элемент имя секции, второй элемент имя команды
     * в секции, последующие элементы являются аргументами команд.
     */
    public static List<String> parseComString(String command){
        list.clear();
        Pattern pattern = Pattern.compile("\\s*(\\S+)");
        Matcher matcher = pattern.matcher(command);
        while(matcher.find()){
            list.add(matcher.group(1));
        }
        return list;
    }

    public static String getNameSection(){
        String nameSection = "";
        if(!list.isEmpty()){
            nameSection = list.get(0);
        }
        return nameSection;
    }

    public static String getNameCommand(){
        String nameCommand = "";
        if(list.size() > 1){
            nameCommand = list.get(1);
        }
        return nameCommand;
    }

    /**
     * На вход подается List с распарсеной строкой
     * @param
     * @return строковый массив аргументов команды.
     */
    public static List<String> getArguments(){
        List<String> arguments = new ArrayList<>(10);
        if(list.size() > 2){
            for (int i=2; i<list.size(); i++){
                arguments.add(list.get(i));
            }
        }
        return arguments;
    }
}
