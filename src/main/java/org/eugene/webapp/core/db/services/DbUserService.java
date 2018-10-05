package org.eugene.webapp.core.db.services;

import org.eugene.webapp.core.db.converters.UserConverterEntity;
import org.eugene.webapp.core.db.dao.UserDao;
import org.eugene.webapp.core.db.model.user.ConverterDataEntity;
import org.eugene.webapp.core.db.model.user.UserEntity;
import org.eugene.webapp.core.parsing.ConverterData;
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

    public void addConverterDataAndUpdate(String userLogin, ConverterData converterData){
        UserEntity userEntity = findByLogin(userLogin);
        if(userEntity == null) return;
        ConverterDataEntity converterDataEntity = userConverterEntity.convertToConverterDataEntity(converterData,userEntity);
        userEntity.addConverterDataEntity(converterDataEntity);
        userDao.update(userEntity);
    }

    public void removeConverterDataAndUpdate(String userLogin, ConverterData converterData){
        UserEntity userEntity = findByLogin(userLogin);
        if(userEntity == null) return;
        Long id = userEntity.getConverterDataEntityId(converterData.getMqttName(),converterData.getTopicName());
        if(id != null){
            userDao.removeConverterById(id);
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
