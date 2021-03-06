package org.eugene.webapp.core.utils;

import org.eugene.webapp.core.model.device.Device;
import org.eugene.webapp.core.model.filter.DataFilter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptCreator {
    private static String pathToScripts;

    private ScriptCreator(){}

    private static Document getDocument(String scriptName){
        File xmlFile = new File(pathToScripts+File.separator+scriptName);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();
        } catch (FileNotFoundException e){
            PrintInformation.printSystemInformation("path to the scripts incorrect");
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return document;
    }

    public static void setPathToScripts(String path){
        pathToScripts = path;
    }

    public static String getPathToScripts() {
        return pathToScripts;
    }

    private static Element getElement(NodeList nodeList){
        if(nodeList.item(0) == null) return null;
        return (Element)nodeList.item(0);
    }

    private static String getValueFromSimpleTag(NodeList nodeList) {
        if(nodeList.item(0) == null) return null;
        if(nodeList.item(0).getChildNodes().item(0) == null) return null;
        return nodeList.item(0).getChildNodes().item(0).getNodeValue();
    }

    private static String getValueFromTagByName(String nameTag, Element element) {
        if(element.getElementsByTagName(nameTag).item(0) == null) return null;
        NodeList nodeList = element.getElementsByTagName(nameTag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        if(node != null){
            return node.getNodeValue();
        }
        return null;
    }

    //----------------------------------filters------------------------------------------------------------

    public static DataFilter createDataFilter(String scriptName){
        DataFilter dataFilter = null;
        Document document = getDocument(scriptName);
        if(document == null) return null;
        Element mqtt = getElement(document.getElementsByTagName("mqtt"));
        String mqttName = "";
        String topicName = "";
        if(mqtt != null){
            mqttName = mqtt.getAttribute("name");
            topicName = mqtt.getAttribute("topic");
        }
        String name = getValueFromSimpleTag(document.getElementsByTagName("name"));
        String dataForFormat = getValueFromSimpleTag(document.getElementsByTagName("data-for-format"));
        if(!mqttName.equals("") && !topicName.equals("") && dataForFormat != null && name != null){
            dataFilter = new DataFilter(name,topicName,mqttName);
            dataFilter = setKeyValues(dataFilter, document.getElementsByTagName("key-values"), dataForFormat);
        }
        return dataFilter;
    }

    private static DataFilter setConverters(DataFilter dataFilter, NodeList nodeList, String key){
        for (int i=0; i<nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                dataFilter.addConverter(key, element.getAttribute("input"),element.getAttribute("output"));
            }
        }
        return dataFilter;
    }

    private static DataFilter setKeyValues(DataFilter dataFilter, NodeList nodeList, String dataForFormat) {
        for (int i=0; i<nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String key = getValueFromTagByName("key", element);
                String beginNonChangeValue = getValueFromTagByName("begin-non-change-value", element);
                String finalNonChangeValue = getValueFromTagByName("final-non-change-value", element);
                if(key != null){
                    if(checkChangeElement(dataForFormat,beginNonChangeValue,finalNonChangeValue)){
                        dataFilter.addKeyValueRegexp(key,getRegexp(beginNonChangeValue,finalNonChangeValue));
                        dataFilter = setConverters(dataFilter,element.getElementsByTagName("converter"),key);
                    }
                }
            }
        }
        return dataFilter;
    }

    private static boolean checkChangeElement(String message, String beginNonChange, String endNonChange){
        int numMatch = 0;
        Pattern pattern;
        if(beginNonChange != null && endNonChange == null){
            pattern = Pattern.compile(beginNonChange+"(.+)");
        } else if(beginNonChange == null && endNonChange != null) {
            pattern = Pattern.compile("(.+)"+endNonChange);
        } else if(beginNonChange != null){ // Оба не равны null
            pattern = Pattern.compile(beginNonChange+"(.+)"+endNonChange);
        } else {
            return true;
        }
        Matcher matcher = pattern.matcher(message);
        while(matcher.find()){
            String match = matcher.group(1);
            System.out.println("changeable element: "+match);
            numMatch++;
        }
        return numMatch == 1;
    }

    private static String getRegexp(String beginNonChange, String endNonChange){
        if(beginNonChange != null && endNonChange == null){
            return beginNonChange+"(.+)";
        } else if(beginNonChange == null && endNonChange != null) {
            return "(.+)"+endNonChange;
        } else if (beginNonChange != null){ // Оба не равны null
            return beginNonChange+"(.+)"+endNonChange;
        } else {
            return "(.+)";
        }
    }

    //-----------------------------------------------------------------------------------------------------------

    //-------------------------------------devices---------------------------------------------------------------

    public static Device createDevice(String scriptName){
        Document document = getDocument(scriptName);
        Device device = null;
        if(document == null) return null;
        Element deviceElement = getElement(document.getElementsByTagName("device"));
        String deviceName = "";
        String deviceDescription = "";
        if(deviceElement != null){
            deviceName = deviceElement.getAttribute("name");
            deviceDescription = deviceElement.getAttribute("description");
        }
        Element mqtt = getElement(document.getElementsByTagName("mqtt"));
        String mqttName = "";
        String topicName = "";
        if(mqtt != null){
            mqttName = mqtt.getAttribute("name");
            topicName = mqtt.getAttribute("topic");
        }
        if(!deviceName.equals("") && !deviceDescription.equals("") && !mqttName.equals("") && !topicName.equals("")){
            device = new Device(deviceName,deviceDescription,mqttName,topicName);
            device = setCommands(device,document.getElementsByTagName("command"));
        }
        return device;
    }

    private static Device setCommands(Device device, NodeList nodeList) {
        for (int i=0; i<nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String commandName = element.getAttribute("name");
                String params = element.getAttribute("params");
                if(params.equals("")) params = " ";
                String commandDescription = getValueFromTagByName("description", element);
                String commandText = getValueFromTagByName("command-text", element);
                if(!commandName.equals("") && commandDescription != null && commandText != null){
                    device.addControlCommand(commandName,params,commandDescription,commandText);
                }
            }
        }
        return device;
    }

    //-----------------------------------------------------------------------------------------------------------
}
