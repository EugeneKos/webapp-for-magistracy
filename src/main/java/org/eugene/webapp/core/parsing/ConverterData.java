package org.eugene.webapp.core.parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ConverterData содержит ключи, которые задаются отдельно и значения, которые являются строкой регулярного выражения
 * для распарсировки входной строки грязных данных. Второй Map предназначен для хранения ключей из первого и хранения значений распарсированных
 * данных от regexp второго Map.
 */

public class ConverterData {
    private String topicName;
    private String mqttName;
    private Map<String,String> keyValueRegexp = new HashMap<>();
    private boolean resolutionPrint;

    public ConverterData(String topicName, String mqttName){
        this.topicName = topicName;
        this.mqttName = mqttName;

    }

    public void put(String key, String value){
        keyValueRegexp.put(key,value);
    }

    public Data convert(String dirtyData){
        HashMap<String,String> map = new HashMap<>();
        for (String key : keyValueRegexp.keySet()){
            map.put(key,parse(dirtyData,keyValueRegexp.get(key)));
        }
        return new Data(mqttName,topicName,map, dirtyData);
    }

    /**
     *
     * @param message входня строка данных сообщений от брокера и конкретной темы
     * @param regexp регулярное выражение для нахождения изменяющихся данных
     * @return возвращается изменяющиеся данные
     */
    private String parse(String message, String regexp){
        String match = null;
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(message);
        while(matcher.find()){
            match = matcher.group(1);
        }
        printParseData(message,regexp,match);
        return match;
    }

    private void printParseData(String message, String regexp, String match){
        if(resolutionPrint){
            System.out.println("Message ---> "+message);
            System.out.println("Regexp ---> "+regexp);
            System.out.println("Match/Value ---> "+match);
            System.out.println("------------------------------------------------");
        }
    }

    public void setResolutionPrint(boolean resolutionPrint) {
        this.resolutionPrint = resolutionPrint;
    }

    public String getMqttName(){
        return mqttName;
    }

    public String getTopicName(){
        return topicName;
    }

    public Map<String, String> getKeyValueRegexp() {
        return keyValueRegexp;
    }

    public void setKeyValueRegexp(Map<String, String> keyValueRegexp) {
        this.keyValueRegexp = keyValueRegexp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ConverterData that = (ConverterData) o;

        if (topicName != null ? !topicName.equals(that.topicName) : that.topicName != null) return false;
        return mqttName != null ? mqttName.equals(that.mqttName) : that.mqttName == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (topicName != null ? topicName.hashCode() : 0);
        result = 31 * result + (mqttName != null ? mqttName.hashCode() : 0);
        return result;
    }

    private String getKeyValueRegexpInfo(){
        StringBuilder info = new StringBuilder();
        for(Map.Entry<String,String> keyValue : keyValueRegexp.entrySet()){
            info.append("[Key: ").append(keyValue.getKey()).append(" ---> Regexp: ").append(keyValue.getValue()).append("]").append(System.lineSeparator());
        }
        return info.toString();
    }

    public List<String> getConverterInfo(){
        List<String> converterInfo = new ArrayList<>();
        converterInfo.add("------------------------------------------------"+"\n");
        converterInfo.add("[Mqtt name: "+mqttName+"]"+"\n");
        converterInfo.add("[Topic name: "+topicName+"]"+"\n");
        converterInfo.add(getKeyValueRegexpInfo());
        converterInfo.add("------------------------------------------------");
        return converterInfo;
    }

    @Override
    public String toString() {
        return "------------------------------------------------"+"\n"+
                "[Mqtt name: "+mqttName+"]"+"\n"+
                "[Topic name: "+topicName+"]"+"\n"+
                getKeyValueRegexpInfo()+
                "------------------------------------------------";
    }
}
