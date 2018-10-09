package org.eugene.webapp.core.db.services;

import org.eugene.webapp.core.db.converters.UserConverterEntity;
import org.eugene.webapp.core.db.dao.UserDao;
import org.eugene.webapp.core.db.model.user.DataFilterEntity;
import org.eugene.webapp.core.db.model.user.DeviceEntity;
import org.eugene.webapp.core.db.model.user.UserEntity;
import org.eugene.webapp.core.parsing.device.Device;
import org.eugene.webapp.core.parsing.filter.DataFilter;
import org.eugene.webapp.core.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DbUserService {
    private final UserDao userDao;
    private final UserConverterEntity userConverterEntity;

    @Autowired
    public DbUserService(UserDao userDao, UserConverterEntity userConverterEntity) {
        this.userDao = userDao;
        this.userConverterEntity = userConverterEntity;
    }

    public void persist(User user){
        UserEntity userEntity = userConverterEntity.convertToUserEntity(user);
        userDao.persist(userEntity);
    }

    public void easyUpdate(User user){
        UserEntity userEntity = findByLogin(user.getLogin());
        if(userEntity == null) return;
        userEntity.setPassword(user.getPassword());
        userEntity.setRole(user.getRole());
        userEntity.setBufferSize(user.getBufferSize());
        userDao.update(userEntity);
    }


    public UserEntity findByLogin(String login){
        return userDao.findByLogin(login);
    }

    public void removeByLogin(String login){
        userDao.removeByLogin(login);
    }

    public void addDataFilterAndUpdate(String userLogin, DataFilter dataFilter){
        UserEntity userEntity = findByLogin(userLogin);
        if(userEntity == null) return;
        DataFilterEntity dataFilterEntity = userConverterEntity.convertToDataFilterEntity(dataFilter,userEntity);
        userEntity.addDataFilterEntity(dataFilterEntity);
        userDao.update(userEntity);
    }

    public void removeDataFilterAndUpdate(String userLogin, DataFilter dataFilter){
        UserEntity userEntity = findByLogin(userLogin);
        if(userEntity == null) return;
        Long id = userEntity.getDataFilterEntityId(dataFilter.getMqttName(), dataFilter.getTopicName());
        if(id != null){
            userDao.removeFilterById(id);
        }
    }

    public void addDeviceAndUpdate(String userLogin, Device device){
        UserEntity userEntity = findByLogin(userLogin);
        if(userEntity == null) return;
        DeviceEntity deviceEntity = userConverterEntity.convertToDeviceEntity(device,userEntity);
        userEntity.addDeviceEntity(deviceEntity);
        userDao.update(userEntity);
    }

    public void removeDeviceAndUpdate(String userLogin, Device device){
        UserEntity userEntity = findByLogin(userLogin);
        if(userEntity == null) return;
        Long id = userEntity.getDeviceEntityId(device.getName());
        if(id != null){
           userDao.removeDeviceById(id);
        }
    }

    public Map<String,User> findAll(){
        List<UserEntity> userEntities = userDao.findAll();
        Map<String,User> userMap = new HashMap<>();
        for (UserEntity userEntity : userEntities){
            User user = userConverterEntity.convertToUser(userEntity);
            userMap.put(user.getLogin(),user);
        }
        return userMap;
    }
}
