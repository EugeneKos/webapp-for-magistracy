package org.eugene.webapp.services;

import org.eugene.webapp.core.parsing.Data;
import org.eugene.webapp.core.user.UserOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;

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

    public void sendMessage(String userLogin, String mqttName, String topic, String content){
        userOperation.getUserByLogin(userLogin).sendMessage(mqttName,topic,content);
    }

    public boolean checkUser(String userLogin, String password){
        return userOperation.checkUser(userLogin,password);
    }
}
