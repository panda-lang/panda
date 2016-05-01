package org.panda_lang.panda.lang.net;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Structure;
import org.panda_lang.panda.lang.ObjectInst;
import org.panda_lang.panda.lang.io.ByteBufferInst;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class SocketChannelInst extends ObjectInst {

    static {
        Structure structure = new Structure("SocketChannel");
        structure.group("panda.network");
        structure.method(new Method("write", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                SocketChannelInst socketChannel = alice.getValueOfInstance();
                ByteBufferInst byteBuffer = alice.getValueOfFactor(0);
                try {
                    socketChannel.getSocketChannel().write(byteBuffer.getByteBuffer());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }));
        structure.method(new Method("read", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                SocketChannelInst socketChannel = alice.getValueOfInstance();
                ByteBufferInst pByteBuffer = new ByteBufferInst();
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

    public SocketChannelInst(SocketChannel socketChannel) {
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
