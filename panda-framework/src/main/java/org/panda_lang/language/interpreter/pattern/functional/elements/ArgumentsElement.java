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

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionParserException;
import org.panda_lang.language.interpreter.pattern.functional.FunctionalPatternElementBuilder;
import org.panda_lang.language.interpreter.pattern.functional.FunctionalData;
import org.panda_lang.language.resource.syntax.separator.Separators;

import java.util.ArrayList;
import java.util.Collection;

public final class ArgumentsElement extends FunctionalPatternElementBuilder<Expression[], ArgumentsElement> {

    protected ArgumentsElement(String id) {
        super(id);

        super.reader(((data, source) -> {
            Context<?> context = data.get(FunctionalData.CONTEXT, Context.class).get();
            ExpressionParser parser = context.getExpressionParser();
            Collection<Expression> transactions = new ArrayList<>();

            while (source.hasNext()) {
                try {
                    transactions.add(parser.parse(context, source));
                } catch (ExpressionParserException e) {
                    break;
                }

                if (source.hasNext()) {
                    if (!source.getNext().equals(Separators.COMMA)) {
                        break;
                    }

                    source.next();
                }
            }

            return transactions.toArray(new Expression[0]);
        }));
    }

    public static ArgumentsElement create(String id) {
        return new ArgumentsElement(id);
    }

}
