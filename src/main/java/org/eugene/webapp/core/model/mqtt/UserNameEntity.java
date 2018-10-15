package org.eugene.webapp.core.model.mqtt;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user_names")
public class UserNameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "user_name")
    private String userName;
    @ManyToMany(mappedBy = "userNameEntities", fetch = FetchType.EAGER)
    private Set<MqttConnect> mqttConnects;

    public UserNameEntity(String userName) {
        this.userName = userName;
    }

    public UserNameEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<MqttConnect> getMqttConnects() {
        return mqttConnects;
    }

    public void setMqttConnects(Set<MqttConnect> mqttConnects) {
        this.mqttConnects = mqttConnects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserNameEntity userNameEntity1 = (UserNameEntity) o;

        return userName != null ? userName.equals(userNameEntity1.userName) : userNameEntity1.userName == null;
    }

    @Override
    public int hashCode() {
        return userName != null ? userName.hashCode() : 0;
    }
}
