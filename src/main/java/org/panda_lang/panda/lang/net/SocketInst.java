package org.panda_lang.panda.lang.net;

import org.panda_lang.panda.core.statement.Structure;
import org.panda_lang.panda.lang.ObjectInst;

import java.net.Socket;

public class SocketInst extends ObjectInst {

    static {
        Structure structure = new Structure("Socket");
        structure.group("panda.network");
    }

    private final Socket socket;

    public SocketInst(Socket socket) {
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
