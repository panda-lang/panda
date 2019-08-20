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

package org.panda_lang.panda.framework.language.resource.expression.subparsers.assignation.array;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.dynamic.assigner.Assigner;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippetable;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.syntax.auxiliary.Section;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

public class ArrayValueAccessorParser implements Parser {

    public @Nullable Assigner<?> parse(Context context, Snippet source, Expression value) {
        TokenRepresentation sectionRepresentation = source.getLast();

        if (sectionRepresentation.getType() != TokenType.SECTION) {
            return null;
        }

        Section section = sectionRepresentation.toToken();

        if (!section.getSeparator().equals(Separators.SQUARE_BRACKET_LEFT)) {
            return null;
        }

        Snippet instanceSource = source.subSource(0, source.size() - 1);
        ExpressionParser parser = context.getComponent(UniversalComponents.EXPRESSION);
        Expression instance = parser.parse(context, instanceSource);

        return parse(context, source, instance, section, value);
    }

    public @Nullable Assigner<?> parse(Context context, Snippetable source, Expression instance, Section indexSource, Expression value) {
        ExpressionParser parser = context.getComponent(UniversalComponents.EXPRESSION);
        Expression index = parser.parse(context, indexSource.getContent());

        if (!PandaTypes.INT.isAssignableFrom(index.getReturnType())) {
            throw PandaParserFailure.builder("The specified index is not an integer", context)
                    .withStreamOrigin(source)
                    .withNote("Change array index to expression that returns int")
                    .build();
        }

        return ArrayValueAccessorUtils.of(context, source, instance, index, value);
    }

}
