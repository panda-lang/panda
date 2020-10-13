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

import org.panda_lang.language.architecture.dynamic.assigner.Assigner;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.expression.ExpressionUtils;
import org.panda_lang.language.architecture.statement.Variable;
import org.panda_lang.language.architecture.statement.VariableData;
import org.panda_lang.language.architecture.type.TypeDeclarationUtils;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.language.interpreter.token.PandaSnippet;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.PandaPipeline;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.AssignationPriorities;
import org.panda_lang.panda.language.resource.syntax.scope.variable.VariableDataInitializer;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.collection.Component;
import org.panda_lang.utilities.commons.function.Option;

import javax.lang.model.util.Elements;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class VariableDeclarationSubparser implements ContextParser<AssignationContext, Assigner<?>> {

    @Override
    public String name() {
        return "variable declaration";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(PandaPipeline.ASSIGNER);
    }

    @Override
    public double priority() {
        return AssignationPriorities.VARIABLE_DECLARATION;
    }

    @Override
    public Option<CompletableFuture<Assigner<?>>> parse(Context<AssignationContext> context) {
        Snippet source = context.getSource();

        if (source.size() < 2) {
            return Option.none();
        }

        TokenInfo name = Objects.requireNonNull(source.getLast());

        if (name.getType() != TokenTypes.UNKNOWN) {
            return Option.none();
        }

        Option<Snippet> typeValue = TypeDeclarationUtils.readTypeBackwards(source.subSource(0, source.size() - 1));

        if (!typeValue.isPresent()) {
            return Option.none();
        }

        Snippet type = typeValue.get();
        Snippet modifiers = PandaSnippet.ofMutable(source.subSource(0, source.size() - 1 - type.size()).getTokensRepresentations());

        // max amount of modifiers: mut, nil
        if (modifiers.size() > 2) {
            return Option.none();
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
            return Option.none();
        }

        boolean mut = mutable != Snippet.NOT_FOUND;
        boolean nil = nillable != Snippet.NOT_FOUND;

        VariableDataInitializer dataInitializer = new VariableDataInitializer(context, context.getScope());
        VariableData variableData = dataInitializer.createVariableData(type, name, mut, nil);

        Variable variable = context.getScope().createVariable(variableData);
        context.getSubject().commit(() -> context.getScope().removeVariable(variable.getName()));

        if (!variable.getType().isAssignableFrom(expression.getType())) {
            throw new PandaParserFailure(context, context.getComponent(Components.SOURCE).getLine(location.getLine()),
                    "Cannot assign " + expression.getType().getSimpleName() + " to " + variable.getType().getSimpleName(),
                    "Change variable type or ensure the expression has compatible return type"
            );
        }

        Expression equalizedExpression = ExpressionUtils.equalize(expression, variable.getType());
        VariableAccessor accessor = new VariableAccessor(variable.initialize());
        Assigner<Variable> assigner = accessor.toAssigner(location, true, equalizedExpression);

        return ExpressionResult.of(assigner);
    }

}
