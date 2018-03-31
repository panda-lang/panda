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

package org.panda_lang.panda.language.structure.statement.variable.parser;

import org.panda_lang.panda.design.architecture.PandaScript;
import org.panda_lang.panda.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.design.interpreter.parser.util.Components;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.design.runtime.expression.PandaExpression;
import org.panda_lang.panda.framework.design.architecture.module.ImportRegistry;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.value.PandaVariable;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.PandaSourceStream;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.language.structure.general.expression.ExpressionParser;
import org.panda_lang.panda.language.structure.general.expression.callbacks.instance.ThisExpressionCallback;
import org.panda_lang.panda.language.structure.statement.variable.VariableParserUtils;
import org.panda_lang.panda.language.syntax.PandaSyntax;
import org.panda_lang.panda.language.syntax.tokens.Keywords;

import java.util.List;

public class VarParser {

    public static final AbyssPattern ASSIGNATION_PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+** = +*")
            .build();

    public VarParserData toVarParserData(TokenizedSource source) {
        SourceStream copyOfStream = new PandaSourceStream(source);
        List<TokenizedSource> hollows = ASSIGNATION_PATTERN.extractor().extract(copyOfStream.toTokenReader());
        return new VarParserData(hollows, (hollows != null && hollows.size() == 2));
    }

    public VarParserResult parseVariable(VarParserData data, ParserInfo delegatedInfo) {
        TokenizedSource left = data.getHollows().get(0);

        if (left.size() > 2) {
            ExpressionParser expressionParser = new ExpressionParser();
            Expression instanceExpression = expressionParser.parse(delegatedInfo, left.subSource(0, left.size() - 2), true);

            if (instanceExpression != null) {
                ClassPrototype prototype = instanceExpression.getReturnType();
                PrototypeField field = prototype.getField(left.getLast().getTokenValue());

                if (field != null) {
                    return new VarParserResult(instanceExpression, field);
                }
            }
        }

        if (left.size() >= 2) {
            String variableName = left.getLast().getTokenValue();
            String variableType = left.getLast(1).getTokenValue();

            PandaScript script = delegatedInfo.getComponent(Components.SCRIPT);
            ImportRegistry importRegistry = script.getImportRegistry();
            ClassPrototype type = importRegistry.forClass(variableType);

            boolean mutable = TokenUtils.contains(left, Keywords.MUTABLE);
            boolean nullable = TokenUtils.contains(left, Keywords.NULLABLE);

            if (type == null) {
                throw new PandaParserException("Unknown type '" + variableType + "'");
            }

            Variable variable = new PandaVariable(type, variableName, 0, mutable, nullable);
            return new VarParserResult(null, variable);
        }

        if (left.size() == 1) {
            ScopeLinker linker = delegatedInfo.getComponent(Components.SCOPE_LINKER);
            Scope scope = linker.getCurrentScope();
            Variable variable = VariableParserUtils.getVariable(scope, left.getLast().getToken().getTokenValue());

            if (variable == null) {
                ClassPrototype prototype = delegatedInfo.getComponent(Components.CLASS_PROTOTYPE);

                if (prototype == null) {
                    throw new PandaParserException("Cannot get field from non-prototype scope");
                }

                Expression instanceExpression = new PandaExpression(prototype, new ThisExpressionCallback());
                PrototypeField field = prototype.getField(left.getLast().getTokenValue());

                return new VarParserResult(instanceExpression, field);
            }

            return new VarParserResult(null, variable);
        }

        throw new PandaParserException("Cannot parse variable declaration: " + left);
    }

}
