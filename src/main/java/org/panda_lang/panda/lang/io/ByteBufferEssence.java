package org.panda_lang.panda.lang.io;

import org.panda_lang.panda.core.statement.Vial;
import org.panda_lang.panda.lang.ObjectEssence;

import java.nio.ByteBuffer;

public class ByteBufferEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("ByteBuffer");
        vial.group("panda.io");
    }

    private ByteBuffer byteBuffer;

    public ByteBufferEssence() {
        this.byteBuffer = ByteBuffer.allocate(0);
    }

    public ByteBufferEssence(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

    @Override
    public Object getJavaValue() {
        return getByteBuffer();
    }

}
