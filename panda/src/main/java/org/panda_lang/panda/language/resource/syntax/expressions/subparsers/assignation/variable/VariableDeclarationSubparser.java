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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.variable;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.architecture.statement.Variable;
import org.panda_lang.framework.design.architecture.statement.VariableData;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Channel;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Handler;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.architecture.prototype.utils.TypeDeclarationUtils;
import org.panda_lang.framework.language.architecture.statement.PandaVariableDataInitializer;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Inter;
import org.panda_lang.panda.language.interpreter.parser.PandaPipeline;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.AssignationPriorities;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.AssignationSubparserBootstrap;

import java.util.Objects;
import java.util.Optional;

@RegistrableParser(pipeline = PandaPipeline.ASSIGNER_LABEL, priority = AssignationPriorities.VARIABLE_DECLARATION)
public final class VariableDeclarationSubparser extends AssignationSubparserBootstrap {

    @Override
    protected BootstrapInitializer<@Nullable ExpressionResult> initialize(Context context, BootstrapInitializer<@Nullable ExpressionResult> initializer) {
        return initializer;
    }

    @Override
    protected Object customHandle(Handler handler, Context context, Channel channel, Snippet source) {
        if (source.size() < 2) {
            return false;
        }

        TokenRepresentation name = Objects.requireNonNull(source.getLast());

        if (name.getType() != TokenTypes.UNKNOWN) {
            return false;
        }

        Optional<Snippet> typeValue = TypeDeclarationUtils.readTypeBackwards(source.subSource(0, source.size() - 1));

        if (!typeValue.isPresent()) {
            return false;
        }

        Snippet type = typeValue.get();
        Snippet modifiers = source.subSource(0, source.size() - 1 - type.size());

        // max amount of modifiers: mut, nil
        if (modifiers.size() > 2) {
            return false;
        }

        int mutable = modifiers.indexOf(Keywords.MUT);

        if (mutable != Snippet.NOT_FOUND) {
            modifiers.remove(mutable);
        }

        int nillable = modifiers.indexOf(Keywords.NIL);

        if (nillable != Snippet.NOT_FOUND) {
            modifiers.remove(nillable);
        }

        if (modifiers.size() > 0) {
            return false;
        }

        channel.put("elements", new Elements(type, name, mutable != Snippet.NOT_FOUND, nillable != Snippet.NOT_FOUND));
        return true;
    }

    @Autowired
    ExpressionResult parse(
            Context context,
            @Component Scope scope, @Component Channel channel, @Component Expression expression, @Component ExpressionContext expressionContext,
            @Inter SourceLocation location
    ) {
        Elements elements = channel.get("elements", Elements.class);
        PandaVariableDataInitializer dataInitializer = new PandaVariableDataInitializer(context, scope);
        VariableData variableData = dataInitializer.createVariableData(elements.type, elements.name, elements.mutable, elements.nillable);

        Variable variable = scope.createVariable(variableData);
        expressionContext.commit(() -> scope.removeVariable(variable.getName()));

        if (!variable.getType().isAssignableFrom(expression.getReturnType())) {
            throw new PandaParserFailure(context,
                    "Cannot assign " + expression.getReturnType().getSimpleName() + " to " + variable.getType().getSimpleName(),
                    "Change variable type or ensure the expression has compatible return type"
            );
        }

        return ExpressionResult.of(new VariableAccessor(variable.initialize())
                .toAssigner(location, true, expression)
                .toExpression());
    }

    private static final class Elements {

        private final TokenRepresentation name;
        private final Snippet type;
        private final boolean mutable;
        private final boolean nillable;

        Elements(Snippet type, TokenRepresentation name, boolean mutable, boolean nillable) {
            this.type = type;
            this.name = name;
            this.mutable = mutable;
            this.nillable = nillable;
        }

    }

}
