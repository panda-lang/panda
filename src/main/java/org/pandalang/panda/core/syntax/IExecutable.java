package org.pandalang.panda.core.syntax;

import org.pandalang.panda.lang.PObject;

public interface IExecutable {

    public PObject run(Parameter instance, Parameter... parameters);

}
