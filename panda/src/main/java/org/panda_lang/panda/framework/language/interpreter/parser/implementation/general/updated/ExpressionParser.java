package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.updated;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;

public interface ExpressionParser {

    Expression parse(ParserData data, Tokens expressionsource);

    default @Nullable Expression parseSilently(ParserData data, Tokens source) {
        try {
            return parse(data, source);
        } catch (Throwable throwable) {
            // mute, we don't want to catch any error that comes from ExpressionParser#parse method
        }

        return null;
    }

    @Nullable Tokens read(Tokens source);

}
