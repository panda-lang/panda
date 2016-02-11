package org.panda_lang.panda.lang.net;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Method;
import org.panda_lang.panda.core.syntax.Vial;
import org.panda_lang.panda.lang.BooleanEssence;
import org.panda_lang.panda.lang.ObjectEssence;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerSocketChannelEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("ServerSocketChannel");
        vial.group("panda.network");
        vial.constructor(new Executable() {
            @Override
            public Essence run(Particle particle) {
                int port = particle.<Numeric>getValueOfFactor(0).getInt();
                try {
                    return new ServerSocketChannelEssence(port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        vial.method(new Method("accept", new Executable() {
            @Override
            public Essence run(Particle particle) {
                SocketChannel socketChannel = null;
                try {
                    socketChannel = particle.<ServerSocketChannelEssence>getValueOfInstance().getServerSocketChannel().accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new SocketChannelEssence(socketChannel);
            }
        }));
        vial.method(new Method("configureBlocking", new Executable() {
            @Override
            public Essence run(Particle particle) {
                ServerSocketChannelEssence serverSocketChannel = particle.getValueOfInstance();
                BooleanEssence flag = particle.getValueOfFactor(0);
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

    public ServerSocketChannelEssence(int port) throws IOException {
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
