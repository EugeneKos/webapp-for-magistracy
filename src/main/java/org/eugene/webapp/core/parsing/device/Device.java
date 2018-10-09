package org.eugene.webapp.core.parsing.device;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Device {
    private String name;
    private String description;
    private String mqttName;
    private String topic;
    private Set<ControlCommand> commands = new HashSet<>();

    public Device(String name, String description, String mqttName, String topic) {
        this.name = name;
        this.description = description;
        this.mqttName = mqttName;
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getMqttName() {
        return mqttName;
    }

    public String getTopic() {
        return topic;
    }

    public Set<ControlCommand> getCommands() {
        return commands;
    }

    public void setCommands(Set<ControlCommand> commands) {
        this.commands = commands;
    }

    public void addControlCommand(String commandName, String params, String description, String commandText){
        commands.add(new ControlCommand(commandName,params,description,commandText));
    }

    public void removeControlCommand(String commandName){
        for (ControlCommand controlCommand : commands){
            if(controlCommand.getName().equals(commandName)){
                commands.remove(controlCommand);
                return;
            }
        }
    }

    public String getCommandTextForDevice(String commandName, String... params){
        for (ControlCommand controlCommand : commands){
            if(controlCommand.getName().equals(commandName)){
                controlCommand.setChangesValue(params);
                return controlCommand.getCommandText();
            }
        }
        return null;
    }

    private String getCommandsInfo(){
        StringBuilder stringBuilder = new StringBuilder();
        for (ControlCommand controlCommand : commands){
            stringBuilder.append("[Com Structure: [").append(controlCommand.getName()).append(controlCommand.getParams()).append("] Description: ").append(controlCommand.getDescription()).append("]").append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }

    private List<String> addCommandsInfoIntoList(){
        List<String> deviceInfo = new ArrayList<>();
        for (ControlCommand controlCommand : commands){
            deviceInfo.add("[Com Structure: ["+controlCommand.getName()+controlCommand.getParams()+"] Description: "+controlCommand.getDescription()+"]");
        }
        return deviceInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Device device = (Device) o;

        return name != null ? name.equals(device.name) : device.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public List<String> getDeviceInfo() {
        List<String> deviceInfo = new ArrayList<>();
        deviceInfo.add("------------------------------------------------");
        deviceInfo.add("[Name: "+name+"]");
        deviceInfo.add("[Description: "+description+"]");
        deviceInfo.add("[Mqtt Broker: "+mqttName+"]");
        deviceInfo.addAll(addCommandsInfoIntoList());
        deviceInfo.add("------------------------------------------------");
        return deviceInfo;
    }

    @Override
    public String toString() {
        return "------------------------------------------------"+System.lineSeparator()+
                "[Name: "+name+"]"+System.lineSeparator()+
                "[Description: "+description+"]"+System.lineSeparator()+
                getCommandsInfo()+
                "------------------------------------------------";
    }
}
