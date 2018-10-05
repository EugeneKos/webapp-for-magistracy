package org.eugene.webapp.core.device;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControlCommand {
    private String name;
    private String description;
    private String commandLine;
    private final String changeValueToken = "--value";

    public ControlCommand(String name, String description, String commandLine) {
        this.name = name;
        this.description = description;
        this.commandLine = commandLine;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCommandLine(){
        return commandLine;
    }

    public void setChangesValue(String... params){
        if(params != null && params.length == getNumberChange()){
            replaceChangeValue(params);
        }
    }

    private int getNumberChange(){
        int num = 0;
        Pattern pattern = Pattern.compile(changeValueToken);
        Matcher matcher = pattern.matcher(commandLine);
        while (matcher.find()){
            System.out.println(matcher.group());
            num++;
        }
        return num;
    }

    private void replaceChangeValue(String[] params){
        int index = 0;
        Pattern pattern = Pattern.compile(changeValueToken);
        Matcher matcher = pattern.matcher(commandLine);
        while (matcher.find()){
            commandLine = commandLine.replaceFirst(matcher.group(),params[index]);
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
                ", commandLine='" + commandLine + '\'' +
                '}';
    }

    public static void main(String[] args) {
        ControlCommand c = new ControlCommand("test","testDescription",
                "dhfhsdk=   --valuesss; dsvsdsd=off; sdcvaluesdc= --value  ;");
        c.setChangesValue("ss","2345");
        System.out.println(c.getCommandLine());
        String s = "23 456   dfdsa";
        System.out.println(Arrays.toString(s.split("\\s+")));
    }
}
