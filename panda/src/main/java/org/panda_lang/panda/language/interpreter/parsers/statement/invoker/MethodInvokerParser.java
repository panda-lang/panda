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

package org.panda_lang.panda.language.interpreter.parsers.statement.invoker;

import org.panda_lang.panda.design.architecture.PandaScript;
import org.panda_lang.panda.design.architecture.statement.invoker.MethodInvoker;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;
import org.panda_lang.panda.design.interpreter.parser.generation.CasualParserGenerationAssistant;
import org.panda_lang.panda.language.interpreter.parsers.PandaPipelines;
import org.panda_lang.panda.language.interpreter.parsers.PandaPriorities;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternAssistant;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.language.interpreter.token.utils.TokenUtils;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.language.interpreter.parsers.general.argument.ArgumentParser;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.ExpressionParser;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.ExpressionUtils;
import org.panda_lang.panda.framework.design.architecture.module.ImportRegistry;
import org.panda_lang.panda.language.interpreter.PandaSyntax;

@ParserRegistration(target = PandaPipelines.STATEMENT, parserClass = MethodInvokerParser.class, handlerClass = MethodInvokerParserHandler.class, priority = PandaPriorities.STATEMENT_METHOD_INVOKER_PARSER)
public class MethodInvokerParser implements UnifiedParser {

    public static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+** . +** ( +* )")
            .lastIndexAlgorithm(true)
            .build();

    @Override
    public void parse(ParserData data) {
        CasualParserGenerationAssistant.delegateImmediately(data, new MethodInvokerDeclarationCasualParserCallback());
    }

    @LocalCallback
    private static class MethodInvokerDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, delegatedData, "instance", "method-name", "arguments");

            Container container = delegatedData.getComponent(PandaComponents.CONTAINER);
            StatementCell cell = container.reserveCell();

            nextLayer.delegate(new MethodInvokerCasualParserCallback(cell, redactor), delegatedData);
        }

    }

    @LocalCallback
    private static class MethodInvokerCasualParserCallback implements CasualParserGenerationCallback {

        private final StatementCell cell;
        private final AbyssRedactor redactor;

        private MethodInvokerCasualParserCallback(StatementCell cell, AbyssRedactor redactor) {
            this.cell = cell;
            this.redactor = redactor;
        }

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            TokenizedSource instanceSource = redactor.get("instance");
            TokenizedSource methodSource = redactor.get("method-name");
            TokenizedSource argumentsSource = redactor.get("arguments");

            PandaScript script = delegatedData.getComponent(PandaComponents.PANDA_SCRIPT);
            ImportRegistry registry = script.getImportRegistry();

            String surmiseClassName = instanceSource.asString();
            ClassPrototype prototype = registry.forClass(surmiseClassName);

            String methodName = methodSource.asString();
            Expression instance = null;

            if (prototype == null) {
                ExpressionParser expressionParser = new ExpressionParser();

                instance = expressionParser.parse(delegatedData, instanceSource);
                prototype = instance.getReturnType();
            }

            ArgumentParser argumentParser = new ArgumentParser();
            Expression[] arguments = argumentParser.parse(delegatedData, argumentsSource);

            ClassPrototype[] parameterTypes = ExpressionUtils.toTypes(arguments);
            PrototypeMethod prototypeMethod = prototype.getMethods().getMethod(methodName, parameterTypes);

            if (prototypeMethod == null) {
                PrototypeField field = prototype.getField(methodName);

                if (field == null) {
                    throw new PandaParserException("Method " + methodName + " not found in class " + prototype.getClassName() + " at line " + TokenUtils.getLine(instanceSource));
                }

                throw new PandaParserException("Not implemented");
            }

            Statement invoker = new MethodInvoker(prototypeMethod, instance, arguments);
            cell.setStatement(invoker);
        }

    }

}
