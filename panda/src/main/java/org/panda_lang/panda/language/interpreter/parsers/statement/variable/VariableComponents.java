package org.panda_lang.panda.language.interpreter.parsers.statement.variable;

import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.framework.design.runtime.expression.*;

public class VariableComponents {

    public static Component<Expression> INSTANCE_EXPRESSION = Component.of("panda-variable-instance-expression", Expression.class);

    public static Component<String> INSTANCE_FIELD = Component.of("panda-variable-instance-field", String.class);

}
