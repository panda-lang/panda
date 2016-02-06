package org.panda_lang.panda.lang.io;

import org.panda_lang.panda.core.syntax.Vial;
import org.panda_lang.panda.lang.PObject;

import java.nio.ByteBuffer;

public class PByteBuffer extends PObject
{

    static
    {
        Vial vial = new Vial("ByteBuffer");
        vial.group("panda.io");
    }

    private ByteBuffer byteBuffer;

    public PByteBuffer()
    {
        this.byteBuffer = ByteBuffer.allocate(0);
    }

    public PByteBuffer(ByteBuffer byteBuffer)
    {
        this.byteBuffer = byteBuffer;
    }

    public ByteBuffer getByteBuffer()
    {
        return byteBuffer;
    }

    @Override
    public Object getJavaValue()
    {
        return getByteBuffer();
    }

}
