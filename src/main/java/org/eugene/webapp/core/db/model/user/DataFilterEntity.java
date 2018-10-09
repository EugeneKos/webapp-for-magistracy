package org.eugene.webapp.core.db.model.user;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "filters")
public class DataFilterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "topic_name")
    private String topicName;
    @Column(name = "mqtt_name")
    private String mqttName;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "dataFilterEntity")
    private Set<KeyValueRegexpEntity> keyValueRegexp;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "dataFilterEntity")
    private Set<DataConverterEntity> converters;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UserEntity userEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getMqttName() {
        return mqttName;
    }

    public void setMqttName(String mqttName) {
        this.mqttName = mqttName;
    }

    public Set<KeyValueRegexpEntity> getKeyValueRegexp() {
        return keyValueRegexp;
    }

    public void setKeyValueRegexp(Set<KeyValueRegexpEntity> keyValueRegexp) {
        this.keyValueRegexp = keyValueRegexp;
    }

    public Set<DataConverterEntity> getConverters() {
        return converters;
    }

    public void setConverters(Set<DataConverterEntity> converters) {
        this.converters = converters;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataFilterEntity that = (DataFilterEntity) o;

        if (topicName != null ? !topicName.equals(that.topicName) : that.topicName != null) return false;
        return mqttName != null ? mqttName.equals(that.mqttName) : that.mqttName == null;
    }

    @Override
    public int hashCode() {
        int result = topicName != null ? topicName.hashCode() : 0;
        result = 31 * result + (mqttName != null ? mqttName.hashCode() : 0);
        return result;
    }
}
