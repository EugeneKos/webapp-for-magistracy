package org.eugene.webapp.core.mqtt;

import org.eugene.webapp.core.dao.MqttConnectDao;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static org.eugene.webapp.core.utils.PrintInformation.*;

@Component
public class MqttConnectOperations {
    private Set<MqttConnect> mqttConnects = new HashSet<>();
    private MqttConnect currentMqttConnect;
    private final UserOperation userOperation;
    private final MqttConnectDao mqttConnectDao;

    @Autowired
    public MqttConnectOperations(UserOperation userOperation, MqttConnectDao mqttConnectDao) {
        this.userOperation = userOperation;
        this.mqttConnectDao = mqttConnectDao;
    }

    public void addMqtt(MqttConnect mqttConnect){
        if(mqttConnects.add(mqttConnect)){
            mqttConnectDao.persist(mqttConnect);
            currentMqttConnect = mqttConnect;
        }

    }

    public void removeMqtt(){
        if(currentMqttConnect != null){
            mqttConnects.remove(currentMqttConnect);
            mqttConnectDao.removeByMqttName(currentMqttConnect.getMqttName());
            currentMqttConnect = null;
        }
    }

    public void selectMqtt(String mqttName){
        for (MqttConnect mqttConnect : mqttConnects) {
            if(mqttConnect.getMqttName().equals(mqttName)){
                currentMqttConnect = mqttConnect;
                printSystemInformation("selected mqtt connect with name < "+mqttName+" >");
                return;
            }
        }
        printSystemInformation("mqtt connect with name < "+mqttName+" > not found !!!");
    }

    public void startAllMqtt(){
        for (MqttConnect mqttConnect : mqttConnects){
            mqttConnect.startMqtt();
        }
    }

    public void disconnectAllMqtt(){
        for (MqttConnect mqttConnect : mqttConnects){
            mqttConnect.closeMqtt();
        }
    }

    public MqttConnect getMqttConnectByName(String mqttName){
        for (MqttConnect mqttConnect : mqttConnects) {
            if(mqttConnect.getMqttName().equals(mqttName)){
                return mqttConnect;
            }
        }
        return null;
    }

    public MqttConnect getCurrentMqttConnect(){
        return currentMqttConnect;
    }

    public void addUserIntoMqttConnectAndUpdate(String mqttName, User user){
        MqttConnect mqttConnect = mqttConnectDao.findByMqttName(mqttName);
        if(mqttConnect != null){
            mqttConnect.getUsers().add(user);
            mqttConnectDao.update(mqttConnect);
        }
    }

    public void addSubscribeIntoMqttConnectAndUpdate(String mqttName, String topic){
        MqttConnect mqttConnect = mqttConnectDao.findByMqttName(mqttName);
        if(mqttConnect != null){
            Subscribe subscribe = new Subscribe(topic);
            subscribe.setMqttConnect(mqttConnect);
            mqttConnect.getSubscribes().add(subscribe);
            mqttConnectDao.update(mqttConnect);
        }
    }

    public void loadMqttConnects(){
        mqttConnects = new HashSet<>(mqttConnectDao.findAll());
    }

    public void removeUserFromAllMqtt(User user){
        for (MqttConnect mqttConnect : mqttConnects){
            mqttConnect.removeUser(user);
        }
        for (MqttConnect mqttConnect : mqttConnectDao.findAll()){
            mqttConnect.getUsers().remove(user);
            mqttConnectDao.update(mqttConnect);
        }
    }

    public void removeUserFromLinkTable(String mqttName, User user){
        MqttConnect mqttConnect = mqttConnectDao.findByMqttName(mqttName);
        if(mqttConnect != null){
            mqttConnect.getUsers().remove(user);
            mqttConnectDao.update(mqttConnect);
        }

    }

    public void removeSubscribes(String[] topics){
        Set<Subscribe> subscribeSet = new HashSet<>();
        if(currentMqttConnect != null){
            for (String topic : topics){
                for (Subscribe subscribe : currentMqttConnect.getSubscribes()){
                    if(topic.equals(subscribe.getSubscribe())){
                        subscribeSet.add(subscribe);
                        mqttConnectDao.removeSubscribeById(subscribe.getId());
                    }
                }
            }
            currentMqttConnect.getSubscribes().removeAll(subscribeSet);
        }
    }

    public void printAllNameMqttConnections(){
        if(mqttConnects.isEmpty()){
            printSystemInformation("mqtt connections does not exists");
            return;
        }
        printCap();
        for (MqttConnect mqttConnect : mqttConnects) {
            printFormatInformation(mqttConnect.getMqttName());
        }
        printCap();
    }

    public UserOperation getUserOperation() {
        return userOperation;
    }
}
