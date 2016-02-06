package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.parser.essential.util.NumberType;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.syntax.Vial;

public class PInt extends Numeric
{

    static
    {
        Vial vial = new Vial("Int");
        vial.group("panda.lang");
    }

    private final int i;

    public PInt(int i)
    {
        this.i = i;
    }

    @Override
    public int getInt()
    {
        return i;
    }

    @Override
    public Number getNumber()
    {
        return getInt();
    }

    @Override
    public NumberType getNumberType()
    {
        return NumberType.INT;
    }

    @Override
    public Object getJavaValue()
    {
        return getInt();
    }

}
