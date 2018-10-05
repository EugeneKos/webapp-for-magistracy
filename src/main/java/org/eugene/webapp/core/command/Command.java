package org.eugene.webapp.core.command;

import java.util.List;

public interface Command {
    String getName();
    void perform();
    boolean checkArgs(List<String> arguments);
    String getDescriptionCommand();
}
