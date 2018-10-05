package org.eugene.webapp.core.user;

import com.google.gson.reflect.TypeToken;
import org.eugene.webapp.core.db.services.DbUserService;
import org.eugene.webapp.core.dto.ConverterDto;
import org.eugene.webapp.core.dto.UserDto;
import org.eugene.webapp.core.mqtt.MqttConnect;
import org.eugene.webapp.core.parsing.ConverterData;
import org.eugene.webapp.core.save.WriterReaderFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.eugene.webapp.core.printer.PrintInformation.*;

@Component
public class UserOperation {
    private Map<String,User> userMap = new HashMap<>();
    private User currentUser;
    private ConverterData converterData;
    private final ConverterDto converterDto;
    private final DbUserService dbUserService;

    @Autowired
    public UserOperation(ConverterDto converterDto, DbUserService dbUserService) {
        this.converterDto = converterDto;
        this.dbUserService = dbUserService;
    }

    public void addUser(User user){
        if(!userMap.keySet().contains(user.getLogin())){
            userMap.put(user.getLogin(),user);
            currentUser = user;
            crudOperationUserIntoDB("create");
            printSystemInformation("user added");
        } else {
            printSystemInformation("user with login < "+user.getLogin()+" > already exist");
        }
    }

    public void selectUser(String login){
        currentUser = userMap.get(login);
        if(currentUser == null){
            printSystemInformation("user with login < "+login+" > not found !!!");
        } else {
            printSystemInformation("selected user with login < "+login+" >");
        }
    }

    public void removeUser(){
        if(currentUser != null){
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
        }
    }

    public void saveUsers(){
        Map<String,UserDto> userDtoMap = new HashMap<>();
        for (String userLogin : userMap.keySet()){
            userDtoMap.put(userLogin,converterDto.convertToUserDto(userMap.get(userLogin)));
        }
        WriterReaderFileUtil.write(userDtoMap,"users");
    }

    public void loadUsers(){
        Type type = new TypeToken<Map<String,UserDto>>(){}.getType();
        Map<String,UserDto> userDtoMap;
        userDtoMap = WriterReaderFileUtil.read("users",type);
        if(userDtoMap != null){
            for (String userLogin : userDtoMap.keySet()){
                userMap.put(userLogin,converterDto.convertToUser(userDtoMap.get(userLogin)));
            }
        }
    }

    //----------------- methods for working Data Base -----------------------------

    public void saveUsersIntoDB(){
        for (User user : userMap.values()){
            dbUserService.persist(user);
        }
    }

    public void loadUsersFromDB(){
        for (Map.Entry<String,User> entry : dbUserService.findAll().entrySet()){
            userMap.put(entry.getKey(),entry.getValue());
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

    public void easyUpdateIntoDB(){
        dbUserService.easyUpdate(currentUser);
    }

    public void addConverterDataIntoDB(String userLogin, ConverterData converterData){
        dbUserService.addConverterDataAndUpdate(userLogin,converterData);
    }

    public void removeConverterDataFromDB(String userLogin, ConverterData converterData){
        if(converterData == null) return;
        dbUserService.removeConverterDataAndUpdate(userLogin,converterData);
    }

    //-------------------------------------------------------------------------------

    public void removeMqttConnectFromUsers(MqttConnect mqttConnect){
        for(User user : userMap.values()){
            user.removeMqttConnect(mqttConnect);
        }
    }

    public void setConverterData(ConverterData converterData) {
        this.converterData = converterData;
    }

    public ConverterData getConverterData() {
        return converterData;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public User getUserByLogin(String login){
        return userMap.get(login);
    }

    public void printAllUsers(){
        Collection<User> userCollection = userMap.values();
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
