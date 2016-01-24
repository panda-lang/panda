package org.panda_lang.panda.lang.net;

import org.panda_lang.panda.core.syntax.Vial;
import org.panda_lang.panda.lang.PObject;

import java.net.Socket;

public class PSocket extends PObject {

    private static final Vial vial;

    static {
        vial = new Vial("Socket");
        vial.group("panda.network");
    }

    private final Socket socket;

    public PSocket(Socket socket) {
        super(vial);
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public Object getJavaValue() {
        return getSocket();
    }

    @Override
    public String toString() {
        return socket.toString();
    }

}
