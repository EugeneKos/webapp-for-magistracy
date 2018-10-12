package org.eugene.webapp.core.user;

import org.eugene.webapp.core.dao.DataFilterDao;
import org.eugene.webapp.core.dao.DeviceDao;
import org.eugene.webapp.core.dao.UserDao;
import org.eugene.webapp.core.device.Device;
import org.eugene.webapp.core.filter.DataFilter;
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

    @Autowired
    public UserOperation(UserDao userDao, DeviceDao deviceDao, DataFilterDao dataFilterDao) {
        this.userDao = userDao;
        this.deviceDao = deviceDao;
        this.dataFilterDao = dataFilterDao;
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
            currentUser = user;
            printSystemInformation("user added");
        } else {
            printSystemInformation("user with login < "+user.getLogin()+" > already exist");
        }
    }

    public void selectUser(String login) {
        currentUser = userDao.findByLogin(login);
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
                printSystemInformation("current user deleted");
                currentUser = null;
            } else {
                printSystemInformation("removal impossible, remove all incoming mqtt connections");
            }
        } else {
            printSystemInformation("current user not found !!!");
        }
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

    public void addFilterIntoUserAndUpdate(DataFilter dataFilter){
        User user = userDao.findByLogin(currentUser.getLogin());
        if(user != null){
            user.getFilters().add(dataFilter);
            userDao.update(user);
        }
    }

    public void addDeviceIntoUserAndUpdate(Device device){
        User user = userDao.findByLogin(currentUser.getLogin());
        if(user != null){
            user.getDevices().add(device);
            userDao.update(user);
        }
    }


    //--------------------------------devices------------------------------------------------

    public void addDeviceIntoDB(Device device){
        if(getDeviceByName(device.getName()) == null){
            deviceDao.persist(device);
            printSystemInformation("device added into data base");
        } else {
            printSystemInformation("device already exist");
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
            printSystemInformation("device deleted");
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

    public void addDataFilterIntoDB(DataFilter dataFilter){
        if(getDataFilterByName(dataFilter.getName()) == null){
            dataFilterDao.persist(dataFilter);
            printSystemInformation("filter added");
        } else {
            printSystemInformation("filter exist");
        }
    }

    public void removeDataFilterFromDB(String dataFilterName){
        DataFilter dataFilter = getDataFilterByName(dataFilterName);
        if(dataFilter != null){
            for (User user : users){
                user.getFilters().remove(dataFilter);
            }
            for (User user : userDao.findAll()){
                user.getFilters().remove(dataFilter);
                userDao.update(user);
            }
            dataFilterDao.removeByDataFilterName(dataFilterName);
            printSystemInformation("filter deleted");
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
}
