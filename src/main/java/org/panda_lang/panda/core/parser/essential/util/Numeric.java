package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.lang.PObject;

public interface Numeric {

    public Numeric add(Numeric numeric);
    public Numeric subtract(Numeric numeric);
    public Numeric multiply(Numeric numeric);
    public Numeric divide(Numeric numeric);
    public NumberType getNumberType();
    public Object getNumber();

}
