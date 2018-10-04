package org.eugene.webapp.core.command.usercom;

import org.eugene.webapp.core.user.UserOperation;

import java.util.List;

public class TotalUserCom {
    protected List<String> arguments;
    protected UserOperation userOperation;

    public TotalUserCom(UserOperation userOperation) {
        this.userOperation = userOperation;
    }
}
