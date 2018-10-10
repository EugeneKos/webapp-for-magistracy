package org.eugene.webapp.core.mqtt;

import javax.persistence.*;

@Entity
@Table(name = "subscribes")
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "subscribe")
    private String subscribe;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MqttConnect mqttConnect;

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

    public MqttConnect getMqttConnect() {
        return mqttConnect;
    }

    public void setMqttConnect(MqttConnect mqttConnect) {
        this.mqttConnect = mqttConnect;
    }
}
