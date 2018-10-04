package org.eugene.webapp.core.db.model.mqtt;

import javax.persistence.*;

@Entity
@Table(name = "user_names")
public class UserNameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "user_name")
    private String userName;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MqttConnectEntity mqttConnectEntity;

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

    public MqttConnectEntity getMqttConnectEntity() {
        return mqttConnectEntity;
    }

    public void setMqttConnectEntity(MqttConnectEntity mqttConnectEntity) {
        this.mqttConnectEntity = mqttConnectEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserNameEntity that = (UserNameEntity) o;

        return userName != null ? userName.equals(that.userName) : that.userName == null;
    }

    @Override
    public int hashCode() {
        return userName != null ? userName.hashCode() : 0;
    }
}
