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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.variable;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.expression.ExpressionUtils;
import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.architecture.statement.Variable;
import org.panda_lang.language.architecture.statement.VariableData;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.LocalChannel;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.language.interpreter.parser.pipeline.Handler;
import org.panda_lang.language.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.architecture.dynamic.assigner.Assigner;
import org.panda_lang.language.architecture.statement.PandaVariableDataInitializer;
import org.panda_lang.language.architecture.type.utils.TypeDeclarationUtils;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.token.PandaSnippet;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.PandaPipeline;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Ctx;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.AssignationPriorities;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.AssignationSubparserBootstrap;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.AssignationType;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.function.Option;

import java.util.Objects;

public final class VariableDeclarationSubparser extends AssignationSubparserBootstrap {

    @Override
    public PipelineComponent<? extends Parser>[] pipeline() {
        return ArrayUtils.of(PandaPipeline.ASSIGNER);
    }

    @Override
    public double priority() {
        return AssignationPriorities.VARIABLE_DECLARATION;
    }

    @Override
    protected BootstrapInitializer<@Nullable ExpressionResult> initialize(Context context, BootstrapInitializer<@Nullable ExpressionResult> initializer) {
        return initializer;
    }

    @Override
    protected Object customHandle(Handler handler, Context context, LocalChannel channel, Snippet source) {
        if (source.size() < 2) {
            return false;
        }

        TokenInfo name = Objects.requireNonNull(source.getLast());

        if (name.getType() != TokenTypes.UNKNOWN) {
            return false;
        }

        Option<Snippet> typeValue = TypeDeclarationUtils.readTypeBackwards(source.subSource(0, source.size() - 1));

        if (!typeValue.isPresent()) {
            return false;
        }

        Snippet type = typeValue.get();
        Snippet modifiers = PandaSnippet.ofMutable(source.subSource(0, source.size() - 1 - type.size()).getTokensRepresentations());

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

        channel.allocated("elements", new Elements(type, name, mutable != Snippet.NOT_FOUND, nillable != Snippet.NOT_FOUND));
        return true;
    }

    @Autowired(order = 1)
    public ExpressionResult parse(
        Context context,
        LocalChannel channel,
        @Ctx Scope scope,
        @Ctx Expression expression,
        @Ctx ExpressionContext expressionContext,
        @Ctx AssignationType type,
        @Channel Location location
    ) {
        Elements elements = channel.get("elements", Elements.class);
        PandaVariableDataInitializer dataInitializer = new PandaVariableDataInitializer(context, scope);
        VariableData variableData = dataInitializer.createVariableData(elements.type, elements.name, elements.mutable, elements.nillable);

        Variable variable = scope.createVariable(variableData);
        expressionContext.commit(() -> scope.removeVariable(variable.getName()));

        if (!variable.getType().isAssignableFrom(expression.getType())) {
            throw new PandaParserFailure(context,
                    "Cannot assign " + expression.getType().getSimpleName() + " to " + variable.getType().getSimpleName(),
                    "Change variable type or ensure the expression has compatible return type"
            );
        }

        Expression equalizedExpression = ExpressionUtils.equalize(expression, variable.getType());
        VariableAccessor accessor = new VariableAccessor(variable.initialize());
        Assigner<Variable> assigner = accessor.toAssigner(location, true, equalizedExpression);

        return ExpressionResult.of(assigner);
    }

    private static final class Elements {

        private final TokenInfo name;
        private final Snippet type;
        private final boolean mutable;
        private final boolean nillable;

        Elements(Snippet type, TokenInfo name, boolean mutable, boolean nillable) {
            this.type = type;
            this.name = name;
            this.mutable = mutable;
            this.nillable = nillable;
        }

    }

}
