package org.eugene.webapp.core.db.model.user;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "login" , unique = true)
    private String login;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    private String role;
    @Column(name = "buffer")
    private Integer bufferSize;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "userEntity")
    private Set<DataFilterEntity> filters;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "userEntity")
    private Set<DeviceEntity> devices;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void addDataFilterEntity(DataFilterEntity dataFilterEntity){
        filters.add(dataFilterEntity);
    }

    public Long getDataFilterEntityId(String mqttName, String topicName){
        for (DataFilterEntity dataFilterEntity : filters){
            if(dataFilterEntity.getMqttName().equals(mqttName) &&
                    dataFilterEntity.getTopicName().equals(topicName)){
                return dataFilterEntity.getId();
            }
        }
        return null;
    }

    public Set<DataFilterEntity> getFilters() {
        return filters;
    }

    public void setFilters(Set<DataFilterEntity> filters) {
        this.filters = filters;
    }

    public void addDeviceEntity(DeviceEntity deviceEntity){
        devices.add(deviceEntity);
    }

    public Long getDeviceEntityId(String deviceName){
        for (DeviceEntity deviceEntity : devices){
            if(deviceEntity.getName().equals(deviceName)){
                return deviceEntity.getId();
            }
        }
        return null;
    }

    public Set<DeviceEntity> getDevices() {
        return devices;
    }

    public void setDevices(Set<DeviceEntity> devices) {
        this.devices = devices;
    }
}
