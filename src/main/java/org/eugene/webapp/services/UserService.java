package org.eugene.webapp.services;

import org.eugene.webapp.core.model.device.Device;
import org.eugene.webapp.core.model.filter.Data;
import org.eugene.webapp.core.model.user.UserOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
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

    public Map<String, String> getStatusMqttConnects(String userLogin){
        return userOperation.getUserByLogin(userLogin).getStatusMqttConnects();
    }

    public void useTheDevice(String userLogin, String deviceName, String commandName, String... params){
        userOperation.getUserByLogin(userLogin).useTheDevice(deviceName,commandName,params);
    }
}
