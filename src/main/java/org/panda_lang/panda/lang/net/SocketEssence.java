package org.panda_lang.panda.lang.net;

import org.panda_lang.panda.core.statement.Vial;
import org.panda_lang.panda.lang.ObjectEssence;

import java.net.Socket;

public class SocketEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("Socket");
        vial.group("panda.network");
    }

    private final Socket socket;

    public SocketEssence(Socket socket) {
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
