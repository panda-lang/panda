package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.parser.essential.util.NumberType;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.syntax.Vial;

public class PByte extends Numeric
{

    static
    {
        Vial vial = new Vial("Byte");
        vial.group("panda.lang");
    }

    private final byte b;

    public PByte(byte b)
    {
        this.b = b;
    }

    @Override
    public byte getByte()
    {
        return b;
    }

    @Override
    public Number getNumber()
    {
        return getByte();
    }

    @Override
    public NumberType getNumberType()
    {
        return NumberType.BYTE;
    }

    @Override
    public Object getJavaValue()
    {
        return getByte();
    }

}
