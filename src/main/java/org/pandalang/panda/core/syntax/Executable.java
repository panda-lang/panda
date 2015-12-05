package org.pandalang.panda.core.syntax;

import org.pandalang.panda.lang.PObject;

public interface Executable {

    public PObject run(Parameter... parameters);

    public String getName();

}
