package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.lang.PObject;

public interface IExecutable {

    public PObject run(Parameter instance, Parameter... parameters);

}
