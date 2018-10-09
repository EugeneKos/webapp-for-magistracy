package org.eugene.webapp.core.dto;

import org.eugene.webapp.core.parsing.device.Device;
import org.eugene.webapp.core.parsing.filter.DataFilter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserDto {
    private String login;
    private String password;
    private String role;
    private int bufferSize;
    private Map<String,DataFilter> filters = new HashMap<>();
    private Set<Device> devices = new HashSet<>();

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

    public Map<String, DataFilter> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, DataFilter> filters) {
        this.filters = filters;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }
}
