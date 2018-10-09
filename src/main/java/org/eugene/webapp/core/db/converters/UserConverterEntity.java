package org.eugene.webapp.core.db.converters;

import org.eugene.webapp.core.db.model.user.*;
import org.eugene.webapp.core.parsing.device.ControlCommand;
import org.eugene.webapp.core.parsing.device.Device;
import org.eugene.webapp.core.parsing.filter.DataConverter;
import org.eugene.webapp.core.parsing.filter.DataFilter;
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
        Map<String,DataFilter> dataFilterMap = user.getFilters();
        Set<DataFilterEntity> dataFilterEntitySet = new HashSet<>();
        for (DataFilter dataFilter : dataFilterMap.values()){
            dataFilterEntitySet.add(convertToDataFilterEntity(dataFilter,userEntity));
        }
        userEntity.setFilters(dataFilterEntitySet);
        Set<DeviceEntity> deviceEntities = new HashSet<>();
        for (Device device : user.getDevices()){
            deviceEntities.add(convertToDeviceEntity(device,userEntity));
        }
        userEntity.setDevices(deviceEntities);
        return userEntity;
    }

    public DataFilterEntity convertToDataFilterEntity(DataFilter dataFilter, UserEntity userEntity){
        DataFilterEntity dataFilterEntity = new DataFilterEntity();
        dataFilterEntity.setMqttName(dataFilter.getMqttName());
        dataFilterEntity.setTopicName(dataFilter.getTopicName());
        Map<String,String> keyValueRegexp = dataFilter.getKeyValueRegexp();
        Set<KeyValueRegexpEntity> keyValueRegexpEntitySet = new HashSet<>();
        for (Map.Entry<String,String> keyValue : keyValueRegexp.entrySet()){
            keyValueRegexpEntitySet.add(convertToKeyValueRegexpEntity(keyValue.getKey(),keyValue.getValue(), dataFilterEntity));
        }
        dataFilterEntity.setKeyValueRegexp(keyValueRegexpEntitySet);
        Set<DataConverterEntity> dataConverterEntities = new HashSet<>();
        for (DataConverter dataConverter : dataFilter.getConverters()){
            dataConverterEntities.add(convertToDataConverterEntity(dataConverter,dataFilterEntity));
        }
        dataFilterEntity.setConverters(dataConverterEntities);
        dataFilterEntity.setUserEntity(userEntity);
        return dataFilterEntity;
    }

    private KeyValueRegexpEntity convertToKeyValueRegexpEntity(String key, String value, DataFilterEntity dataFilterEntity){
        KeyValueRegexpEntity keyValueRegexpEntity = new KeyValueRegexpEntity();
        keyValueRegexpEntity.setKey(key);
        keyValueRegexpEntity.setValue(value);
        keyValueRegexpEntity.setDataFilterEntity(dataFilterEntity);
        return keyValueRegexpEntity;
    }

    private DataConverterEntity convertToDataConverterEntity(DataConverter dataConverter, DataFilterEntity dataFilterEntity){
        DataConverterEntity dataConverterEntity = new DataConverterEntity();
        dataConverterEntity.setKey(dataConverter.getKey());
        dataConverterEntity.setInput(dataConverter.getInput());
        dataConverterEntity.setOutput(dataConverter.getOutput());
        dataConverterEntity.setDataFilterEntity(dataFilterEntity);
        return dataConverterEntity;
    }

    public DeviceEntity convertToDeviceEntity(Device device, UserEntity userEntity){
        DeviceEntity deviceEntity = new DeviceEntity();
        deviceEntity.setName(device.getName());
        deviceEntity.setDescription(device.getDescription());
        deviceEntity.setMqttName(device.getMqttName());
        deviceEntity.setTopic(device.getTopic());
        Set<ControlCommandEntity> controlCommandEntities = new HashSet<>();
        for (ControlCommand controlCommand : device.getCommands()){
            controlCommandEntities.add(convertToControlCommandEntity(controlCommand,deviceEntity));
        }
        deviceEntity.setCommands(controlCommandEntities);
        deviceEntity.setUserEntity(userEntity);
        return deviceEntity;
    }

    private ControlCommandEntity convertToControlCommandEntity(ControlCommand controlCommand, DeviceEntity deviceEntity){
        ControlCommandEntity controlCommandEntity = new ControlCommandEntity();
        controlCommandEntity.setName(controlCommand.getName());
        controlCommandEntity.setParams(controlCommand.getParams());
        controlCommandEntity.setDescription(controlCommand.getDescription());
        controlCommandEntity.setCommandText(controlCommand.getCommandText());
        controlCommandEntity.setDeviceEntity(deviceEntity);
        return controlCommandEntity;
    }

    public User convertToUser(UserEntity userEntity){
        User user = new User(userEntity.getLogin(),userEntity.getPassword(),userEntity.getRole(),userEntity.getBufferSize());
        Map<String,DataFilter> converters = new HashMap<>();
        for (DataFilterEntity dataFilterEntity : userEntity.getFilters()){
            DataFilter dataFilter = convertToDataFilter(dataFilterEntity);
            String nameConverter = dataFilter.getMqttName()+User.delimiter+ dataFilter.getTopicName();
            converters.put(nameConverter, dataFilter);
        }
        user.setFilters(converters);
        Set<Device> devices = new HashSet<>();
        for (DeviceEntity deviceEntity : userEntity.getDevices()){
            devices.add(convertToDevice(deviceEntity));
        }
        user.setDevices(devices);
        return user;
    }

    private DataFilter convertToDataFilter(DataFilterEntity dataFilterEntity){
        DataFilter dataFilter = new DataFilter(dataFilterEntity.getTopicName(), dataFilterEntity.getMqttName());
        Map<String,String> keyValueRegexp = new HashMap<>();
        for (KeyValueRegexpEntity keyValueRegexpEntity : dataFilterEntity.getKeyValueRegexp()){
            keyValueRegexp.put(keyValueRegexpEntity.getKey(),keyValueRegexpEntity.getValue());
        }
        dataFilter.setKeyValueRegexp(keyValueRegexp);
        Set<DataConverter> converters = new HashSet<>();
        for (DataConverterEntity dataConverterEntity : dataFilterEntity.getConverters()){
            converters.add(convertToDataConverter(dataConverterEntity));
        }
        dataFilter.setConverters(converters);
        return dataFilter;
    }

    private DataConverter convertToDataConverter(DataConverterEntity dataConverterEntity){
        return new DataConverter(dataConverterEntity.getKey(), dataConverterEntity.getInput(),dataConverterEntity.getOutput());
    }

    private Device convertToDevice(DeviceEntity deviceEntity){
        Device device = new Device(deviceEntity.getName(),deviceEntity.getDescription(),deviceEntity.getMqttName(),deviceEntity.getTopic());
        Set<ControlCommand> commands = new HashSet<>();
        for (ControlCommandEntity controlCommandEntity : deviceEntity.getCommands()) {
            commands.add(convertToControlCommand(controlCommandEntity));
        }
        device.setCommands(commands);
        return device;
    }

    private ControlCommand convertToControlCommand(ControlCommandEntity controlCommandEntity){
        return new ControlCommand(controlCommandEntity.getName(),controlCommandEntity.getParams(),controlCommandEntity.getDescription(),controlCommandEntity.getCommandText());
    }
}
