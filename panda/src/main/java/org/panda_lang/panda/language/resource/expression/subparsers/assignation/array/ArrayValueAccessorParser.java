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

package org.panda_lang.panda.language.resource.expression.subparsers.assignation.array;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.token.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.Snippetable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.language.architecture.prototype.array.ArrayClassPrototype;
import org.panda_lang.panda.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.language.resource.PandaTypes;
import org.panda_lang.panda.language.resource.syntax.auxiliary.Section;
import org.panda_lang.panda.language.resource.syntax.separator.Separators;

public final class ArrayValueAccessorParser implements Parser {

    public @Nullable ArrayAccessor parse(Context context, Snippet source) {
        TokenRepresentation sectionRepresentation = source.getLast();

        if (sectionRepresentation == null || sectionRepresentation.getType() != TokenType.SECTION) {
            return null;
        }

        Section section = sectionRepresentation.toToken();

        if (!section.getSeparator().equals(Separators.SQUARE_BRACKET_LEFT)) {
            return null;
        }

        Snippet instanceSource = source.subSource(0, source.size() - 1);
        ExpressionParser parser = context.getComponent(UniversalComponents.EXPRESSION);
        Expression instance = parser.parse(context, instanceSource).getExpression();

        return parse(context, source, instance, section);
    }

    public ArrayAccessor parse(Context context, Snippetable source, Expression instance, Section indexSource) {
        ExpressionParser parser = context.getComponent(UniversalComponents.EXPRESSION);
        Expression index = parser.parse(context, indexSource.getContent()).getExpression();

        if (!PandaTypes.INT.isAssignableFrom(index.getReturnType())) {
            throw PandaParserFailure.builder("The specified index is not an integer", context)
                    .withStreamOrigin(source)
                    .withNote("Change array index to expression that returns int")
                    .build();
        }

        return of(context, source, instance, index);
    }

    public ArrayAccessor of(Context context, Snippetable source, Expression instance, Expression index) {
        if (!instance.getReturnType().isArray()) {
            throw PandaParserFailure.builder("Cannot use index on non-array type (" + instance.getReturnType() + ")", context)
                    .withStreamOrigin(source)
                    .build();
        }

        ArrayClassPrototype arrayPrototype = (ArrayClassPrototype) instance.getReturnType();

        if (arrayPrototype == null) {
            throw PandaParserFailure.builder("Cannot locate array class", context)
                    .withStreamOrigin(source)
                    .build();
        }

        return new ArrayAccessor(instance, index);
    }

}
