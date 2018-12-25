package org.panda_lang.panda.framework.language.interpreter.parser.general.expression.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.general.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.general.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

class SectionExpressionSubparser implements ExpressionSubparser {

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        return SubparserUtils.readBetweenSeparators(source, Separators.LEFT_PARENTHESIS_DELIMITER);
    }

    @Override
    public @Nullable Expression parse(ExpressionParser main, ParserData data, Tokens source) {
        return main.parse(data, source.subSource(1, source.size() - 1));
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.DYNAMIC;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.SECTION;
    }

}
