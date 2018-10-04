package org.eugene.webapp.core.dto;

import org.eugene.webapp.core.user.User;
import org.eugene.webapp.core.mqtt.MqttConnect;
import org.springframework.stereotype.Component;

@Component
public class ConverterDto {
    public MqttConnect convertToMqttConnect(MqttConnectDto mqttConnectDto){
        MqttConnect mqttConnect = new MqttConnect(mqttConnectDto.getMqttName(),mqttConnectDto.getBroker(),mqttConnectDto.getClientId());
        mqttConnect.setUserNames(mqttConnectDto.getUserNames());
        mqttConnect.setSetSubscribes(mqttConnectDto.getSetSubscribes());
        return mqttConnect;
    }

    public MqttConnectDto convertToMqttConnectDto(MqttConnect mqttConnect){
        MqttConnectDto mqttConnectDto = new MqttConnectDto();
        mqttConnectDto.setMqttName(mqttConnect.getMqttName());
        mqttConnectDto.setBroker(mqttConnect.getBroker());
        mqttConnectDto.setClientId(mqttConnect.getClientId());
        mqttConnectDto.setUserNames(mqttConnect.getUserNames());
        mqttConnectDto.setSetSubscribes(mqttConnect.getSetSubscribes());
        return mqttConnectDto;
    }

    public User convertToUser(UserDto userDto){
        User user = new User(userDto.getLogin(),userDto.getPassword(),userDto.getRole(),userDto.getBufferSize());
        user.setConverters(userDto.getConverters());
        return user;
    }

    public UserDto convertToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setLogin(user.getLogin());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());
        userDto.setBufferSize(user.getBufferSize());
        userDto.setConverters(user.getConverters());
        return userDto;
    }

}
