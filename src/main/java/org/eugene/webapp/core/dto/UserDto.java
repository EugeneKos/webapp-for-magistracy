package org.eugene.webapp.core.dto;

import org.eugene.webapp.core.parsing.ConverterData;

import java.util.HashMap;
import java.util.Map;

public class UserDto {
    private String login;
    private String password;
    private String role;
    private int bufferSize;
    private Map<String,ConverterData> converters = new HashMap<>();

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public Map<String, ConverterData> getConverters() {
        return converters;
    }

    public void setConverters(Map<String, ConverterData> converters) {
        this.converters = converters;
    }
}
