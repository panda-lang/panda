package org.panda_lang.panda.lang.net;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.statement.Constructor;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Vial;
import org.panda_lang.panda.lang.NullEssence;
import org.panda_lang.panda.lang.ObjectEssence;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerSocketEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("ServerSocket");
        vial.group("panda.network");
        vial.constructor(new Constructor() {
            @Override
            public Essence execute(Alice alice) {
                Numeric port = alice.getValueOfFactor(0);
                try {
                    return new ServerSocketEssence(port.getInt());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new NullEssence();
            }
        });
        vial.method(new Method("accept", new Executable() {
            @Override
            public Essence execute(Alice alice) {
                ServerSocketEssence serverSocket = alice.getValueOfInstance();
                try {
                    return new SocketEssence(serverSocket.getServerSocket().accept());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new NullEssence();
            }
        }));
    }

    private final ServerSocket serverSocket;

    public ServerSocketEssence(int port) throws IOException {
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
