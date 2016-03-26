package org.panda_lang.panda.lang.net;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Method;
import org.panda_lang.panda.core.syntax.Vial;
import org.panda_lang.panda.lang.ObjectEssence;
import org.panda_lang.panda.lang.io.ByteBufferEssence;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class SocketChannelEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("SocketChannel");
        vial.group("panda.network");
        vial.method(new Method("write", new Executable() {
            @Override
            public Essence run(Alice alice) {
                SocketChannelEssence socketChannel = alice.getValueOfInstance();
                ByteBufferEssence byteBuffer = alice.getValueOfFactor(0);
                try {
                    socketChannel.getSocketChannel().write(byteBuffer.getByteBuffer());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }));
        vial.method(new Method("read", new Executable() {
            @Override
            public Essence run(Alice alice) {
                SocketChannelEssence socketChannel = alice.getValueOfInstance();
                ByteBufferEssence pByteBuffer = new ByteBufferEssence();
                try {
                    socketChannel.getSocketChannel().read(pByteBuffer.getByteBuffer());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return pByteBuffer;
            }
        }));
    }

    private final SocketChannel socketChannel;

    public SocketChannelEssence(SocketChannel socketChannel) {
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
