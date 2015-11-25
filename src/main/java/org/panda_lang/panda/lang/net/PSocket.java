package org.panda_lang.panda.lang.net;

import org.panda_lang.panda.lang.PObject;

import java.net.Socket;

public class PSocket extends PObject {

    private final Socket socket;

    public PSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public String toString() {
        return socket.toString();
    }

}
