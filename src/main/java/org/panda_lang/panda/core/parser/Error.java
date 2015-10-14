package org.panda_lang.panda.core.parser;

import org.panda_lang.panda.Panda;

public class Error {

    private final String message;

    public Error(String message){
        this.message = message;
    }

    public void print(){
        System.out.println("Caused by: " + message);
        System.out.println("Panda Version: " + Panda.PANDA_VERSION);
    }

}
