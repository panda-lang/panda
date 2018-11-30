package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

public interface ExpressionParser {

    @Nullable Tokens read(Tokens source);

    Expression parse(ParserData data, Tokens source);

    default @Nullable Expression parseSilently(ParserData data, Tokens source) {
        try {
            return parse(data, source);
        } catch (Throwable throwable) {
            // mute, we don't want to catch any error that comes from ExpressionParser#parse method
        }

        return null;
    }

    default Expression toSimpleKnownExpression(ParserData data, String className, Object value) {
        ClassPrototype type = data.getComponent(PandaComponents.PANDA_SCRIPT).getModuleLoader().forClass(className);
        return toSimpleKnownExpression(type, value);
    }

    default Expression toSimpleKnownExpression(ClassPrototype type, Object value) {
        return new PandaExpression(new PandaValue(type, value));
    }

}
