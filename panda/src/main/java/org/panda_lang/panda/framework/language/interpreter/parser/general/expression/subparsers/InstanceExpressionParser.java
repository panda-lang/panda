package org.panda_lang.panda.framework.language.interpreter.parser.general.expression.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.general.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.general.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.parser.general.expression.subparsers.callbacks.instance.ConstructorExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.general.expression.subparsers.callbacks.instance.InstanceExpressionCallback;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaTokenReader;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

import java.util.List;

class InstanceExpressionParser implements ExpressionSubparser {

    public static final AbyssPattern INSTANCE_PATTERN = new AbyssPatternBuilder()
            .unit(TokenType.KEYWORD, "new")
            .simpleHollow()
            .unit(TokenType.SEPARATOR, "(")
            .simpleHollow()
            .unit(TokenType.SEPARATOR, ")")
            .simpleHollow()
            .build();

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        if (!source.getFirst().contentEquals(Keywords.NEW)) {
           return null;
        }

        Tokens args = SubparserUtils.readBetweenSeparators(source.subSource(2, source.size()), Separators.LEFT_PARENTHESIS_DELIMITER);

        if (args == null) {
            return null;
        }

        return source.subSource(0, 2 + args.size());
    }

    @Override
    public Expression parse(ExpressionParser main, ParserData data, Tokens source) {
        List<Tokens> constructorMatches = INSTANCE_PATTERN.match(new PandaTokenReader(source));

        if (constructorMatches != null && constructorMatches.size() == 3 && constructorMatches.get(2).size() == 0) {
            ConstructorExpressionParser callbackParser = new ConstructorExpressionParser();

            callbackParser.parse(source, data);
            InstanceExpressionCallback callback = callbackParser.toCallback();

            return new PandaExpression(callback.getReturnType(), callback);
        }

        return null;
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.Dynamic.CONSTRUCTOR_CALL;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.INSTANCE;
    }

}
