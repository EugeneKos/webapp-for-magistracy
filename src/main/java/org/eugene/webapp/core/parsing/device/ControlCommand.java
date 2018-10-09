package org.eugene.webapp.core.parsing.device;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControlCommand {
    private String name;
    private String params;
    private String description;
    private String commandText;
    private final String changeValueToken = "--value";

    public ControlCommand(String name, String params, String description, String commandText) {
        this.name = name;
        this.params = params;
        this.description = description;
        this.commandText = commandText;
    }

    public String getName() {
        return name;
    }

    public String getParams() {
        return params;
    }

    public String getDescription() {
        return description;
    }

    public String getCommandText(){
        return commandText;
    }

    public void setChangesValue(String... params){
        if(params != null && params.length == getNumberChange()){
            replaceChangeValue(params);
        }
    }

    private int getNumberChange(){
        int num = 0;
        Pattern pattern = Pattern.compile(changeValueToken);
        Matcher matcher = pattern.matcher(commandText);
        while (matcher.find()){
            System.out.println(matcher.group());
            num++;
        }
        return num;
    }

    private void replaceChangeValue(String[] params){
        int index = 0;
        Pattern pattern = Pattern.compile(changeValueToken);
        Matcher matcher = pattern.matcher(commandText);
        while (matcher.find()){
            commandText = commandText.replaceFirst(matcher.group(),params[index]);
            index++;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ControlCommand that = (ControlCommand) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DeviceCom{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", commandText='" + commandText + '\'' +
                '}';
    }
}
