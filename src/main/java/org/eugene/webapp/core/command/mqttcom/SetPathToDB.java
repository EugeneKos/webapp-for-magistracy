package org.eugene.webapp.core.command.mqttcom;

import org.eugene.webapp.core.command.Command;
import org.eugene.webapp.core.mqtt.MqttConnectOperations;
import org.eugene.webapp.core.save.WriterReaderFileUtil;

import java.util.List;

import static org.eugene.webapp.core.printer.PrintInformation.printSystemInformation;

public class SetPathToDB extends TotalMqttCom implements Command {
    public SetPathToDB(MqttConnectOperations mqttConnectOperations) {
        super(mqttConnectOperations);
    }

    @Override
    public String getName() {
        return "db-path";
    }

    @Override
    public void perform() {
        WriterReaderFileUtil.setPathToDB(arguments.get(0));
        printSystemInformation("change path to data base: "+arguments.get(0));
    }

    @Override
    public boolean checkArgs(List<String> arguments) {
        if(arguments.size() == 1){
            super.arguments = arguments;
            return true;
        }
        return false;
    }

    @Override
    public String getDescriptionCommand() {
        return "db-path [path to data base]";
    }
}
