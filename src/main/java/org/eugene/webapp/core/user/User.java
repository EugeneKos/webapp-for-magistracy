package org.eugene.webapp.core.user;

import org.eugene.webapp.core.device.Device;
import org.eugene.webapp.core.mqtt.MqttConnect;
import org.eugene.webapp.core.filter.DataFilter;
import org.eugene.webapp.core.filter.Data;
import org.eugene.webapp.core.utils.PrintInformation;

import javax.persistence.*;
import java.util.*;

import static org.eugene.webapp.core.utils.PrintInformation.printSystemInformation;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "login", unique = true)
    private String login;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    private String role;
    @Column(name = "buffer")
    private Integer bufferSize;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<MqttConnect> mqttConnects = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER/*, cascade = CascadeType.MERGE*/)
    @JoinTable(name = "Users_Filters",
            joinColumns = @JoinColumn(name = "User_ID"),
            inverseJoinColumns = @JoinColumn(name = "Filter_ID"))
    private Set<DataFilter> filters = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER/*, cascade = CascadeType.MERGE*/)
    @JoinTable(name = "Users_Devices",
            joinColumns = @JoinColumn(name = "User_ID"),
            inverseJoinColumns = @JoinColumn(name = "Device_ID"))
    private Set<Device> devices = new HashSet<>();

    @Transient
    private LinkedList<Data> queueData = new LinkedList<>();
    @Transient
    private Map<String,Data> inputData = new HashMap<>();
    @Transient
    private final Object monitor = new Object();
    @Transient
    private boolean resolutionPrint = false;
    @Transient
    private boolean isFilters = true;

    public User(String login, String password, String role, int bufferSize) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.bufferSize = bufferSize;
    }

    public User() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getBufferSize() {
        return bufferSize;
    }

    public String getLogin() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public Set<MqttConnect> getMqttConnects() {
        return mqttConnects;
    }

    public Set<DataFilter> getFilters() {
        return filters;
    }

    public void setFilters(Set<DataFilter> filters) {
        this.filters = filters;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
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

    //--------------------------------------filters-----------------------------------------------

    public void addFilter(DataFilter dataFilter) {
        String filterName = dataFilter.getName();
        if(filters.add(dataFilter)){
            printSystemInformation("filter added");
        } else {
            printSystemInformation("filter with name < " + filterName + " > already exist");
        }

    }

    public DataFilter removeFilter(String filterName) {
        for (DataFilter dataFilter : filters){
            if(dataFilter.getName().equals(filterName)){
                filters.remove(dataFilter);
                inputData.remove(filterName);
                printSystemInformation("filter with name < " + filterName + " > deleted");
                return dataFilter;
            }
        }
        printSystemInformation("filter with name < " + filterName + " > does not exist");
        return null;
    }

    public DataFilter getFilterByName(String filterName){
        for (DataFilter dataFilter : filters){
            if(dataFilter.getName().equals(filterName)){
                return dataFilter;
            }
        }
        return null;
    }

    //----------------------------------------------------------------------------------------

    public void addMqttConnect(MqttConnect mqttConnect) {
        mqttConnects.add(mqttConnect);
    }

    public void removeMqttConnect(MqttConnect mqttConnect) {
        mqttConnects.remove(mqttConnect);
    }

    public void addIntoQueue(String mqttName, String topic, String message) {
        for (DataFilter dataFilter : filters){
            if (dataFilter.getMqttName().equals(mqttName) && dataFilter.getTopicName().equals(topic)) {
                synchronized (monitor) {
                    if (queueData.size() >= bufferSize) {
                        queueData.removeLast();
                    }
                    queueData.addFirst(dataFilter.filter(message));
                    if(resolutionPrint){
                        showQueue();
                    }
                    inputData.put(dataFilter.getName(), dataFilter.filter(message));
                }
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

    //-----------------------------------------devices-----------------------------------------------

    public void addDevice(Device device){
        for (MqttConnect mqttConnect : mqttConnects){
            if(mqttConnect.getMqttName().equals(device.getMqttName())){
                if(devices.add(device)){
                    printSystemInformation("device with name < "+device.getName()+" > added");
                } else {
                    printSystemInformation("device with name < "+device.getName()+" > already exist");
                }
                return;
            }
        }
        printSystemInformation("mqtt connect with name < "+device.getMqttName()+" > not found");
        printSystemInformation("device not added");
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

    //--------------------------------------------------------------------------------------

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

    private Set<String> getMqttNames() {
        Set<String> mqttNames = new HashSet<>();
        for (MqttConnect mqttConnect : mqttConnects) {
            mqttNames.add(mqttConnect.getMqttName());
        }
        return mqttNames;
    }

    private Set<String> getFilterNames() {
        Set<String> filterNames = new HashSet<>();
        for (DataFilter dataFilter : filters) {
            filterNames.add(dataFilter.getName());
        }
        return filterNames;
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
        userInfo.add("[Filters: "+ getFilterNames()+"]");
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
                "[Filters: "+ getFilterNames()+"]"+System.lineSeparator()+
                "[Devices: "+ getDeviceNames()+"]"+System.lineSeparator()+
                "[Mqtt connects: "+ getMqttNames()+"]"+System.lineSeparator()+
                "------------------------------------------------";
    }
}
