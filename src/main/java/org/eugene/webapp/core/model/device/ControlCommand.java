package org.eugene.webapp.core.model.device;

import javax.persistence.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name = "control_commands")
public class ControlCommand {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "params")
    private String params;
    @Column(name = "description")
    private String description;
    @Column(name = "command_text")
    private String commandText;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Device device;
    @Transient
    private final String changeValueToken = "--value";

    public ControlCommand(String name, String params, String description, String commandText) {
        this.name = name;
        this.params = params;
        this.description = description;
        this.commandText = commandText;
    }

    public ControlCommand() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
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
