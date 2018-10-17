package org.eugene.webapp.core.model.filter;

import java.util.Map;

public class Data {
    private final String filterName;
    private final String mqttName;
    private final String topicName;
    private final Map<String, String> keyValues;
    private final String message;

    public Data(String filterName, String mqttName, String topicName, Map<String, String> keyValues, String message) {
        this.filterName = filterName;
        this.mqttName = mqttName;
        this.topicName = topicName;
        this.keyValues = keyValues;
        this.message = message;
    }

    public String getFilterName() {
        return filterName;
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

    public String getDataWithFilters() {
        return "Data{" +
                "mqttName='" + mqttName + '\'' +
                ", topicName='" + topicName + '\'' +
                ", keyValues=" + keyValues +
                '}';
    }

    public String getDataWithoutFilters() {
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
