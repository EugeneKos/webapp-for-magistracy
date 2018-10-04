package org.eugene.webapp.core.printer;

import java.util.Date;

public class PrintInformation {
    public static void printCap(){
        System.out.println("------------------------------------------------");
    }

    public static void printFormatInformation(String information){
        System.out.printf("%-2s%-45s%-10s%n","+",information,"+");
    }

    public static void printSystemInformation(String information){
        int size = information.length() + 2;
        System.out.printf("%-14s%-"+size+"s%s%n","[SERVER INFO]",information, new Date());
    }
}
