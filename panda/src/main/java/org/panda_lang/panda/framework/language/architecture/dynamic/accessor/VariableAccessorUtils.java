package org.panda_lang.panda.framework.language.architecture.dynamic.accessor;

import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;

public class VariableAccessorUtils {

    public static VariableAccessor of(ParserData data, Scope scope, Variable variable, Expression expression) {
        if (!variable.getType().isAssignableFrom(expression.getReturnType())) {
            throw new PandaParserFailure("Cannot assign " + expression.getReturnType().getClassName() + " to " + variable.getType().getClassName() + " variable", data);
        }

        return new VariableAccessor(variable, scope.indexOf(variable), expression);
    }

}
