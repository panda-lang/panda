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

package org.panda_lang.framework.language.interpreter.pattern.custom.elements;

import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.framework.language.interpreter.parser.expression.PandaExpressionParserException;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPatternElementBuilder;
import org.panda_lang.framework.language.interpreter.pattern.custom.UniversalData;
import org.panda_lang.framework.language.resource.syntax.separator.Separators;

import java.util.ArrayList;
import java.util.Collection;

public final class ArgumentsElement extends CustomPatternElementBuilder<ExpressionTransaction[], ArgumentsElement> {

    protected ArgumentsElement(String id) {
        super(id);

        super.custom(((data, source) -> {
            Context context = data.get(UniversalData.CONTEXT);
            ExpressionParser parser = context.getComponent(Components.EXPRESSION);
            Collection<ExpressionTransaction> transactions = new ArrayList<>();

            while (source.hasNext()) {
                try {
                    transactions.add(parser.parse(context, source));
                } catch (PandaExpressionParserException e) {
                    break;
                }

                if (source.hasNext()) {
                    if (!source.getNext().equals(Separators.COMMA)) {
                        break;
                    }

                    source.next();
                }
            }

            return transactions.toArray(new ExpressionTransaction[0]);
        }));
    }

    public static ArgumentsElement create(String id) {
        return new ArgumentsElement(id);
    }

}
