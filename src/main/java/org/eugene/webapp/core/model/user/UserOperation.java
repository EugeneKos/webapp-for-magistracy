package org.eugene.webapp.core.model.user;

import org.eugene.webapp.core.dao.DataFilterDao;
import org.eugene.webapp.core.dao.DeviceDao;
import org.eugene.webapp.core.dao.UserDao;
import org.eugene.webapp.core.dao.UserNameEntityDao;
import org.eugene.webapp.core.model.device.Device;
import org.eugene.webapp.core.model.filter.DataFilter;
import org.eugene.webapp.core.model.mqtt.MqttConnect;
import org.eugene.webapp.core.model.mqtt.UserNameEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static org.eugene.webapp.core.utils.PrintInformation.*;

@Component
public class UserOperation {
    private Set<User> users = new HashSet<>();
    private User currentUser;
    private DataFilter dataFilter;
    private final UserDao userDao;
    private final DeviceDao deviceDao;
    private final DataFilterDao dataFilterDao;
    private final UserNameEntityDao userNameEntityDao;

    @Autowired
    public UserOperation(UserDao userDao, DeviceDao deviceDao, DataFilterDao dataFilterDao, UserNameEntityDao userNameEntityDao) {
        this.userDao = userDao;
        this.deviceDao = deviceDao;
        this.dataFilterDao = dataFilterDao;
        this.userNameEntityDao = userNameEntityDao;
    }

    public void setDataFilter(DataFilter dataFilter) {
        this.dataFilter = dataFilter;
    }

    public DataFilter getDataFilter() {
        return dataFilter;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void addUser(User user) {
        if(users.add(user)){
            userDao.persist(user);
            userNameEntityDao.persist(new UserNameEntity(user.getLogin()));
            currentUser = user;
            printSystemInformation("user added");
        } else {
            printSystemInformation("user with login < "+user.getLogin()+" > already exist");
        }
    }

    public void selectUser(String login) {
        for (User user : users){
            if(user.getLogin().equals(login)){
                currentUser = user;
                printSystemInformation("selected user with login < "+login+" >");
                return;
            }
        }
        printSystemInformation("user with login < "+login+" > not found !!!");
    }

    public void removeUser() {
        if (currentUser != null) {
            if(currentUser.isMqttConnectsEmpty()){
                users.remove(currentUser);
                userDao.removeByLogin(currentUser.getLogin());
                userNameEntityDao.removeByUserName(currentUser.getLogin());
                printSystemInformation("current user deleted");
                currentUser = null;
            } else {
                printSystemInformation("removal impossible, remove all incoming mqtt connections");
            }
        } else {
            printSystemInformation("current user not found !!!");
        }
    }

    public UserNameEntity getUserNameEntityByUserName(String userName){
        return userNameEntityDao.findByUserName(userName);
    }

    public User getUserByLogin(String login) {
        for (User user : users){
            if(user.getLogin().equals(login)){
                return user;
            }
        }
        return null;
    }

    public User getUserByLoginFromDB(String login){
        return userDao.findByLogin(login);
    }

    public void updateUserInDB() {
        if (currentUser != null) {
            User user = userDao.findByLogin(currentUser.getLogin());
            if(user != null){
                user.setPassword(currentUser.getPassword());
                user.setRole(currentUser.getRole());
                user.setBufferSize(currentUser.getBufferSize());
                userDao.update(user);
            }
        }
    }

    //--------------------------------devices------------------------------------------------

    public void addDeviceIntoUserAndUpdate(Device device){
        User user = userDao.findByLogin(currentUser.getLogin());
        if(user != null){
            user.getDevices().add(device);
            userDao.update(user);
        }
    }

    public void addDeviceIntoDB(Device device){
        if(getDeviceByName(device.getName()) == null){
            deviceDao.persist(device);
            printSystemInformation("device added into data base");
        } else {
            printSystemInformation("device already exist in data base");
        }
    }

    public void removeDeviceFromDB(String deviceName){
        Device device = getDeviceByName(deviceName);
        if(device != null){
            for (User user : users){
                user.getDevices().remove(device);
            }
            for (User user : userDao.findAll()){
                user.getDevices().remove(device);
                userDao.update(user);
            }
            deviceDao.removeByDeviceName(deviceName);
            printSystemInformation("device deleted from data base");
        } else {
            printSystemInformation("device not found");
        }
    }

    public void removeDeviceFromLinkTable(String userLogin, Device device){
        User user = userDao.findByLogin(userLogin);
        if(user != null){
            user.getDevices().remove(device);
            userDao.update(user);
        }
    }

    public Device getDeviceByName(String deviceName){
        return deviceDao.findByDeviceName(deviceName);
    }

    //--------------------------------------------------------------------------------------

    //--------------------------------filters-----------------------------------------------

    public void addFilterIntoUserAndUpdate(DataFilter dataFilter){
        User user = userDao.findByLogin(currentUser.getLogin());
        if(user != null){
            user.getFilters().add(dataFilter);
            userDao.update(user);
        }
    }

    public void addDataFilterIntoDB(DataFilter dataFilter){
        if(getDataFilterByName(dataFilter.getName()) == null){
            dataFilterDao.persist(dataFilter);
            printSystemInformation("filter added into data base");
        } else {
            printSystemInformation("filter exist in data base");
        }
    }

    public void removeDataFilterFromDB(String dataFilterName){
        DataFilter dataFilter = getDataFilterByName(dataFilterName);
        if(dataFilter != null){
            for (User user : users){
                user.getFilters().remove(dataFilter);
                user.getInputData().remove(dataFilterName);
            }
            for (User user : userDao.findAll()){
                user.getFilters().remove(dataFilter);
                userDao.update(user);
            }
            dataFilterDao.removeByDataFilterName(dataFilterName);
            printSystemInformation("filter deleted from data base");
        } else {
            printSystemInformation("filter not found");
        }
    }

    public void removeDataFilterFromLinkTable(String userLogin, DataFilter dataFilter){
        User user = userDao.findByLogin(userLogin);
        if(user != null){
            user.getFilters().remove(dataFilter);
            userDao.update(user);
        }
    }

    public DataFilter getDataFilterByName(String dataFilterName){
        return dataFilterDao.findByDataFilterName(dataFilterName);
    }

    //--------------------------------------------------------------------------------------

    public void loadUsersFromDB(){
        users = new HashSet<>(userDao.findAll());
    }

    public void removeMqttConnectFromAllUsers(MqttConnect mqttConnect){
        for (User user : users){
            user.removeMqttConnect(mqttConnect);
        }
    }

    public void printAllUsers() {
        if (users.isEmpty()) {
            printSystemInformation("users not found !!!");
            return;
        }
        printCap();
        for (User user : users) {
            printFormatInformation(user.getLogin());
        }
        printCap();
    }

    public void printAllDevices(){
        Set<Device> devices = deviceDao.findAll();
        if(devices.isEmpty()){
            printSystemInformation("devices not found !!!");
            return;
        }
        printCap();
        for (Device device : devices) {
            printFormatInformation(device.getName());
        }
        printCap();
    }

    public void printAllDataFilters(){
        Set<DataFilter> filters = dataFilterDao.findAll();
        if(filters.isEmpty()){
            printSystemInformation("filters not found !!!");
            return;
        }
        printCap();
        for (DataFilter filter : filters) {
            printFormatInformation(filter.getName());
        }
        printCap();
    }
}
