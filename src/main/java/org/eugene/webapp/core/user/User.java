package org.eugene.webapp.core.user;

import org.eugene.webapp.core.mqtt.MqttConnect;
import org.eugene.webapp.core.parsing.ConverterData;
import org.eugene.webapp.core.parsing.Data;

import java.util.*;

import static org.eugene.webapp.core.printer.PrintInformation.printSystemInformation;

public class User {
    private String login;
    private String password;
    private int bufferSize;
    private String role;
    private Set<MqttConnect> mqttConnects = new HashSet<>();
    private Map<String, ConverterData> converters = new HashMap<>();
    private LinkedList<Data> queueData = new LinkedList<>();
    private Map<String,Data> inputData = new HashMap<>();
    private Object monitor = new Object();
    private boolean resolutionPrint = false;
    private boolean isConverters = true;
    public static final String delimiter = "@";

    public User(String login, String password, String role, int bufferSize) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.bufferSize = bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        synchronized (monitor){
            if(bufferSize < this.bufferSize){
                for (int i=0; i<this.bufferSize-bufferSize; i++){
                    queueData.removeLast();
                }
            }
            this.bufferSize = bufferSize;
        }
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isMqttConnectsEmpty(){
        return mqttConnects.isEmpty();
    }

    public void setResolutionPrint(boolean resolutionPrint){
        this.resolutionPrint = resolutionPrint;
    }

    public void setIsConverters(boolean isConverters) {
        this.isConverters  = isConverters;
    }

    public void addConverter(ConverterData converterData) {
        String converterName = converterData.getMqttName()+delimiter+converterData.getTopicName();
        if(!converters.keySet().contains(converterName)){
            converters.put(converterName, converterData);
            printSystemInformation("converter added");
        } else {
            printSystemInformation("converter with name < " + converterName + " > already exist");
        }

    }

    public ConverterData removeConverter(String converterName) {
        ConverterData converterData = null;
        if (converters.keySet().contains(converterName)) {
            converterData = converters.remove(converterName);
            printSystemInformation("converter with name < " + converterName + " > deleted");
        } else {
            printSystemInformation("converter with name < " + converterName + " > does not exist");
        }
        return converterData;
    }

    public void addMqttConnect(MqttConnect mqttConnect) {
        mqttConnects.add(mqttConnect);
    }

    public void removeMqttConnect(MqttConnect mqttConnect) {
        mqttConnects.remove(mqttConnect);
    }

    public void addIntoQueue(String nameMqtt, String topic, String message) {
        ConverterData converterData = converters.get(nameMqtt+delimiter+topic);
        if (converterData != null) {
            synchronized (monitor) {
                if (queueData.size() >= bufferSize) {
                    queueData.removeLast();
                }
                queueData.addFirst(converterData.convert(message));
                if(resolutionPrint){
                    showQueue();
                }
                inputData.put(nameMqtt+delimiter+topic, converterData.convert(message));
            }
        }
    }

    public LinkedList<Data> getQueueData() {
        LinkedList<Data> queueDataInverse = new LinkedList<>();
        for (Data element : queueData){
            queueDataInverse.addFirst(element);
        }
        return queueDataInverse;
    }

    public Map<String, Data> getInputData(){
        return inputData;
    }

    public void sendMessage(String mqttName, String topic, String content) {
        for (MqttConnect mqttConnect : mqttConnects) {
            if (mqttConnect.getMqttName().equals(mqttName)) {
                mqttConnect.publishMessage(content, topic, 2);
            }
        }
    }

    private void showQueue(){
        int gapPage = 0;
        for (Data data : getQueueData()){
            if(isConverters){
                System.out.println(data.getDataWithConverters());
                gapPage = data.getDataWithConverters().length();
            } else {
                System.out.println(data.getDataWithoutConverters());
                gapPage = data.getDataWithoutConverters().length();
            }
        }
        for (int i=0; i<gapPage; i++){
            System.out.print("-");
        }
        System.out.println();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public Map<String, ConverterData> getConverters() {
        return converters;
    }

    public void setConverters(Map<String, ConverterData> converters) {
        this.converters = converters;
    }

    private Set<String> getMqttNames() {
        Set<String> namesMqtt = new HashSet<>();
        for (MqttConnect mqttConnect : mqttConnects) {
            namesMqtt.add(mqttConnect.getMqttName());
        }
        return namesMqtt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

    @Override
    public String toString() {
        return "------------------------------------------------"+"\n"+
                "[Login: "+login+"]"+"\n"+
                "[Password: "+password+"]"+"\n"+
                "[Role: "+role+"]"+"\n"+
                "[Buffer size: "+bufferSize+"]"+"\n"+
                "[Resolution print queue: "+resolutionPrint+"]"+"\n"+
                "[Converters included: "+isConverters+"]"+"\n"+
                "[Converters: "+converters.keySet()+"]"+"\n"+
                "[Mqtt connects: "+ getMqttNames()+"]"+"\n"+
                "------------------------------------------------";
    }
}
