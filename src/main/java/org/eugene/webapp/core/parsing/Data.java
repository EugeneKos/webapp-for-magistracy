package org.eugene.webapp.core.parsing;

import java.util.Map;

public class Data {
    private final String nameMqtt;
    private final String topicName;
    private final Map<String, String> keyValues;
    private final String message;

    public Data(String nameMqtt, String topicName, Map<String, String> keyValues, String message) {
        this.nameMqtt = nameMqtt;
        this.topicName = topicName;
        this.keyValues = keyValues;
        this.message = message;
    }

    public String getNameMqtt() {
        return nameMqtt;
    }

    public String getTopicName() {
        return topicName;
    }

    public Map<String, String> getKeyValues() {
        return keyValues;
    }

    public String getMessage() {
        return message;
    }

    public String getDataWithConverters() {
        return "Data{" +
                "nameMqtt='" + nameMqtt + '\'' +
                ", topicName='" + topicName + '\'' +
                ", keyValues=" + keyValues +
                '}';
    }

    public String getDataWithoutConverters() {
        return "Data{" +
                "nameMqtt='" + nameMqtt + '\'' +
                ", topicName='" + topicName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return "Data{" +
                "nameMqtt='" + nameMqtt + '\'' +
                ", topicName='" + topicName + '\'' +
                ", keyValues=" + keyValues +
                ", message='" + message + '\'' +
                '}';
    }
}
