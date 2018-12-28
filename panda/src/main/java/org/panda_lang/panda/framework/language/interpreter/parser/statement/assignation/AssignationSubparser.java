package org.panda_lang.panda.framework.language.interpreter.parser.statement.assignation;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;

public interface AssignationSubparser extends Parser {

    @Nullable Statement parseVariable(ParserData data, Tokens source, Expression expression) throws Throwable;

}
