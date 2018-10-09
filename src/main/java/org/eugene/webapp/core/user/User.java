package org.eugene.webapp.core.user;

import org.eugene.webapp.core.parsing.device.Device;
import org.eugene.webapp.core.mqtt.MqttConnect;
import org.eugene.webapp.core.parsing.filter.DataFilter;
import org.eugene.webapp.core.parsing.filter.Data;
import org.eugene.webapp.core.printer.PrintInformation;

import java.util.*;

import static org.eugene.webapp.core.printer.PrintInformation.printSystemInformation;

public class User {
    private String login;
    private String password;
    private int bufferSize;
    private String role;
    private Set<MqttConnect> mqttConnects = new HashSet<>();
    private Map<String, DataFilter> filters = new HashMap<>();
    private LinkedList<Data> queueData = new LinkedList<>();
    private Map<String,Data> inputData = new HashMap<>();
    private Set<Device> devices = new HashSet<>();
    private final Object monitor = new Object();
    private boolean resolutionPrint = false;
    private boolean isFilters = true;
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

    public void setIsFilters(boolean isFilters) {
        this.isFilters = isFilters;
    }

    public void addFilter(DataFilter dataFilter) {
        String filterName = dataFilter.getMqttName()+delimiter+ dataFilter.getTopicName();
        if(!filters.keySet().contains(filterName)){
            filters.put(filterName, dataFilter);
            printSystemInformation("filter added");
        } else {
            printSystemInformation("filter with name < " + filterName + " > already exist");
        }

    }

    public DataFilter removeFilter(String filterName) {
        DataFilter dataFilter = null;
        if (filters.keySet().contains(filterName)) {
            dataFilter = filters.remove(filterName);
            inputData.remove(filterName);
            printSystemInformation("filter with name < " + filterName + " > deleted");
        } else {
            printSystemInformation("filter with name < " + filterName + " > does not exist");
        }
        return dataFilter;
    }

    public void addMqttConnect(MqttConnect mqttConnect) {
        mqttConnects.add(mqttConnect);
    }

    public void removeMqttConnect(MqttConnect mqttConnect) {
        mqttConnects.remove(mqttConnect);
    }

    public void addIntoQueue(String nameMqtt, String topic, String message) {
        DataFilter dataFilter = filters.get(nameMqtt+delimiter+topic);
        if (dataFilter != null) {
            synchronized (monitor) {
                if (queueData.size() >= bufferSize) {
                    queueData.removeLast();
                }
                queueData.addFirst(dataFilter.filter(message));
                if(resolutionPrint){
                    showQueue();
                }
                inputData.put(nameMqtt+delimiter+topic, dataFilter.filter(message));
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

    public Map<String, String> getStatusMqttConnects(){
        Map<String, String> statusMqttConnects = new HashMap<>();
        for (MqttConnect mqttConnect : mqttConnects){
            statusMqttConnects.put(mqttConnect.getMqttName(),mqttConnect.isConnected());
        }
        return statusMqttConnects;
    }

    public Device addDevice(String deviceName, String deviceDescription, String mqttName, String topic){
        for (MqttConnect mqttConnect : mqttConnects){
            if(mqttConnect.getMqttName().equals(mqttName)){
                Device device = new Device(deviceName,deviceDescription,mqttName,topic);
                if(devices.add(device)){
                    printSystemInformation("device with name < "+deviceName+" > added");
                    return device;
                } else {
                    printSystemInformation("device with name < "+deviceName+" > already exist");
                }
            }
        }
        printSystemInformation("mqtt connect with name < "+mqttName+" > not found");
        printSystemInformation("device not added");
        return null;
    }

    public Device removeDevice(String deviceName){
        for (Device device : devices){
            if(device.getName().equals(deviceName)){
                devices.remove(device);
                printSystemInformation("device with name < "+deviceName+" > removed");
                return device;
            }
        }
        printSystemInformation("device with name < "+deviceName+" > not found");
        return null;
    }

    public Device getDeviceByName(String deviceName){
        for (Device device : devices){
            if(device.getName().equals(deviceName)){
                return device;
            }
        }
        return null;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    public void sendMessage(String deviceName, String commandName, String... params) {
        for (Device device : devices){
            if(device.getName().equals(deviceName)){
                String commandTextForDevice = device.getCommandTextForDevice(commandName,params);
                if(commandTextForDevice != null){
                    sendMessage(device.getMqttName(),device.getTopic(),commandTextForDevice);
                }
            }
        }
    }

    public void sendMessage(String mqttName, String topic, String content) {
        for (MqttConnect mqttConnect : mqttConnects) {
            if (mqttConnect.getMqttName().equals(mqttName)) {
                if(mqttConnect.isConnected().equals("true")){
                    mqttConnect.publishMessage(content, topic, 2);
                }
            }
        }
    }

    private void showQueue(){
        int gapPage = 0;
        for (Data data : getQueueData()){
            if(isFilters){
                System.out.println(data.getDataWithFilters());
                PrintInformation.addMessageIntoOperationBuffer(data.getDataWithFilters());
                gapPage = data.getDataWithFilters().length();
            } else {
                System.out.println(data.getDataWithoutFilters());
                PrintInformation.addMessageIntoOperationBuffer(data.getDataWithoutFilters());
                gapPage = data.getDataWithoutFilters().length();
            }
        }
        for (int i=0; i<gapPage; i++){
            System.out.print("-");
        }
        System.out.println();
        PrintInformation.addMessageIntoOperationBuffer("-----------------------------------------------");
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

    public Map<String, DataFilter> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, DataFilter> filters) {
        this.filters = filters;
    }

    private Set<String> getMqttNames() {
        Set<String> mqttNames = new HashSet<>();
        for (MqttConnect mqttConnect : mqttConnects) {
            mqttNames.add(mqttConnect.getMqttName());
        }
        return mqttNames;
    }

    private Set<String> getDeviceNames() {
        Set<String> deviceNames = new HashSet<>();
        for (Device device : devices) {
            deviceNames.add(device.getName());
        }
        return deviceNames;
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

    public List<String> getUserInfo(){
        List<String> userInfo = new ArrayList<>();
        userInfo.add("------------------------------------------------");
        userInfo.add("[Login: "+login+"]");
        userInfo.add("[Password: "+password+"]");
        userInfo.add("[Role: "+role+"]");
        userInfo.add("[Buffer size: "+bufferSize+"]");
        userInfo.add("[Resolution print queue: "+resolutionPrint+"]");
        userInfo.add("[Filters included: "+ isFilters +"]");
        userInfo.add("[Filters: "+ filters.keySet()+"]");
        userInfo.add("[Devices: "+ getDeviceNames()+"]");
        userInfo.add("[Mqtt connects: "+ getMqttNames()+"]");
        userInfo.add("------------------------------------------------");
        return userInfo;
    }

    @Override
    public String toString() {
        return "------------------------------------------------"+System.lineSeparator()+
                "[Login: "+login+"]"+System.lineSeparator()+
                "[Password: "+password+"]"+System.lineSeparator()+
                "[Role: "+role+"]"+System.lineSeparator()+
                "[Buffer size: "+bufferSize+"]"+System.lineSeparator()+
                "[Resolution print queue: "+resolutionPrint+"]"+System.lineSeparator()+
                "[Filters included: "+ isFilters +"]"+System.lineSeparator()+
                "[Filters: "+ filters.keySet()+"]"+System.lineSeparator()+
                "[Devices: "+ getDeviceNames()+"]"+System.lineSeparator()+
                "[Mqtt connects: "+ getMqttNames()+"]"+System.lineSeparator()+
                "------------------------------------------------";
    }
}
