package org.panda_lang.panda.lang.net;

import org.panda_lang.panda.core.syntax.Vial;

public class PServerSocketChannel {

    private static final Vial vial;

    static {
        vial = new Vial("ServerSocketChannel");
        vial.group("panda.network");
    }
}
