package org.pandalang.panda.core.parser.improved;

import org.pandalang.panda.Panda;

public class PandaException {

    private String title;
    private String code;
    private int line;
    private int caret;


    public PandaException(String title, String code, int line, int caret) {
        this.title = title;
        this.code = code;
        this.line = line;
        this.caret = caret;
    }

    public void print() {
        System.out.println("[Panda] !# Oh no, Panda " + Panda.PANDA_VERSION + " is sad! Why? Because she caught an exception: " + title);
        System.out.println("[Panda] !# at line: " + code + " [" + line + ":" + caret + "]");
    }

}
