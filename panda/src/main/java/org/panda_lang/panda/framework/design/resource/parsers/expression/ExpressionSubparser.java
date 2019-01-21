/*
 * Copyright (c) 2015-2019 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.framework.design.resource.parsers.expression;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

public interface ExpressionSubparser extends Comparable<ExpressionSubparser> {

    default void initialize(ParserData data) { }

    @Nullable Tokens read(ExpressionParser main, Tokens source);

    @Nullable Expression parse(ExpressionParser main, ParserData data, Tokens source);

    default @Nullable Expression parseSilently(ExpressionParser main, ParserData data, Tokens source) {
        try {
            return parse(main, data, source);
        } catch (Throwable throwable) {
            // mute, we don't want to catch any error that comes from ExpressionParser#parse method
            return null;
        }
    }

    default Expression toSimpleKnownExpression(ParserData data, String className, Object value) {
        ClassPrototypeReference type = data.getComponent(PandaComponents.PANDA_SCRIPT).getModuleLoader().forClass(className);
        return toSimpleKnownExpression(type.get(), value);
    }

    default Expression toSimpleKnownExpression(ClassPrototype type, Object value) {
        return new PandaExpression(new PandaValue(type, value));
    }

    @Override
    default int compareTo(ExpressionSubparser subparser) {
        return Double.compare(getPriority(), subparser.getPriority());
    }

    default int getMinimumLength() {
        return -1;
    }

    double getPriority();

    String getName();

}
