package me.yarinlevi.qprivatevoices;

import javax.security.auth.login.LoginException;

/**
 * @author YarinQuapi
 */
public class Main {
    public static void main(String[] args) {
        try {
            new QPrivateVoices();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}
