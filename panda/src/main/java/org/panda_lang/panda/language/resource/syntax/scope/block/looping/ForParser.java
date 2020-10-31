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
import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.architecture.expression.PandaExpression;
import org.panda_lang.language.architecture.statement.PandaBlock;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.language.interpreter.parser.PandaSourceReader;
import org.panda_lang.panda.language.resource.syntax.scope.block.BlockParser;
import org.panda_lang.utilities.commons.function.Option;

import java.util.concurrent.CompletableFuture;

public final class ForParser extends BlockParser<ForBlock> {

    private Expression defaultCondition;

    @Override
    public String name() {
        return "for";
    }

    @Override
    public void initialize(Context<?> context) {
        this.defaultCondition = new PandaExpression(context.getTypeLoader().requireType("panda::Bool").getSignature(), true);
        super.initialize(context);
    }

    @Override
    public Option<CompletableFuture<ForBlock>> parse(Context<?> context) {
        PandaSourceReader sourceReader = new PandaSourceReader(context.getStream());

        if (sourceReader.read(Keywords.FOR).isEmpty()) {
            return Option.none();
        }

        Option<Snippet> forArguments = sourceReader.readArguments();

        if (forArguments.isEmpty()) {
            throw new PandaParserFailure(context, context.getSource(), "Missing expression in for loop");
        }

        Snippet[] forEachElements = forArguments.get().split(Separators.SEMICOLON);

        if (forEachElements.length != 3) {
            throw new PandaParserFailure(context, forArguments.get(),
                    "Invalid amount of statements in for loop declaration",
                    "The statement should look like: for (<initialization>; <termination>; <increment>)"
            );
        }

        Scope forExpressionScope = new PandaBlock(context.getScope(), context.getSource());

        Context<?> delegatedContext = context.forkCreator()
                .withScope(forExpressionScope)
                .toContext();

        Snippet initializationSource = forEachElements[0];
        Expression initialization = null;

        if (!initializationSource.isEmpty()) {
            initialization = context.getExpressionParser().parse(delegatedContext, initializationSource).getExpression();
        }

        Snippet terminationSource = forEachElements[1];
        Expression termination = defaultCondition;

        if (!terminationSource.isEmpty()) {
            termination = context.getExpressionParser().parse(delegatedContext, terminationSource).getExpression();
        }

        Snippet incrementSource = forEachElements[2];
        Expression increment = null;

        if (!incrementSource.isEmpty()) {
            increment = context.getExpressionParser().parse(delegatedContext, incrementSource).getExpression();
        }

        ForBlock forBlock = new ForBlock(forExpressionScope, context, initialization, termination, increment);
        context.getScope().addStatement(forBlock);
        SCOPE_PARSER.parse(delegatedContext, forBlock, sourceReader.readBody().get());

        return Option.of(CompletableFuture.completedFuture(forBlock));
    }

}
