package org.eugene.webapp.core.parsing.filter;

import org.eugene.webapp.core.printer.PrintInformation;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DataFilter содержит ключи, которые задаются отдельно и значения, которые являются строкой регулярного выражения
 * для распарсировки входной строки грязных данных. Второй Map предназначен для хранения ключей из первого и хранения значений распарсированных
 * данных от regexp второго Map.
 */

public class DataFilter {
    private String topicName;
    private String mqttName;
    private Map<String,String> keyValueRegexp = new HashMap<>();
    private Set<DataConverter> converters = new HashSet<>();
    private boolean resolutionPrint;

    public DataFilter(String topicName, String mqttName){
        this.topicName = topicName;
        this.mqttName = mqttName;

    }

    public void addKeyValueRegexp(String key, String value){
        keyValueRegexp.put(key,value);
    }

    public void addConverter(String key, String input, String output){
        converters.add(new DataConverter(key, input,output));
    }

    public Data filter(String dirtyData){
        HashMap<String,String> map = new HashMap<>();
        for (String key : keyValueRegexp.keySet()){
            map.put(key,convert(key, parse(dirtyData,keyValueRegexp.get(key))));
        }
        return new Data(mqttName,topicName,map, dirtyData);
    }

    private String convert(String key, String input){
        for (DataConverter dataConverter : converters){
            if(dataConverter.getKey().equals(key) && dataConverter.isInputMatch(input)){
                return dataConverter.getOutput();
            }
        }
        return input;
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
            PrintInformation.addMessageIntoOperationBuffer("Message ---> "+message);
            PrintInformation.addMessageIntoOperationBuffer("Regexp ---> "+regexp);
            PrintInformation.addMessageIntoOperationBuffer("Match/Value ---> "+match);
            PrintInformation.addMessageIntoOperationBuffer("------------------------------------------------");
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

    public Set<DataConverter> getConverters() {
        return converters;
    }

    public void setConverters(Set<DataConverter> converters) {
        this.converters = converters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DataFilter that = (DataFilter) o;

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
            for (DataConverter dataConverter : converters){
                info.append("Converter: input: ").append(dataConverter.getInput()).append(" ---> output: ").append(dataConverter.getOutput()).append(System.lineSeparator());
            }
        }
        return info.toString();
    }

    private List<String> addKeyValueRegexpInfoIntoList(){
        List<String> filterInfo = new ArrayList<>();
        for(Map.Entry<String,String> keyValue : keyValueRegexp.entrySet()){
            filterInfo.add("[Key: "+keyValue.getKey()+" ---> Regexp: "+keyValue.getValue());
            for (DataConverter dataConverter : converters){
                if(dataConverter.getKey().equals(keyValue.getKey())){
                    filterInfo.add("Converter: input: "+dataConverter.getInput()+" ---> output: "+dataConverter.getOutput());
                }
            }
        }
        return filterInfo;
    }

    public List<String> getFilterInfo(){
        List<String> filterInfo = new ArrayList<>();
        filterInfo.add("------------------------------------------------");
        filterInfo.add("[Mqtt name: "+mqttName+"]");
        filterInfo.add("[Topic name: "+topicName+"]");
        filterInfo.addAll(addKeyValueRegexpInfoIntoList());
        filterInfo.add("------------------------------------------------");
        return filterInfo;
    }

    @Override
    public String toString() {
        return "------------------------------------------------"+System.lineSeparator()+
                "[Mqtt name: "+mqttName+"]"+System.lineSeparator()+
                "[Topic name: "+topicName+"]"+System.lineSeparator()+
                getKeyValueRegexpInfo()+
                "------------------------------------------------";
    }
}
