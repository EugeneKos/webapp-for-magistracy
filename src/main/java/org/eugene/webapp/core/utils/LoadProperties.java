package org.eugene.webapp.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadProperties {
    private LoadProperties(){}

    public static void loadProperties(){
        try {
            InputStream inputStream = LoadProperties.class.getClassLoader().getResourceAsStream("config.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            setScriptPath(properties.getProperty("scriptPath"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setScriptPath(String path){
        File file = new File(path);
        if(file.exists()){
            ScriptCreator.setPathToScripts(path);
        }
    }
}
