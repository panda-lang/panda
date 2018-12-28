package org.panda_lang.panda.framework.language.interpreter.parser.scope.statement.assignation;

import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.interpreter.parser.component.Component;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;

public class AssignationComponents {

    public static final String SCOPE_LABEL = "assignation-scope";
    public static final Component<Scope> SCOPE = Component.of(SCOPE_LABEL, Scope.class);

    public static final String EXPRESSION_LABEL = "assignation-expression";
    public static final Component<Expression> EXPRESSION = Component.of(EXPRESSION_LABEL, Expression.class);

}
