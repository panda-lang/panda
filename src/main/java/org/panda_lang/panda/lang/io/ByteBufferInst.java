package org.panda_lang.panda.lang.io;

import org.panda_lang.panda.core.statement.Structure;
import org.panda_lang.panda.lang.ObjectInst;

import java.nio.ByteBuffer;

public class ByteBufferInst extends ObjectInst {

    static {
        Structure structure = new Structure("ByteBuffer");
        structure.group("panda.io");
    }

    private ByteBuffer byteBuffer;

    public ByteBufferInst() {
        this.byteBuffer = ByteBuffer.allocate(0);
    }

    public ByteBufferInst(ByteBuffer byteBuffer) {
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
