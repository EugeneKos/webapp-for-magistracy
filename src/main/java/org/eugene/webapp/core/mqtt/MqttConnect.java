package org.eugene.webapp.core.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eugene.webapp.core.user.User;

import java.util.*;

import static org.eugene.webapp.core.printer.PrintInformation.*;

public class MqttConnect {
    private String mqttName; // Имя mqtt подключения
    private String broker; // = "tcp://iot.eclipse.org:1883"; // Сервер к которому я подключаюсь
    private String clientId; // = "JavaSample"; // Идентификатор клиента
    private String userName = null; // = Имя пользователя для подключения м авторизацией
    private String password = null; // = Пароль для подключения
    private boolean resolutionPrint = false;
    private Set<String> setSubscribes = new HashSet<>();
    private MemoryPersistence persistence = new MemoryPersistence();
    private MqttClient sampleClient;
    private Set<User> users = new HashSet<>();
    private Set<String> userNames = new HashSet<>();
    private Object monitor = new Object();

    public MqttConnect(String mqttName, String broker, String clientId){
        this.mqttName = mqttName;
        this.broker = broker;
        this.clientId = clientId;
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
            System.out.println("Password : "+ Arrays.toString(connOpts.getPassword()));

            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");

            autoSubscribe(setSubscribes);

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
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });


        } catch(MqttException me) {
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
            setSubscribes.add(topic);
            System.out.println("Subscribe to: "+topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void autoSubscribe(Set<String> setSubscribes){
        try {
            for (String topic : setSubscribes){
                sampleClient.subscribe(topic,2);
                System.out.println("Subscribe to: "+topic);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void unsubscribe(String[] topicFilter){
        try {
            sampleClient.unsubscribe(topicFilter);
            setSubscribes.removeAll(Arrays.asList(topicFilter));
            System.out.println("unsubscribe from: "+Arrays.toString(topicFilter));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishMessage(String content, String topic, int qos){
        System.out.println("Publishing message: "+content);
        try {
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        System.out.println("Message published");
    }

    public void closeMqtt(){
        try {
            sampleClient.disconnect();
            sampleClient.close();
            System.out.println("Disconnected");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void addUser(User user){
        synchronized (monitor){
            if(users.add(user)){
                userNames.add(user.getLogin());
                user.addMqttConnect(this);
                printSystemInformation("user < "+user.getLogin()+" > added in mqtt < "+ mqttName +" > data base");
            } else {
                printSystemInformation("user < "+user.getLogin()+" > already exist in mqtt < "+ mqttName +" > data base");
            }
        }
    }

    public void removeUser(User user){
        synchronized (monitor){
            if(users.remove(user) && userNames.remove(user.getLogin())){
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

    public Set<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(Set<String> userNames) {
        this.userNames = userNames;
    }

    public Set<String> getSetSubscribes() {
        return setSubscribes;
    }

    public void setSetSubscribes(Set<String> setSubscribes) {
        this.setSubscribes = setSubscribes;
    }

    private String isConnected(){
        if(sampleClient != null){
            return ""+sampleClient.isConnected();
        }
        return "unknown";
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

    public String toString(){
        return "------------------------------------------------"+"\n"+
                "[Name connection: "+ mqttName +"]"+"\n"+
                "[Broker: "+broker+"]"+"\n"+
                "[Client Id: "+clientId+"]"+"\n"+
                "[Resolution print: "+resolutionPrint+"]"+"\n"+
                "[is Connected: "+isConnected()+"]"+"\n"+
                "[Users: "+ userNames +"]"+"\n"+
                "[Subscribes: "+setSubscribes+"]"+"\n"+
                "------------------------------------------------";
    }

}
