package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.module.PrimitivePrototypeLiquid;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.old.callbacks.instance.ThisExpressionCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.prototype.ClassPrototypeComponents;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

class LiteralExpressionParser implements ExpressionSubparser {

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        return SubparserUtils.readFirstOfType(source, TokenType.LITERAL);
    }

    @Override
    public Expression parse(ExpressionParser main, ParserData data, Tokens source) {
        TokenRepresentation token = source.get(0);

        switch (token.getTokenValue()) {
            case "null":
                return new PandaExpression(new PandaValue(null, null));
            case "true":
                return toSimpleKnownExpression(PrimitivePrototypeLiquid.BOOLEAN, true);
            case "false":
                return toSimpleKnownExpression(PrimitivePrototypeLiquid.BOOLEAN, false);
            case "this":
                ClassPrototype type = data.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);
                return new PandaExpression(type, new ThisExpressionCallback());
            default:
                throw new PandaParserException("Unknown literal: " + token);
        }
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.SINGLE;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.LITERAL;
    }

}
