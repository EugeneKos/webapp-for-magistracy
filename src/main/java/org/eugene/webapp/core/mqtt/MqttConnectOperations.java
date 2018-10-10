package org.eugene.webapp.core.mqtt;

import org.eugene.webapp.core.db.services.DbMqttConnectService;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static org.eugene.webapp.core.printer.PrintInformation.*;

@Component
public class MqttConnectOperations {
    //private Set<MqttConnect> mqttConnects = new HashSet<>();
    private MqttConnect currentMqttConnect;
    private final UserOperation userOperation;
    private final DbMqttConnectService dbMqttConnectService;

    @Autowired
    public MqttConnectOperations(UserOperation userOperation, DbMqttConnectService dbMqttConnectService) {
        this.userOperation = userOperation;
        this.dbMqttConnectService = dbMqttConnectService;
    }

    public void addMqtt(MqttConnect mqttConnect){
        /*if(mqttConnects.add(mqttConnect)){
            this.currentMqttConnect = mqttConnect;
            crudOperationMqttConnectIntoDB("create");
            printSystemInformation("mqtt connection added");
        } else {
            printSystemInformation("mqtt connection with this name already exists");
        }*/
        dbMqttConnectService.persist(mqttConnect);
    }

    public void removeMqtt(){
        /*if(mqttConnects.remove(currentMqttConnect)){
            printSystemInformation("mqtt connection deleted");
            userOperation.removeMqttConnectFromUsers(currentMqttConnect);
            crudOperationMqttConnectIntoDB("delete");
            currentMqttConnect = null;
        } else{
            printSystemInformation("mqtt connection does not exist");
        }*/
        if(currentMqttConnect != null){
            dbMqttConnectService.removeByMqttName(currentMqttConnect.getMqttName());
        }
    }

    public void startAllMqtt(){
        Set<MqttConnect> mqttConnects = dbMqttConnectService.findAll();
        for (MqttConnect mqttConnect : mqttConnects){
            mqttConnect.startMqtt();
        }
    }

    public void disconnectAllMqtt(){
        Set<MqttConnect> mqttConnects = dbMqttConnectService.findAll();
        for (MqttConnect mqttConnect : mqttConnects){
            mqttConnect.closeMqtt();
        }
    }

    public void selectMqtt(String mqttName){
        /*for (MqttConnect mqttConnect : mqttConnects) {
            if(mqttConnect.getMqttName().equals(mqttName)){
                this.currentMqttConnect = mqttConnect;
                printSystemInformation("selected mqtt connection < "+mqttName+" >");
                return;
            }
        }*/
        currentMqttConnect = dbMqttConnectService.findByMqttName(mqttName);
        printSystemInformation("mqtt connection does not exist");
    }

    public MqttConnect getMqttConnectByName(String nameMqtt){
        Set<MqttConnect> mqttConnects = dbMqttConnectService.findAll();
        for (MqttConnect mqttConnect : mqttConnects) {
            if(mqttConnect.getMqttName().equals(nameMqtt)){
                return mqttConnect;
            }
        }
        return null;
    }

    public MqttConnect getCurrentMqttConnect(){
        return currentMqttConnect;
    }

    //----------------- methods for working Data Base -----------------------------

    public void loadMqttConnectsFromDB(){
        //mqttConnects.addAll(dbMqttConnectService.findAll());
    }

    private void crudOperationMqttConnectIntoDB(String crud){
        if(currentMqttConnect != null){
            switch (crud){
                case "create":
                    dbMqttConnectService.persist(currentMqttConnect);
                    break;
                case "delete":
                    dbMqttConnectService.removeByMqttName(currentMqttConnect.getMqttName());
                    break;
            }
        }
    }

    public void updateMqttConnect(){
        if(currentMqttConnect != null){
            dbMqttConnectService.update(currentMqttConnect);
        }
    }

    /*public void addSubscribeIntoDB(String mqttName, String subscribe){
        dbMqttConnectService.addSubscribeAndUpdate(mqttName,subscribe);
    }

    public void removeSubscribesFromDB(String mqttName, String[] subscribes){
        for (String subscribe : subscribes){
            dbMqttConnectService.removeSubscribeAndUpdate(mqttName,subscribe);
        }
    }

    public void addUserNameIntoDB(String mqttName, String userLogin){
        dbMqttConnectService.addUserNameAndUpdate(mqttName,userLogin);
    }

    public void removeUserNameFromDB(String mqttName, String userLogin){
        dbMqttConnectService.removeUserNameAndUpdate(mqttName,userLogin);
    }*/

    //-------------------------------------------------------------------------------

    public void removeUserFromAllMqtt(User user){
        Set<MqttConnect> mqttConnects = dbMqttConnectService.findAll();
        for (MqttConnect mqttConnect : mqttConnects){
            mqttConnect.removeUser(user);
            //removeUserNameFromDB(mqttConnect.getMqttName(),user.getLogin());
        }
    }

    public void printAllNameMqttConnections(){
        Set<MqttConnect> mqttConnects = dbMqttConnectService.findAll();
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
