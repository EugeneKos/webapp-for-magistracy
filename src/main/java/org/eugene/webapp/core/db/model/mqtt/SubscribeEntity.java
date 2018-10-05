package org.eugene.webapp.core.db.model.mqtt;

import javax.persistence.*;

@Entity
@Table(name = "subscribes")
public class SubscribeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "subscribe")
    private String subscribe;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MqttConnectEntity mqttConnectEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
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

        SubscribeEntity that = (SubscribeEntity) o;

        return subscribe != null ? subscribe.equals(that.subscribe) : that.subscribe == null;
    }

    @Override
    public int hashCode() {
        return subscribe != null ? subscribe.hashCode() : 0;
    }
}
