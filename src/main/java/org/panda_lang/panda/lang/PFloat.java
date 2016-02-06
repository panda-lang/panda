package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.parser.essential.util.NumberType;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.syntax.Vial;

public class PFloat extends Numeric
{

    static
    {
        Vial vial = new Vial("Float");
        vial.group("panda.lang");
    }

    private final float f;

    public PFloat(float f)
    {
        this.f = f;
    }

    @Override
    public float getFloat()
    {
        return f;
    }

    @Override
    public Number getNumber()
    {
        return getFloat();
    }

    @Override
    public NumberType getNumberType()
    {
        return NumberType.FLOAT;
    }

    @Override
    public Object getJavaValue()
    {
        return getFloat();
    }

}
