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

package org.panda_lang.panda.language.resource.syntax.scope.block.looping;

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.statement.PandaVariable;
import org.panda_lang.language.architecture.statement.VariableData;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.PandaParserException;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.language.resource.syntax.operator.Operators;
import org.panda_lang.panda.language.interpreter.parser.PandaSourceReader;
import org.panda_lang.panda.language.resource.syntax.scope.block.BlockParser;
import org.panda_lang.panda.language.resource.syntax.scope.variable.VariableDataInitializer;
import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.Option;

public final class ForEachParser extends BlockParser<ForEachBlock> {

    @Override
    public String name() {
        return "foreach";
    }

    @Override
    public Option<Completable<ForEachBlock>> parse(Context<?> context) {
        PandaSourceReader sourceReader = new PandaSourceReader(context.getStream());

        if (sourceReader.read(Keywords.FOREACH).isEmpty()) {
            return Option.none();
        }

        Option<Snippet> forEachArguments = sourceReader.readArguments();

        if (forEachArguments.isEmpty()) {
            throw new PandaParserFailure(context, context.getSource(), "Missing expression in for-each loop");
        }

        Snippet[] elements = forEachArguments.get().split(Operators.COLON);

        if (elements.length != 2) {
            throw new PandaParserFailure(context, forEachArguments.get(),
                    "Invalid amount of statements in for each loop declaration", "The statement should look like: foreach (<value> : <source>)"
            );
        }

        Expression iterableExpression = context.getExpressionParser().parse(context, elements[1]);

        if (!context.getTypeLoader().requireType("panda::Iterable").isAssignableFrom(iterableExpression.getKnownType())) {
            throw new PandaParserException("ForEach requires Iterable value");
        }

        ForEachBlock forEach = new ForEachBlock(context.getScope(), context, iterableExpression);
        VariableDataInitializer dataInitializer = new VariableDataInitializer(context, forEach);
        VariableData variableData = dataInitializer.createVariableDataByDeclaration(elements[0], true, true);

        PandaVariable forVariable = new PandaVariable(forEach.getValuePointer(), variableData);
        forEach.addVariable(forVariable);

        context.getScope().addStatement(forEach);
        SCOPE_PARSER.parse(context, forEach, sourceReader.readBody().get());

        return Option.ofCompleted(forEach);
    }

}
