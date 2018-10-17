package org.eugene.webapp.core.model.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eugene.webapp.core.utils.PrintInformation;
import org.eugene.webapp.core.model.user.User;

import javax.persistence.*;
import java.util.*;

import static org.eugene.webapp.core.utils.PrintInformation.*;

@Entity
@Table(name = "mqtt_connects")
public class MqttConnect {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "mqtt_name", unique = true)
    private String mqttName; // Имя mqtt подключения
    @Column(name = "broker_name")
    private String broker; // = "tcp://iot.eclipse.org:1883"; // Сервер к которому я подключаюсь
    @Column(name = "clientId")
    private String clientId; // = "JavaSample"; // Идентификатор клиента

    @Transient
    private Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "MqttConnects_UserNames",
            joinColumns = @JoinColumn(name = "MqttConnect_ID"),
            inverseJoinColumns = @JoinColumn(name = "UserName_ID"))
    private Set<UserNameEntity> userNameEntities = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "mqttConnect")
    private Set<Subscribe> subscribes = new HashSet<>();

    @Transient
    private MemoryPersistence persistence = new MemoryPersistence();
    @Transient
    private MqttClient sampleClient;
    @Transient
    private String userName = null; // = Имя пользователя для подключения м авторизацией
    @Transient
    private String password = null; // = Пароль для подключения
    @Transient
    private boolean resolutionPrint = false;
    @Transient
    private final Object monitor = new Object();

    public MqttConnect(String mqttName, String broker, String clientId){
        this.mqttName = mqttName;
        this.broker = broker;
        this.clientId = clientId;
    }

    public MqttConnect() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMqttName(){
        return mqttName;
    }

    public void setResolutionPrint(boolean resolutionPrint){
        this.resolutionPrint = resolutionPrint;
    }

    public void startMqtt(){
        try {
            sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            //Для подключения с именем пользователя и паролем.
            connOpts = setLogPass(connOpts,userName,password);
            System.out.println("User name: "+connOpts.getUserName());
            addMessageIntoBuffer("User name: "+connOpts.getUserName());
            System.out.println("Password : "+ Arrays.toString(connOpts.getPassword()));
            addMessageIntoBuffer("Password : "+ Arrays.toString(connOpts.getPassword()));

            System.out.println("Connecting to broker: "+broker);
            addMessageIntoBuffer("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            addMessageIntoBuffer("Connected");

            autoSubscribe();

            sampleClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    cause.printStackTrace();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String answerMqtt = new String(message.getPayload());
                    dispatch(topic,answerMqtt);
                    if(resolutionPrint){
                        System.out.println(topic +": "+ answerMqtt);
                        PrintInformation.addMessageIntoOperationBuffer(topic +": "+ answerMqtt);
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });


        } catch(MqttException me) {
            printErrorMqttInform();
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }

    private MqttConnectOptions setLogPass(MqttConnectOptions mqttConnectOptions, String userName, String password){
        if(userName != null && password != null){
            mqttConnectOptions.setUserName(userName);
            mqttConnectOptions.setPassword(password.toCharArray());
            return mqttConnectOptions;
        }
        return mqttConnectOptions;
    }

    public void setUserNameAndPass(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public void subscribe(String topic, int qos){
        try {
            sampleClient.subscribe(topic,qos);
            Subscribe subscribe = new Subscribe(topic);
            subscribe.setMqttConnect(this);
            subscribes.add(subscribe);
            System.out.println("Subscribe to: "+topic);
            addMessageIntoBuffer("Subscribe to: "+topic);
        } catch (MqttException e) {
            e.printStackTrace();
            printErrorMqttInform();
        }
    }

    private void autoSubscribe(){
        try {
            for (Subscribe subscribe : subscribes){
                String topic = subscribe.getSubscribe();
                sampleClient.subscribe(topic,2);
                System.out.println("Subscribe to: "+topic);
                addMessageIntoBuffer("Subscribe to: "+topic);
            }
        } catch (MqttException e) {
            e.printStackTrace();
            printErrorMqttInform();
        }
    }

    public void unsubscribe(String[] topicFilter){
        try {
            sampleClient.unsubscribe(topicFilter);
            System.out.println("unsubscribe from: "+Arrays.toString(topicFilter));
            addMessageIntoBuffer("unsubscribe from: "+Arrays.toString(topicFilter));
        } catch (MqttException e) {
            e.printStackTrace();
            printErrorMqttInform();
        }
    }

    public void publishMessage(String content, String topic, int qos){
        System.out.println("Publishing message: "+content);
        addMessageIntoOperationBuffer("Publishing message: "+content);
        try {
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
            printErrorMqttInform();
        }
        System.out.println("Message published");
        addMessageIntoOperationBuffer("Message published");
    }

    public void closeMqtt(){
        try {
            sampleClient.disconnect();
            sampleClient.close();
            System.out.println("Disconnected");
            addMessageIntoBuffer("Disconnected");
        } catch (MqttException e) {
            e.printStackTrace();
            printErrorMqttInform();
        }
    }

    public void addUser(User user){
        synchronized (monitor){
            if(users.add(user)){
                user.addMqttConnect(this);
                printSystemInformation("user < "+user.getLogin()+" > added in mqtt < "+ mqttName +" > data base");
            } else {
                printSystemInformation("user < "+user.getLogin()+" > already exist in mqtt < "+ mqttName +" > data base");
            }
        }
    }

    public void removeUser(User user){
        synchronized (monitor){
            if(users.remove(user)){
                user.removeMqttConnect(this);
                printSystemInformation("user < "+user.getLogin()+" > deleted from mqtt < "+ mqttName +" > data base");
            } else {
                printSystemInformation("user < "+user.getLogin()+" > not found in mqtt < "+ mqttName +" > data base");
            }
        }
    }

    private void dispatch(String topic, String message){
        synchronized (monitor){
            for (User user : users){
                user.addIntoQueue(mqttName,topic,message);
            }
        }
    }

    public String getBroker() {
        return broker;
    }

    public String getClientId() {
        return clientId;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Subscribe> getSubscribes() {
        return subscribes;
    }

    public void setSubscribes(Set<Subscribe> subscribes) {
        this.subscribes = subscribes;
    }

    public void setUserNameEntities(Set<UserNameEntity> userNameEntities) {
        this.userNameEntities = userNameEntities;
    }

    public Set<UserNameEntity> getUserNameEntities() {
        return userNameEntities;
    }

    public String isConnected(){
        if(sampleClient != null){
            return ""+sampleClient.isConnected();
        }
        return "unknown";
    }

    private void printErrorMqttInform(){
        printSystemInformation("mqtt connection < "+mqttName+" > broke the link");
        printSystemInformation("check internet connection");
        printSystemInformation("it is recommended to execute commands \"disconnect\" and then \"start\" on this mqtt connection");
    }

    private Set<String> getUserNames(){
        Set<String> userNames = new HashSet<>();
        for (User user : users){
            userNames.add(user.getLogin());
        }
        return userNames;
    }

    private Set<String> getSubscribeTopics(){
        Set<String> subscribeTopics = new HashSet<>();
        for (Subscribe subscribe : subscribes){
            subscribeTopics.add(subscribe.getSubscribe());
        }
        return subscribeTopics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MqttConnect that = (MqttConnect) o;
        return Objects.equals(mqttName, that.mqttName) &&
                Objects.equals(broker, that.broker) &&
                Objects.equals(clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mqttName, broker, clientId);
    }

    public List<String> getMqttInfo(){
        List<String> mqttInfo = new ArrayList<>();
        mqttInfo.add("------------------------------------------------");
        mqttInfo.add("[Name connection: "+ mqttName +"]");
        mqttInfo.add("[Broker: "+broker+"]");
        mqttInfo.add("[Client Id: "+clientId+"]");
        mqttInfo.add("[Resolution print: "+resolutionPrint+"]");
        mqttInfo.add("[is Connected: "+isConnected()+"]");
        mqttInfo.add("[Users: "+ getUserNames() +"]");
        mqttInfo.add("[Subscribes: "+ getSubscribeTopics()+"]");
        mqttInfo.add("------------------------------------------------");
        return mqttInfo;
    }

    public String toString(){
        return "------------------------------------------------"+System.lineSeparator()+
                "[Name connection: "+ mqttName +"]"+System.lineSeparator()+
                "[Broker: "+broker+"]"+System.lineSeparator()+
                "[Client Id: "+clientId+"]"+System.lineSeparator()+
                "[Resolution print: "+resolutionPrint+"]"+System.lineSeparator()+
                "[is Connected: "+isConnected()+"]"+System.lineSeparator()+
                "[Users: "+ getUserNames() +"]"+System.lineSeparator()+
                "[Subscribes: "+getSubscribeTopics()+"]"+System.lineSeparator()+
                "------------------------------------------------";
    }

}
