package org.eugene.webapp.core.printer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrintInformation {
    private static List<String> messageBuffer = new ArrayList<>();

    public static void addMessageIntoBuffer(String message){
        messageBuffer.add(message);
    }

    public static void addMessagesIntoBuffer(List<String> messages){
        messageBuffer.addAll(messages);
    }

    public static List<String> getMessageBuffer(){
        return messageBuffer;
    }

    public static void clearMessageBuffer(){
        messageBuffer.clear();
    }

    public static void printCap(){
        String cap = "------------------------------------------------";
        System.out.println(cap);
        addMessageIntoBuffer(cap);
    }

    public static void printFormatInformation(String information){
        String formattingInformation = String.format("%-2s%-45s%-10s","+",information,"+");
        System.out.println(formattingInformation);
        addMessageIntoBuffer(formattingInformation);
        //System.out.printf("%-2s%-45s%-10s%n","+",information,"+");
    }

    public static void printSystemInformation(String information){
        int size = information.length() + 2;
        //System.out.printf("%-14s%-"+size+"s%s%n","[SERVER INFO]",information, new Date());
        String formattingInformation = String.format("%-14s%-"+size+"s%s","[SERVER INFO]",information, new Date());
        System.out.println(formattingInformation);
        addMessageIntoBuffer(formattingInformation);
    }
}
