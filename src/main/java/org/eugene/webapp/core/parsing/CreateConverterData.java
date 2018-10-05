package org.eugene.webapp.core.parsing;

import org.eugene.webapp.core.printer.PrintInformation;
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

public class CreateConverterData {
    private static String pathToScripts;

    public static ConverterData createConverter(String scriptName){
        File xmlFile = new File(pathToScripts+"/"+scriptName);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        ConverterData converterData = null;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            String mqttName = getValueFromSimpleTag(document.getElementsByTagName("mqtt-name"));
            String topicName = getValueFromSimpleTag(document.getElementsByTagName("topic-name"));
            String dataForFormat = getValueFromSimpleTag(document.getElementsByTagName("data-for-format"));

            if(mqttName != null && topicName != null && dataForFormat != null){
                converterData = new ConverterData(topicName,mqttName);
                converterData = setKeyValues(converterData, document.getElementsByTagName("key-values"), dataForFormat);
            }
        } catch (FileNotFoundException e){
            PrintInformation.printSystemInformation("path to the scripts incorrect");
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return converterData;
    }

    public static void setPathToScripts(String path){
        pathToScripts = path;
    }

    private static String getValueFromSimpleTag(NodeList nodeList) {
        if(nodeList.item(0) == null) return null;
        if(nodeList.item(0).getChildNodes().item(0) == null) return null;
        return nodeList.item(0).getChildNodes().item(0).getNodeValue();
    }

    private static ConverterData setKeyValues(ConverterData converterData, NodeList nodeList, String dataForFormat) {
        for (int i=0; i<nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String key = getValueFromTagByName("key", element);
                String beginNonChangeValue = getValueFromTagByName("begin-non-change-value", element);
                String finalNonChangeValue = getValueFromTagByName("final-non-change-value", element);
                if(key != null && beginNonChangeValue != null && finalNonChangeValue != null){
                    if(checkChangeElement(dataForFormat,beginNonChangeValue,finalNonChangeValue)){
                        converterData.put(key,getRegexp(beginNonChangeValue,finalNonChangeValue));
                    }
                }
            }
        }
        return converterData;
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

    private static boolean checkChangeElement(String message, String beginNonChange, String endNonChange){
        int numMatch = 0;
        Pattern pattern = Pattern.compile(beginNonChange+"(.+)"+endNonChange);
        Matcher matcher = pattern.matcher(message);
        while(matcher.find()){
            String match = matcher.group(1);
            System.out.println("changeable element: "+match);
            numMatch++;
        }
        if(numMatch == 1){
            return true;
        }
        return false;
    }

    private static String getRegexp(String beginNonChange, String endNonChange){
        return beginNonChange+"(.+)"+endNonChange;
    }
}
