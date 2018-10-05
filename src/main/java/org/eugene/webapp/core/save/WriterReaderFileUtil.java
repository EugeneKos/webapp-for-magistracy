package org.eugene.webapp.core.save;

import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Type;

import static org.eugene.webapp.core.printer.PrintInformation.printSystemInformation;

public class WriterReaderFileUtil {
    private static String pathToDB;

    public static <T> void write(T t, String fileName){
        Gson gson = new Gson();
        String content = gson.toJson(t);
        writeFile(fileName, content);
    }

    public static<T> T read(String fileName, Type type){
        Gson gson = new Gson();
        String content = readFile(fileName);
        T t = gson.fromJson(content,type);
        return t;
    }

    public static void setPathToDB(String path){
        pathToDB = path;
    }

    private static void writeFile(String fileName, String content){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(pathToDB+"/"+fileName+".txt")));
            bufferedWriter.write(content);
            bufferedWriter.close();
            printSystemInformation("write data in "+fileName+" complete");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readFile(String fileName){
        String content = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(pathToDB+"/"+fileName+".txt")));
            content = bufferedReader.readLine();
            bufferedReader.close();
            printSystemInformation("read data from "+fileName+" complete");
        } catch (FileNotFoundException e){
            printSystemInformation("data base "+pathToDB+"/"+fileName+".txt"+" not found");
            createNewFile(pathToDB,fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private static void createNewFile(String pathToDirectory, String fileName){
        File file = new File(pathToDirectory);
        if(!file.exists()){
            if(file.mkdir()){
                printSystemInformation("create new directory "+pathToDirectory);
            } else return;
        }
        try {
            file = new File(pathToDirectory+"/"+fileName+".txt");
            if(file.createNewFile()){
                printSystemInformation("create new data base "+pathToDirectory+"/"+fileName+".txt");
            } else {
                printSystemInformation("data base exist "+pathToDirectory+"/"+fileName+".txt");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
