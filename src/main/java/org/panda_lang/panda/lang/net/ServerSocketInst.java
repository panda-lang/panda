package org.panda_lang.panda.lang.net;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.statement.Constructor;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Structure;
import org.panda_lang.panda.lang.NullInst;
import org.panda_lang.panda.lang.ObjectInst;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerSocketInst extends ObjectInst {

    static {
        Structure structure = new Structure("ServerSocket");
        structure.group("panda.network");
        structure.constructor(new Constructor() {
            @Override
            public Inst execute(Alice alice) {
                Numeric port = alice.getValueOfFactor(0);
                try {
                    return new ServerSocketInst(port.getInt());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new NullInst();
            }
        });
        structure.method(new Method("accept", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                ServerSocketInst serverSocket = alice.getValueOfInstance();
                try {
                    return new SocketInst(serverSocket.getServerSocket().accept());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new NullInst();
            }
        }));
    }

    private final ServerSocket serverSocket;

    public ServerSocketInst(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);

    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    @Override
    public Object getJavaValue() {
        return getServerSocket();
    }

    @Override
    public String toString() {
        return serverSocket.toString();
    }

}
