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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.old;

import org.panda_lang.language.architecture.dynamic.assigner.Assigner;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.expression.ExpressionUtils;
import org.panda_lang.language.architecture.statement.Variable;
import org.panda_lang.language.architecture.statement.VariableAccessor;
import org.panda_lang.language.architecture.statement.VariableData;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.PandaSourceReader;
import org.panda_lang.panda.language.interpreter.parser.PandaTargets;
import org.panda_lang.panda.language.resource.syntax.scope.variable.VariableDataInitializer;
import org.panda_lang.panda.language.resource.syntax.type.SignatureSource;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.collection.Component;
import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.Option;

public final class VariableDeclarationSubparser implements ContextParser<AssignationContext, Assigner<?>> {

    @Override
    public String name() {
        return "variable declaration";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(PandaTargets.ASSIGNATION);
    }

    @Override
    public double priority() {
        return AssignationPriorities.VARIABLE_DECLARATION;
    }

    @Override
    public Option<Completable<Assigner<?>>> parse(Context<? extends AssignationContext> context) {
        Snippet source = context.getSource();

        if (source.size() < 2) {
            return Option.none();
        }

        PandaSourceReader sourceReader = new PandaSourceReader(context.getStream());
        boolean mutable = sourceReader.optionalRead(() -> sourceReader.read(Keywords.MUT)).isDefined();
        boolean nillable = sourceReader.optionalRead(() -> sourceReader.read(Keywords.NIL)).isDefined();
        Option<SignatureSource> signatureSource = sourceReader.readSignature();

        if (signatureSource.isEmpty()) {
            if (mutable || nillable) {
                throw new PandaParserFailure(context, "Missing variable signature");
            }

            return Option.none();
        }

        Option<TokenInfo> name = sourceReader.read(TokenTypes.UNKNOWN);

        if (name.isEmpty()) {
            // Skip variable names read as signatures
            // throw new PandaParserFailure(context, "Missing variable name");
            return Option.none();
        }

        VariableDataInitializer dataInitializer = new VariableDataInitializer(context, context.getScope());
        VariableData variableData = dataInitializer.createVariableData(signatureSource.get(), name.get(), mutable, nillable);

        Variable variable = context.getScope().createVariable(variableData);
        context.getSubject().getTransaction().getCommits().add(() -> context.getScope().removeVariable(variable.getName()));
        Expression expression = context.getSubject().getTransaction().getExpression();

        if (!variable.getSignature().isAssignableFrom(expression.getSignature())) {
            throw new PandaParserFailure(context, signatureSource.get().getName(),
                    "Cannot assign " + expression.getSignature() + " to " + variable.getSignature(),
                    "Change variable type or ensure the expression has compatible return type"
            );
        }

        Expression equalizedExpression = ExpressionUtils.equalize(expression, variable.getSignature()).orElseThrow(error -> {
            throw new PandaParserFailure(context, "Incompatible signatures");
        });

        VariableAccessor accessor = new VariableAccessor(variable.initialize());
        Assigner<Variable> assigner = accessor.toAssigner(name.get(), true, equalizedExpression);

        return Option.ofCompleted(assigner);
    }

}
