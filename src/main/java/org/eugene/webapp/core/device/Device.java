package org.eugene.webapp.core.device;

import org.eugene.webapp.core.mqtt.MqttConnect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Device {
    private String name;
    private String description;
    private MqttConnect mqttConnect;
    private String topic;
    private Set<ControlCommand> commands = new HashSet<>();

    public Device(String name, String description, MqttConnect mqttConnect, String topic) {
        this.name = name;
        this.description = description;
        this.mqttConnect = mqttConnect;
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<ControlCommand> getCommands() {
        return commands;
    }

    public void addControlCommand(String commandName, String description, String commandLine){
        commands.add(new ControlCommand(commandName,description,commandLine));
    }

    public void removeControlCommand(String commandName){
        for (ControlCommand controlCommand : commands){
            if(controlCommand.getName().equals(commandName)){
                commands.remove(controlCommand);
                return;
            }
        }
    }

    public void sendMessage(String commandName, String... params){
        for (ControlCommand controlCommand : commands){
            if(controlCommand.getName().equals(commandName)){
                controlCommand.setChangesValue(params);
                mqttConnect.publishMessage(controlCommand.getCommandLine(),topic,2);
            }
        }
    }

    private String getCommandsInfo(){
        StringBuilder stringBuilder = new StringBuilder();
        for (ControlCommand controlCommand : commands){
            stringBuilder.append(controlCommand.toString()).append("\n");
        }
        return stringBuilder.toString();
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
        List<String> userInfo = new ArrayList<>();
        userInfo.add("------------------------------------------------" + "\n");
        userInfo.add("[Name: "+name+"]"+"\n");
        userInfo.add("[Description: "+description+"]"+"\n");
        userInfo.add(getCommandsInfo());
        userInfo.add("------------------------------------------------");
        return userInfo;
    }

    @Override
    public String toString() {
        return "------------------------------------------------"+"\n"+
                "[Name: "+name+"]"+"\n"+
                "[Description: "+description+"]"+"\n"+
                getCommandsInfo()+
                "------------------------------------------------";
    }
}
