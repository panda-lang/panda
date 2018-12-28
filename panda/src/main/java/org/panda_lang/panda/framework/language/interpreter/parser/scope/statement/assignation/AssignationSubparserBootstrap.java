package org.panda_lang.panda.framework.language.interpreter.parser.scope.statement.assignation;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.UnifiedParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

public abstract class AssignationSubparserBootstrap extends UnifiedParserBootstrap<@Nullable Statement> implements AssignationSubparser {

    @Override
    public final @Nullable Statement parseVariable(ParserData data, Tokens source, Expression expression) throws Throwable {
        data.setComponent(AssignationComponents.EXPRESSION, expression);
        data.setComponent(UniversalComponents.SOURCE_STREAM, new PandaSourceStream(source));
        return parse(data);
    }

}
