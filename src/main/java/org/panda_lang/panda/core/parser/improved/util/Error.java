package org.panda_lang.panda.core.parser.improved.util;

import org.panda_lang.panda.Panda;

public class Error {

    private String title;
    private String code;
    private int line;
    private int caret;


    public Error(String title, String code, int line, int caret){
        this.title = title;
        this.code = code;
        this.line = line;
        this.caret = caret;
    }

    public void print(){
        System.out.println("Oh no, Panda " + Panda.PANDA_VERSION + " is sad! Why? Because she caught an exception: " + title);
        System.out.println("at " + caret + " -> line " + line + ": " + code);
    }

}
