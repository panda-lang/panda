/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.interpreter.pattern.functional.elements;

import org.panda_lang.language.interpreter.parser.Components;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.language.interpreter.pattern.functional.FunctionalPatternElementBuilder;
import org.panda_lang.language.interpreter.pattern.functional.FunctionalData;
import org.panda_lang.language.interpreter.token.PandaSourceStream;

public final class ExpressionElement extends FunctionalPatternElementBuilder<ExpressionTransaction, ExpressionElement> {

    private ExpressionElement(String id) {
        super(id);

        super.reader((data, source) -> {
            SourceStream stream = new PandaSourceStream(source.getAvailableSource());
            Context context = data.get(FunctionalData.CONTEXT, Context.class).get();

            ExpressionParser parser = context.getComponent(Components.EXPRESSION);
            ExpressionTransaction expressionTransaction = parser.parse(context, stream);

            source.next(stream.getReadLength());
            return expressionTransaction;
        });
    }

    public static ExpressionElement create(String id) {
        return new ExpressionElement(id);
    }

}
