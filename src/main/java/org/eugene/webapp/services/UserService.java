package org.eugene.webapp.services;

import org.eugene.webapp.core.parsing.device.Device;
import org.eugene.webapp.core.parsing.filter.Data;
import org.eugene.webapp.core.user.UserOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

@Service
public class UserService {
    private final UserOperation userOperation;

    @Autowired
    public UserService(UserOperation userOperation) {
        this.userOperation = userOperation;
    }

    public LinkedList<Data> qetQueueData(String userLogin){
        return userOperation.getUserByLogin(userLogin).getQueueData();
    }

    public Collection<Data> getInputData(String userLogin){
        return userOperation.getUserByLogin(userLogin).getInputData().values();
    }

    public Set<Device> getDevices(String userLogin){
        return userOperation.getUserByLogin(userLogin).getDevices();
    }

    public void sendMessage(String userLogin, String mqttName, String topic, String content){
        userOperation.getUserByLogin(userLogin).sendMessage(mqttName,topic,content);
    }

    public void sendMessage(String userLogin, String deviceName, String commandName, String... params){
        userOperation.getUserByLogin(userLogin).sendMessage(deviceName,commandName,params);
    }
}
