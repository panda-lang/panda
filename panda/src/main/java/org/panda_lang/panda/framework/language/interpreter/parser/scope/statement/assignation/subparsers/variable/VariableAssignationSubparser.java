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

package org.panda_lang.panda.framework.language.interpreter.parser.scope.statement.assignation.subparsers.variable;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapParserBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.AutowiredParameters;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Type;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.FieldAccessor;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.VariableAccessor;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers.callbacks.instance.ThisExpressionCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.prototype.ClassPrototypeComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.scope.statement.assignation.AssignationComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.scope.statement.assignation.AssignationPriorities;
import org.panda_lang.panda.framework.language.interpreter.parser.scope.statement.assignation.AssignationSubparserBootstrap;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

@ParserRegistration(target = PandaPipelines.ASSIGNER_LABEL, priority = AssignationPriorities.VARIABLE_ASSIGNATION)
public class VariableAssignationSubparser extends AssignationSubparserBootstrap {

    @Override
    protected BootstrapParserBuilder<@Nullable Statement> initialize(ParserData data, BootstrapParserBuilder<@Nullable Statement> defaultBuilder) {
        return defaultBuilder.pattern("<name:reader expression include field>");
    }

    @Autowired
    @AutowiredParameters(skip = 2, value = {
            @Type(with = Component.class, value = AssignationComponents.SCOPE_LABEL),
            @Type(with = Src.class, value = "name"),
            @Type(with = Component.class, value = AssignationComponents.EXPRESSION_LABEL)
    })
    public @Nullable Statement parse(ParserData data, ExtractorResult result, Scope scope, Tokens name, Expression expression) {
        if (name.size() > 1) {
            Expression instanceExpression = data.getComponent(PandaComponents.EXPRESSION).parse(data, name.subSource(0, name.size() - 2));

            if (instanceExpression == null) {
                throw new PandaParserException("Cannot parse variable reference: " + name);
            }

            String fieldName = name.getLast().getTokenValue();
            PrototypeField field = instanceExpression.getReturnType().getFields().getField(fieldName);

            if (field == null) {
                throw new PandaParserFailure("Field " + fieldName + " does not exist", data);
            }

            return new FieldAccessor(instanceExpression, field, expression);
        }

        Variable variable = scope.getVariable(name.asString());

        if (variable != null) {
            return new VariableAccessor(variable, scope.indexOf(variable), expression);
        }

        ClassPrototype prototype = data.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);

        if (prototype == null) {
            throw new PandaParserFailure("Cannot get field from non-prototype scope", data);
        }

        Expression instanceExpression = new PandaExpression(prototype, new ThisExpressionCallback());
        PrototypeField field = prototype.getFields().getField(name.asString());

        if (field == null) {
            throw new PandaParserFailure("Field " + name.asString() + " does not exist", data);
        }

        return new FieldAccessor(instanceExpression, field, expression);
    }

}
