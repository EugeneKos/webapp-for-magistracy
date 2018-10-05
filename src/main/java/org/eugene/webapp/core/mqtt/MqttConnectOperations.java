package org.eugene.webapp.core.mqtt;

import com.google.gson.reflect.TypeToken;
import org.eugene.webapp.core.db.services.DbMqttConnectService;
import org.eugene.webapp.core.dto.ConverterDto;
import org.eugene.webapp.core.dto.MqttConnectDto;
import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.user.UserOperation;
import org.eugene.webapp.core.save.WriterReaderFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import static org.eugene.webapp.core.printer.PrintInformation.*;

@Component
public class MqttConnectOperations {
    private Set<MqttConnect> mqttConnects = new HashSet<>();
    private MqttConnect currentMqttConnect;
    private final UserOperation userOperation;
    private final ConverterDto converterDto;
    private final DbMqttConnectService dbMqttConnectService;

    @Autowired
    public MqttConnectOperations(UserOperation userOperation, ConverterDto converterDto, DbMqttConnectService dbMqttConnectService) {
        this.userOperation = userOperation;
        this.converterDto = converterDto;
        this.dbMqttConnectService = dbMqttConnectService;
    }

    public void addMqtt(MqttConnect mqttConnect){
        if(mqttConnects.add(mqttConnect)){
            this.currentMqttConnect = mqttConnect;
            crudOperationMqttConnectIntoDB("create");
            printSystemInformation("mqtt connection added");
        } else {
            printSystemInformation("mqtt connection with this name already exists");
        }
    }

    public void removeMqtt(){
        if(mqttConnects.remove(currentMqttConnect)){
            printSystemInformation("mqtt connection deleted");
            userOperation.removeMqttConnectFromUsers(currentMqttConnect);
            crudOperationMqttConnectIntoDB("delete");
            currentMqttConnect = null;
        } else{
            printSystemInformation("mqtt connection does not exist");
        }

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

    public void selectMqtt(String nameMqtt){
        for (MqttConnect mqttConnect : mqttConnects) {
            if(mqttConnect.getMqttName().equals(nameMqtt)){
                this.currentMqttConnect = mqttConnect;
                printSystemInformation("selected mqtt connection < "+nameMqtt+" >");
                return;
            }
        }
        printSystemInformation("mqtt connection does not exist");
    }

    public MqttConnect getMqttConnectByName(String nameMqtt){
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

    public void saveMqttConnectsIntoDB(){
        for (MqttConnect mqttConnect : mqttConnects){
            dbMqttConnectService.persist(mqttConnect);
        }
    }

    /*public void updateMqttConnectsIntoDB(){
        for (MqttConnect mqttConnect : mqttConnects){
            dbMqttConnectService.update(mqttConnect);
        }
    }*/

    public void loadMqttConnectsFromDB(){
        mqttConnects.addAll(dbMqttConnectService.findAll());
        loadUsersToMqttConnect();
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

    public void addSubscribeIntoDB(String mqttName, String subscribe){
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
    }

    //-------------------------------------------------------------------------------

    public void saveMqttConnects(){
        Set<MqttConnectDto> mqttConnectDtoSet = new HashSet<>();
        for (MqttConnect mqttConnect : mqttConnects){
            mqttConnectDtoSet.add(converterDto.convertToMqttConnectDto(mqttConnect));
        }
        WriterReaderFileUtil.write(mqttConnectDtoSet,"mqtt_connects");
    }

    public void loadMqttConnects(){
        Type type = new TypeToken<Set<MqttConnectDto>>(){}.getType();
        Set<MqttConnectDto> mqttConnectDtoSet;
        mqttConnectDtoSet = WriterReaderFileUtil.read("mqtt_connects",type);
        if(mqttConnectDtoSet != null){
            for (MqttConnectDto mqttConnectDto : mqttConnectDtoSet){
                mqttConnects.add(converterDto.convertToMqttConnect(mqttConnectDto));
            }
            loadUsersToMqttConnect();
        }
    }

    private void loadUsersToMqttConnect(){
        for(MqttConnect mqttConnect : mqttConnects){
            for (String userName : mqttConnect.getUserNames()){
                User user = userOperation.getUserByLogin(userName);
                mqttConnect.addUser(user);
            }
        }
    }

    public void removeUserFromAllMqtt(User user){
        for (MqttConnect mqttConnect : mqttConnects){
            mqttConnect.removeUser(user);
            removeUserNameFromDB(mqttConnect.getMqttName(),user.getLogin());
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
