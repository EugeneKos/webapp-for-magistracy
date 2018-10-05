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
    private Set<ConverterDataEntity> converters;

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

    public void addConverterDataEntity(ConverterDataEntity converterDataEntity){
        converters.add(converterDataEntity);
    }

    public Long getConverterDataEntityId(String mqttName, String topicName){
        for (ConverterDataEntity converterDataEntity : converters){
            if(converterDataEntity.getMqttName().equals(mqttName) &&
                    converterDataEntity.getTopicName().equals(topicName)){
                return converterDataEntity.getId();
            }
        }
        return null;
    }

    public Set<ConverterDataEntity> getConverters() {
        return converters;
    }

    public void setConverters(Set<ConverterDataEntity> converters) {
        this.converters = converters;
    }
}
