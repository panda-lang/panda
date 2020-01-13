/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.framework.language.interpreter.pattern.custom.elements;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPatternElementBuilder;
import org.panda_lang.framework.language.interpreter.pattern.custom.UniversalData;
import org.panda_lang.framework.language.interpreter.token.PandaSourceStream;

public final class ExpressionElement extends CustomPatternElementBuilder<ExpressionTransaction, ExpressionElement> {

    private @Nullable ExpressionParser customParser;

    private ExpressionElement(String id) {
        super(id);

        super.custom((data, source) -> {
            SourceStream stream = new PandaSourceStream(source.getAvailableSource());
            Context context = data.get(UniversalData.CONTEXT);

            ExpressionParser parser = customParser != null ? customParser : context.getComponent(Components.EXPRESSION);
            ExpressionTransaction expressionTransaction = parser.parse(context, stream);

            source.next(stream.getReadLength());
            return expressionTransaction;
        });
    }

    public ExpressionElement parser(ExpressionParser parser) {
        this.customParser = parser;
        return this;
    }

    public static ExpressionElement create(String id) {
        return new ExpressionElement(id);
    }

}
