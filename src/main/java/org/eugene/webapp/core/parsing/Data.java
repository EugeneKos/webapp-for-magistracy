package org.eugene.webapp.core.parsing;

import java.util.Map;

public class Data {
    private final String mqttName;
    private final String topicName;
    private final Map<String, String> keyValues;
    private final String message;

    public Data(String mqttName, String topicName, Map<String, String> keyValues, String message) {
        this.mqttName = mqttName;
        this.topicName = topicName;
        this.keyValues = keyValues;
        this.message = message;
    }

    public String getMqttName() {
        return mqttName;
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
                "mqttName='" + mqttName + '\'' +
                ", topicName='" + topicName + '\'' +
                ", keyValues=" + keyValues +
                '}';
    }

    public String getDataWithoutConverters() {
        return "Data{" +
                "mqttName='" + mqttName + '\'' +
                ", topicName='" + topicName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return "Data{" +
                "mqttName='" + mqttName + '\'' +
                ", topicName='" + topicName + '\'' +
                ", keyValues=" + keyValues +
                ", message='" + message + '\'' +
                '}';
    }
}
