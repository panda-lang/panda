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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.interpreter.parser.Components;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.token.PandaSourceStream;
import org.panda_lang.panda.language.interpreter.parser.autowired.AutowiredParser;

public abstract class AutowiredAssignationParser extends AutowiredParser<@Nullable ExpressionResult> implements AssignationSubparser<ExpressionResult> {

    @Override
    public final @Nullable ExpressionResult parseAssignment(Context context, Snippet declaration, TokenInfo operator, Expression expression) {
        context.withComponent(Components.STREAM, new PandaSourceStream(declaration));
        context.withComponent(AssignationComponents.EXPRESSION, expression);

        context.withComponent(AssignationComponents.OPERATOR, operator);
        context.withComponent(AssignationComponents.TYPE, AssignationType.of(operator.toToken()).orThrow(() -> {
            throw new PandaParserFailure(context, operator, "Unknown assignation operator");
        }));

        return parse(context);
    }

}
