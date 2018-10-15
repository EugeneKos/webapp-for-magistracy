package org.eugene.webapp.core.filter;

import org.eugene.webapp.core.utils.PrintInformation;
import org.eugene.webapp.core.user.User;

import javax.persistence.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DataFilter содержит ключи, которые задаются отдельно и значения, которые являются строкой регулярного выражения
 * для распарсировки входной строки грязных данных. Второй Map предназначен для хранения ключей из первого и хранения значений распарсированных
 * данных от regexp второго Map.
 */

@Entity
@Table(name = "filters")
public class DataFilter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "topic")
    private String topicName;
    @Column(name = "mqtt_name")
    private String mqttName;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "dataFilter")
    private Set<KeyValueRegexp> keyValueRegexps = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "dataFilter")
    private Set<DataConverter> converters = new HashSet<>();
    @ManyToMany(mappedBy = "filters", fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();
    @Transient
    private boolean resolutionPrint;

    public DataFilter(String name, String topicName, String mqttName){
        this.name = name;
        this.topicName = topicName;
        this.mqttName = mqttName;

    }

    public DataFilter() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addKeyValueRegexp(String key, String value){
        KeyValueRegexp keyValueRegexp = new KeyValueRegexp(key,value);
        keyValueRegexp.setDataFilter(this);
        keyValueRegexps.add(keyValueRegexp);

    }

    public void addConverter(String key, String input, String output){
        DataConverter dataConverter = new DataConverter(key,input,output);
        dataConverter.setDataFilter(this);
        converters.add(dataConverter);
    }

    public Data filter(String dirtyData){
        HashMap<String,String> map = new HashMap<>();
        for (KeyValueRegexp keyValueRegexp : keyValueRegexps){
            map.put(keyValueRegexp.getKey(),convert(keyValueRegexp.getKey(), parse(dirtyData,keyValueRegexp.getValue())));
        }
        return new Data(mqttName,topicName,map, dirtyData);
    }

    private String convert(String key, String input){
        for (DataConverter dataConverter : converters){
            if(dataConverter.getKey().equals(key) && dataConverter.isContains(input)){
                input = dataConverter.getConvertValue(input);
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

    public String getName() {
        return name;
    }

    public String getMqttName(){
        return mqttName;
    }

    public String getTopicName(){
        return topicName;
    }

    public Set<KeyValueRegexp> getKeyValueRegexps() {
        return keyValueRegexps;
    }

    public void setKeyValueRegexps(Set<KeyValueRegexp> keyValueRegexps) {
        this.keyValueRegexps = keyValueRegexps;
    }

    public Set<DataConverter> getConverters() {
        return converters;
    }

    public void setConverters(Set<DataConverter> converters) {
        this.converters = converters;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataFilter that = (DataFilter) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    private String getKeyValueRegexpInfo(){
        StringBuilder info = new StringBuilder();
        for(KeyValueRegexp keyValueRegexp : keyValueRegexps){
            info.append("[Key: ").append(keyValueRegexp.getKey()).append(" ---> Regexp: ").append(keyValueRegexp.getValue()).append("]").append(System.lineSeparator());
            for (DataConverter dataConverter : converters){
                if(dataConverter.getKey().equals(keyValueRegexp.getKey())){
                    info.append("Converter: input: ").append(dataConverter.getInput()).append(" ---> output: ").append(dataConverter.getOutput()).append(System.lineSeparator());
                }
            }
        }
        return info.toString();
    }

    private List<String> addKeyValueRegexpInfoIntoList(){
        List<String> filterInfo = new ArrayList<>();
        for(KeyValueRegexp keyValueRegexp : keyValueRegexps){
            filterInfo.add("[Key: "+keyValueRegexp.getKey()+" ---> Regexp: "+keyValueRegexp.getValue());
            for (DataConverter dataConverter : converters){
                if(dataConverter.getKey().equals(keyValueRegexp.getKey())){
                    filterInfo.add("Converter: input: "+dataConverter.getInput()+" ---> output: "+dataConverter.getOutput());
                }
            }
        }
        return filterInfo;
    }

    public List<String> getFilterInfo(){
        List<String> filterInfo = new ArrayList<>();
        filterInfo.add("------------------------------------------------");
        filterInfo.add("[Name: "+name+"]");
        filterInfo.add("[Mqtt name: "+mqttName+"]");
        filterInfo.add("[Topic name: "+topicName+"]");
        filterInfo.addAll(addKeyValueRegexpInfoIntoList());
        filterInfo.add("------------------------------------------------");
        return filterInfo;
    }

    @Override
    public String toString() {
        return "------------------------------------------------"+System.lineSeparator()+
                "[Name: "+name+"]"+System.lineSeparator()+
                "[Mqtt name: "+mqttName+"]"+System.lineSeparator()+
                "[Topic name: "+topicName+"]"+System.lineSeparator()+
                getKeyValueRegexpInfo()+
                "------------------------------------------------";
    }
}
