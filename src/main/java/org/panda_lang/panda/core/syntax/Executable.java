package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.lang.PObject;

public interface Executable {

    public PObject run(Parameter... parameters);
    public String getName();

}
