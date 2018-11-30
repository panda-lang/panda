package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

public interface ExpressionSubparser extends Comparable<ExpressionSubparser> {

    @Nullable Tokens read(ExpressionParser main, Tokens source);

    Expression parse(ExpressionParser main, ParserData data, Tokens source);

    default @Nullable Expression parseSilently(ExpressionParser main, ParserData data, Tokens source) {
        try {
            return parse(main, data, source);
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

    @Override
    default int compareTo(ExpressionSubparser subparser) {
        return Double.compare(getPriority(), subparser.getPriority());
    }

    double getPriority();

}
