package com.olumide.tutorial.jaas;

import java.io.Serializable;
import java.security.Principal;

public class JPrincipal implements Principal, Serializable {

    private String name;

    public JPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean equals(Object object){
        boolean flag = false;
        if(object instanceof JPrincipal)
            flag = name.equals(((JPrincipal) object).getName());
        return flag;
    }

}
