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

package org.panda_lang.panda.framework.language.resource.parsers.general.accessor;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.Accessor;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.FieldAccessor;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.VariableAccessor;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.ThisExpressionCallback;
import org.panda_lang.panda.framework.language.resource.parsers.prototype.ClassPrototypeComponents;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operators;

import java.util.Optional;

public class AccessorParser implements Parser {

    public Optional<Accessor<? extends Variable>> parseSilently(ParserData data, SourceStream source) {
        try {
            return Optional.of(parse(data, source));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Accessor<? extends Variable> parse(ParserData data, SourceStream source) {
        int index = source.toTokenizedSource().indexOf(Operators.ASSIGNMENT);

        if (index == -1) {
            throw new PandaParserFailure("Source does not contain assignment", data, source.toTokenizedSource());
        }

        Tokens accessorSource = source.toTokenizedSource().subSource(0, index);

        if (accessorSource.isEmpty()) {
            throw new PandaParserFailure("Source cannot be empty", data, source.toTokenizedSource());
        }

        return parse(data, accessorSource);
    }

    public Accessor<? extends Variable> parse(ParserData data, Tokens source) {
        if (source.size() > 1) {
            Expression instanceExpression = data.getComponent(PandaComponents.EXPRESSION).parse(data, source.subSource(0, source.size() - 2));

            if (instanceExpression == null) {
                throw new PandaParserFailure("Cannot parse variable reference: " + source, data, source);
            }

            String fieldName = source.getLast().getTokenValue();
            PrototypeField field = instanceExpression.getReturnType().getFields().getField(fieldName);

            if (field == null) {
                throw new PandaParserFailure("Field " + fieldName + " does not exist", data, source);
            }

            return new FieldAccessor(instanceExpression, field);
        }

        Scope scope = data.getComponent(UniversalComponents.SCOPE_LINKER).getCurrentScope();
        Variable variable = scope.getVariable(source.asString());

        if (variable != null) {
            return new VariableAccessor(variable, scope.indexOf(variable));
        }

        ClassPrototype prototype = data.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);

        if (prototype == null) {
            throw new PandaParserFailure("Cannot get field from non-prototype scope", data, source);
        }

        Expression instanceExpression = ThisExpressionCallback.asExpression(prototype);
        PrototypeField field = prototype.getFields().getField(source.asString());

        if (field == null) {
            throw new PandaParserFailure("Field " + source.asString() + " does not exist", data, source);
        }

        return new FieldAccessor(instanceExpression, field);
    }

}
