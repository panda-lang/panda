package org.panda_lang.panda.lang.net;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Structure;
import org.panda_lang.panda.lang.BooleanInst;
import org.panda_lang.panda.lang.ObjectInst;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerSocketChannelInst extends ObjectInst {

    static {
        Structure structure = new Structure("ServerSocketChannel");
        structure.group("panda.network");
        structure.constructor(new Executable() {
            @Override
            public Inst execute(Alice alice) {
                int port = alice.<Numeric> getValueOfFactor(0).getInt();
                try {
                    return new ServerSocketChannelInst(port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        structure.method(new Method("accept", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                SocketChannel socketChannel = null;
                try {
                    socketChannel = alice.<ServerSocketChannelInst> getValueOfInstance().getServerSocketChannel().accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new SocketChannelInst(socketChannel);
            }
        }));
        structure.method(new Method("configureBlocking", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                ServerSocketChannelInst serverSocketChannel = alice.getValueOfInstance();
                BooleanInst flag = alice.getValueOfFactor(0);
                try {
                    serverSocketChannel.getServerSocketChannel().configureBlocking(flag.getBoolean());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }));
    }

    private final ServerSocketChannel serverSocketChannel;

    public ServerSocketChannelInst(int port) throws IOException {
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.bind(new InetSocketAddress(port));
    }

    public ServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }

    @Override
    public Object getJavaValue() {
        return getServerSocketChannel();
    }

}
