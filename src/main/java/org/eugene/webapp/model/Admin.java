package org.eugene.webapp.model;

import org.springframework.stereotype.Component;

@Component
public class Admin {
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
