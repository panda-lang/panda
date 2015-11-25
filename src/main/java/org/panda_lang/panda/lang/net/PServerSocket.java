package org.panda_lang.panda.lang.net;

import org.panda_lang.panda.lang.PObject;

public class PServerSocket extends PObject {

    private final PServerSocket serverSocket;

    public PServerSocket(PServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public PServerSocket getServerSocket() {
        return serverSocket;
    }

    @Override
    public String toString() {
        return serverSocket.toString();
    }

}
