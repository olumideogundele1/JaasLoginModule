package com.olumide.tutorial.jaas;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.util.Map;

public class JLoginModule implements LoginModule {
    public static final String[][] TEST_USERS = {{"user1","password1"},{"user2","password2"},{"user","password3"}};

    private Subject subject = null;
    private CallbackHandler callbackHandler = null;
    private JPrincipal principal  = null;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> map, Map<String, ?> map1) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        System.out.println("Intializing.......");
    }

    @Override
    public boolean login() throws LoginException {
        boolean flag = false;
        System.out.println("Login in .......");

        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("Username:");
        callbacks[1] = new PasswordCallback("Password:",false);
        try{
            callbackHandler.handle(callbacks);
            String name = ((NameCallback) callbacks[0]).getName();
            String password = new String(((PasswordCallback) callbacks[1]).getPassword());
            int i = 0;
            while(i < TEST_USERS.length){
                if(TEST_USERS[i][0].equals(name) && TEST_USERS[i][1].equals(password)){
                    principal = new JPrincipal(name);
                    System.out.println("Authentication Successful....");
                    flag = true;
                    break;
                }
                i++;
            }
            if(flag == false) throw new FailedLoginException("Authentication Failure....");
        }catch (IOException | UnsupportedCallbackException e){
            System.out.println("Error "+e.getMessage());
        }

        return flag;
    }

    @Override
    public boolean commit() throws LoginException {
        boolean flag = false;
        if(subject != null && !subject.getPrincipals().contains(principal)){
            subject.getPrincipals().add(principal);
            flag = true;
        }
        System.out.println("Commiting .......");
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        System.out.println("Aborting .......");
        if(subject != null && principal != null && subject.getPrincipals().contains(principal))
            subject.getPrincipals().remove(principal);
        subject = null;
        principal = null;
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        System.out.println("Log out ...........");
        subject.getPrincipals().remove(principal);
        subject = null;
        return true;
    }
}
