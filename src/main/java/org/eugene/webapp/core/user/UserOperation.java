package org.eugene.webapp.core.user;

import org.eugene.webapp.core.db.services.DbUserService;
import org.eugene.webapp.core.mqtt.MqttConnect;
import org.eugene.webapp.core.parsing.device.Device;
import org.eugene.webapp.core.parsing.filter.DataFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.eugene.webapp.core.printer.PrintInformation.*;

@Component
public class UserOperation {
    //private Map<String,User> userMap = new HashMap<>();
    private User currentUser;
    private DataFilter dataFilter;
    private final DbUserService dbUserService;

    @Autowired
    public UserOperation(DbUserService dbUserService) {
        this.dbUserService = dbUserService;
    }

    public void addUser(User user){
        /*if(!userMap.keySet().contains(user.getLogin())){
            userMap.put(user.getLogin(),user);
            currentUser = user;
            crudOperationUserIntoDB("create");
            printSystemInformation("user added");
        } else {
            printSystemInformation("user with login < "+user.getLogin()+" > already exist");
        }*/
        dbUserService.persist(user);
    }

    public void selectUser(String login){
        /*currentUser = userMap.get(login);
        if(currentUser == null){
            printSystemInformation("user with login < "+login+" > not found !!!");
        } else {
            printSystemInformation("selected user with login < "+login+" >");
        }*/
        currentUser = dbUserService.findByLogin(login);
    }

    public void removeUser(){
        /*if(currentUser != null){
            if(currentUser.isMqttConnectsEmpty()){
                userMap.remove(currentUser.getLogin());
                crudOperationUserIntoDB("delete");
                printSystemInformation("current user deleted");
                currentUser = null;
            } else {
                printSystemInformation("removal impossible, remove all incoming mqtt connections");
            }
        } else {
            printSystemInformation("current user not found !!!");
        }*/
        if(currentUser != null){
            dbUserService.removeByLogin(currentUser.getLogin());
        }
    }

    //----------------- methods for working Data Base -----------------------------

   /* public void saveUsersIntoDB(){
        for (User user : userMap.values()){
            dbUserService.persist(user);
        }
    }

    public void loadUsersFromDB(){
        for (Map.Entry<String,User> entry : dbUserService.findAll().entrySet()){
            userMap.put(entry.getKey(),entry.getValue());
        }
    }*/

   public void updateUser(){
       if(currentUser != null){
           dbUserService.update(currentUser);
       }
   }

    private void crudOperationUserIntoDB(String crud){
        if(currentUser != null){
            switch (crud){
                case "create":
                    dbUserService.persist(currentUser);
                    break;
                case "delete":
                    dbUserService.removeByLogin(currentUser.getLogin());
                    break;
            }
        }
    }

   /* public void easyUpdateIntoDB(){
        dbUserService.easyUpdate(currentUser);
    }

    public void addDataFilterIntoDB(String userLogin, DataFilter dataFilter){
        dbUserService.addDataFilterAndUpdate(userLogin, dataFilter);
    }

    public void removeDataFilterFromDB(String userLogin, DataFilter dataFilter){
        if(dataFilter == null) return;
        dbUserService.removeDataFilterAndUpdate(userLogin, dataFilter);
    }

    public void addDeviceIntoDB(String userLogin, Device device){
        dbUserService.addDeviceAndUpdate(userLogin,device);
    }

    public void removeDeviceFromDB(String userLogin, Device device){
        if(device == null) return;
        dbUserService.removeDeviceAndUpdate(userLogin,device);
    }*/

    //-------------------------------------------------------------------------------

    /*public void removeMqttConnectFromUsers(MqttConnect mqttConnect){
        for(User user : userMap.values()){
            user.removeMqttConnect(mqttConnect);
        }
    }*/

    public void setDataFilter(DataFilter dataFilter) {
        this.dataFilter = dataFilter;
    }

    public DataFilter getDataFilter() {
        return dataFilter;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public User getUserByLogin(String login){
        return dbUserService.findByLogin(login);
    }

    public void printAllUsers(){
        Collection<User> userCollection = dbUserService.findAll();
        if(userCollection.isEmpty()){
            printSystemInformation("users not found !!!");
            return;
        }
        printCap();
        for (User user : userCollection){
            printFormatInformation(user.getLogin());
        }
        printCap();
    }
}
