package org.panda_lang.panda.lang.net;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Method;
import org.panda_lang.panda.core.syntax.Vial;
import org.panda_lang.panda.lang.PObject;

import java.nio.channels.SocketChannel;

public class PSocketChannel extends PObject {

    private static final Vial vial;

    static {
        vial = new Vial("SocketChannel");
        vial.group("panda.network");
        vial.method(new Method("write", new Executable() {
            @Override
            public Essence run(Particle particle) {
                //TODO: write
                return null;
            }
        }));
        vial.method(new Method("read", new Executable() {
            @Override
            public Essence run(Particle particle) {
                //TODO: read
                return null;
            }
        }));
    }

    private final SocketChannel socketChannel;

    public PSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    @Override
    public Object getJavaValue() {
        return getSocketChannel();
    }

}
