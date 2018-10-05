package org.eugene.webapp.core.commander;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ControllerCommander {
    private Map<String,Commander> commanders = new HashMap<>();
    private final MqttCommander mqttCommander;
    private final UserCommander userCommander;

    @Autowired
    public ControllerCommander(MqttCommander mqttCommander, UserCommander userCommander){
        this.mqttCommander = mqttCommander;
        this.userCommander = userCommander;
        initialCommanders();
    }

    private void initialCommanders(){
        userCommander.loadCommander();
        mqttCommander.loadCommander();
        putCommanders();
    }

    private void putCommanders(){
        commanders.put("mqtt",mqttCommander);
        commanders.put("user",userCommander);
    }

    public Commander getCommander(String name){
        return commanders.get(name);
    }

    public Set<String> getSections(){
        return commanders.keySet();
    }
}
