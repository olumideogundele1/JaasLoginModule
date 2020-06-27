package com.olumide.tutorial.jaas;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.PrivilegedAction;

public class Driver {

    public enum Action{action1,action2,logout};
    public static void main(String[] args) throws LoginException {

        System.setProperty("java.security.auth.login.config","jaas.config");
        LoginContext loginContext = null;
        while(true){
            try{
                loginContext = new LoginContext("JaasTutorial",new JCallBackHandler());
                loginContext.login();
                boolean flag  = true;
                while(flag) flag = performAction(loginContext);
            }catch (LoginException | IOException e){
                System.out.println(e.getMessage());
            }
        }

    }

    static boolean performAction(LoginContext loginContext) throws IOException, LoginException {
        boolean flag = true;
        System.out.println("Please specify action to take (usage: action1, action2,logout)");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            switch (Action.valueOf(br.readLine())) {
                case logout:
                    loginContext.logout();
                    System.out.println("You are now logged out...");
                    flag = false;
                    break;
                case action1:
                    PrivilegedAction<Object> privilegedAction = () -> {
                        System.out.println("Action 1 was performed...");
                        return null;
                    };
                    Subject.doAs(loginContext.getSubject(), privilegedAction);
                    System.out.println(" by " + loginContext.getSubject().getPrincipals().iterator().next().getName());
                    break;
                case action2:
                    PrivilegedAction<Object> privilegedAction1 = () -> {
                        System.out.println("Action 2 was performed...");
                        return null;
                    };
                    Subject.doAs(loginContext.getSubject(), privilegedAction1);
                    System.out.println(" by " + loginContext.getSubject().getPrincipals().iterator().next().getName());
                    break;
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid entry....");
        }
        return flag;

    }

}
