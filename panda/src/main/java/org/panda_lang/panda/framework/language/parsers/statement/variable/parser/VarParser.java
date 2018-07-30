/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.parsers.statement.variable.parser;

import org.panda_lang.panda.framework.design.architecture.dynamic.assigner.*;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSyntax;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.*;
import org.panda_lang.panda.framework.design.interpreter.token.*;
import org.panda_lang.panda.framework.design.architecture.module.*;
import org.panda_lang.panda.framework.design.architecture.prototype.*;
import org.panda_lang.panda.framework.design.architecture.prototype.field.*;
import org.panda_lang.panda.framework.design.architecture.statement.*;
import org.panda_lang.panda.framework.design.architecture.value.*;
import org.panda_lang.panda.framework.design.architecture.PandaScript;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.interpreter.token.defaults.keyword.*;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.*;
import org.panda_lang.panda.framework.language.parsers.prototype.ClassPrototypeComponents;
import org.panda_lang.panda.language.runtime.expression.*;
import org.panda_lang.panda.framework.language.architecture.value.*;
import org.panda_lang.panda.framework.language.interpreter.parser.*;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.*;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.*;
import org.panda_lang.panda.framework.language.interpreter.token.utils.*;
import org.panda_lang.panda.framework.language.runtime.expression.*;
import org.panda_lang.panda.framework.language.parsers.general.expression.*;
import org.panda_lang.panda.framework.language.parsers.general.expression.callbacks.instance.*;
import org.panda_lang.panda.framework.language.parsers.statement.variable.*;

import java.util.*;

public class VarParser {

    public static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+**")
            .build();

    public static final AbyssPattern ASSIGNATION_PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+** = +*")
            .build();

    public VarParserData toVarParserData(ParserData delegatedData, TokenizedSource source) {
        return toVarParserData(delegatedData, new PandaSourceStream(source));
    }

    public VarParserData toVarParserData(ParserData delegatedData, SourceStream sourceStream) {
        List<TokenizedSource> hollows = ASSIGNATION_PATTERN.extractor().extract(sourceStream.toTokenReader());
        boolean assignation = hollows != null && hollows.size() == 2;

        if (assignation) {
            hollows = AbyssPatternUtils.extract(ASSIGNATION_PATTERN, sourceStream).getGaps();
        }
        else {
            hollows = AbyssPatternUtils.extract(PATTERN, sourceStream).getGaps();
        }

        Container container = delegatedData.getComponent(PandaComponents.CONTAINER);
        StatementCell cell = container.reserveCell();

        return new VarParserData(cell, hollows, assignation);
    }

    public VarParserResult parseVariable(VarParserData data, ParserData delegatedData) {
        TokenizedSource left = data.getHollows().get(0);
        ScopeLinker linker = delegatedData.getComponent(PandaComponents.SCOPE_LINKER);
        Scope scope = linker.getCurrentScope();

        if (left.size() > 2) {
            ExpressionParser expressionParser = new ExpressionParser();
            Expression instanceExpression = expressionParser.parse(delegatedData, left.subSource(0, left.size() - 2), true);

            if (instanceExpression != null) {
                ClassPrototype prototype = instanceExpression.getReturnType();
                PrototypeField field = prototype.getFields().getField(left.getLast().getTokenValue());

                if (field != null) {
                    return new VarParserResult(instanceExpression, field, false, scope, false);
                }
            }
        }

        if (left.size() >= 2) {
            String variableName = left.getLast().getTokenValue();
            String variableType = left.getLast(1).getTokenValue();

            PandaScript script = delegatedData.getComponent(PandaComponents.PANDA_SCRIPT);
            ImportRegistry importRegistry = script.getImportRegistry();
            ClassPrototype type = importRegistry.forClass(variableType);

            boolean mutable = TokenUtils.contains(left, Keywords.MUTABLE);
            boolean nullable = TokenUtils.contains(left, Keywords.NULLABLE);

            if (type == null) {
                throw new PandaParserException("Unknown type '" + variableType + "'");
            }

            Variable variable = new PandaVariable(type, variableName, 0, mutable, nullable);
            return new VarParserResult(null, variable, true, scope, true);
        }

        if (left.size() == 1) {
            Variable variable = VariableParserUtils.getVariable(scope, left.getLast().getToken().getTokenValue());

            if (variable == null) {
                ClassPrototype prototype = delegatedData.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);

                if (prototype == null) {
                    throw new PandaParserException("Cannot get field from non-prototype scope at line " + TokenUtils.getLine(left));
                }

                Expression instanceExpression = new PandaExpression(prototype, new ThisExpressionCallback());
                PrototypeField field = prototype.getFields().getField(left.getLast().getTokenValue());

                return new VarParserResult(instanceExpression, field, false, scope, false);
            }

            return new VarParserResult(null, variable, false, scope, true);
        }

        throw new PandaParserException("Cannot parse variable declaration: " + left);
    }

    public void parseAssignation(VarParserData data, VarParserResult result, ParserData delegatedData) {
        Variable variable = result.getVariable();
        TokenizedSource right = data.getHollows().get(1);

        ExpressionParser expressionParser = new ExpressionParser();
        Expression expressionValue = expressionParser.parse(delegatedData, right);

        if (expressionValue == null) {
            throw new PandaParserException("Cannot parse expression '" + right + "'");
        }

        Statement assigner;

        if (result.isLocal()) {
            if (!expressionValue.isNull() && !expressionValue.getReturnType().isAssociatedWith(variable.getType())) {
                throw new PandaParserException("Return type is incompatible with the type of variable at line " + TokenUtils.getLine(right)
                        + " (var: " + variable.getType().getClassName() + "; expr: " + expressionValue.getReturnType().getClassName() + ")");
            }

            assigner = new VariableAssigner(variable, VariableParserUtils.indexOf(result.getScope(), variable), expressionValue);
        }
        else {
            Expression instanceExpression = result.getInstanceExpression();
            String fieldName = result.getVariable().getName();

            ClassPrototype type = instanceExpression.getReturnType();
            PrototypeField field = type.getFields().getField(fieldName);

            if (field == null) {
                throw new PandaParserException("Field '" + fieldName + "' does not belong to " + type.getClassName());
            }

            if (!field.getType().equals(expressionValue.getReturnType())) {
                throw new PandaParserException("Return type is incompatible with the type of variable at line " + TokenUtils.getLine(right));
            }

            assigner = new FieldAssigner(instanceExpression, field, expressionValue);
        }

        data.getCell().setStatement(assigner);
    }

}
