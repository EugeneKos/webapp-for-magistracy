package org.eugene.webapp.core.db.converters;

import org.eugene.webapp.core.db.model.user.ConverterDataEntity;
import org.eugene.webapp.core.db.model.user.KeyValueRegexpEntity;
import org.eugene.webapp.core.db.model.user.UserEntity;
import org.eugene.webapp.core.parsing.ConverterData;
import org.eugene.webapp.core.user.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class UserConverterEntity {
    public UserEntity convertToUserEntity(User user){
        UserEntity userEntity = new UserEntity();
        userEntity.setLogin(user.getLogin());
        userEntity.setPassword(user.getPassword());
        userEntity.setRole(user.getRole());
        userEntity.setBufferSize(user.getBufferSize());
        Map<String,ConverterData> converterDataMap = user.getConverters();
        Set<ConverterDataEntity> converterDataEntitySet = new HashSet<>();
        for (ConverterData converterData : converterDataMap.values()){
            converterDataEntitySet.add(convertToConverterDataEntity(converterData,userEntity));
        }
        userEntity.setConverters(converterDataEntitySet);
        return userEntity;
    }

    public ConverterDataEntity convertToConverterDataEntity(ConverterData converterData, UserEntity userEntity){
        ConverterDataEntity converterDataEntity = new ConverterDataEntity();
        converterDataEntity.setMqttName(converterData.getMqttName());
        converterDataEntity.setTopicName(converterData.getTopicName());
        Map<String,String> keyValueRegexp = converterData.getKeyValueRegexp();
        Set<KeyValueRegexpEntity> keyValueRegexpEntitySet = new HashSet<>();
        for (Map.Entry<String,String> keyValue : keyValueRegexp.entrySet()){
            keyValueRegexpEntitySet.add(convertToKeyValueRegexpEntity(keyValue.getKey(),keyValue.getValue(),converterDataEntity));
        }
        converterDataEntity.setKeyValueRegexp(keyValueRegexpEntitySet);
        converterDataEntity.setUserEntity(userEntity);
        return converterDataEntity;
    }

    private KeyValueRegexpEntity convertToKeyValueRegexpEntity(String key, String value, ConverterDataEntity converterDataEntity){
        KeyValueRegexpEntity keyValueRegexpEntity = new KeyValueRegexpEntity();
        keyValueRegexpEntity.setKey(key);
        keyValueRegexpEntity.setValue(value);
        keyValueRegexpEntity.setConverterDataEntity(converterDataEntity);
        return keyValueRegexpEntity;
    }

    public User convertToUser(UserEntity userEntity){
        User user = new User(userEntity.getLogin(),userEntity.getPassword(),userEntity.getRole(),userEntity.getBufferSize());
        Map<String,ConverterData> converters = new HashMap<>();
        for (ConverterDataEntity converterDataEntity : userEntity.getConverters()){
            ConverterData converterData = convertToConverterData(converterDataEntity);
            String nameConverter = converterData.getMqttName()+User.delimiter+converterData.getTopicName();
            converters.put(nameConverter,converterData);
        }
        user.setConverters(converters);
        return user;
    }

    private ConverterData convertToConverterData(ConverterDataEntity converterDataEntity){
        ConverterData converterData = new ConverterData(converterDataEntity.getTopicName(),converterDataEntity.getMqttName());
        Map<String,String> keyValueRegexp = new HashMap<>();
        for (KeyValueRegexpEntity keyValueRegexpEntity : converterDataEntity.getKeyValueRegexp()){
            keyValueRegexp.put(keyValueRegexpEntity.getKey(),keyValueRegexpEntity.getValue());
        }
        converterData.setKeyValueRegexp(keyValueRegexp);
        return converterData;
    }
}
