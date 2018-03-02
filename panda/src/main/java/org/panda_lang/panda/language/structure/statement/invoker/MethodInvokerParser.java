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

package org.panda_lang.panda.language.structure.statement.invoker;

import org.panda_lang.panda.design.architecture.PandaScript;
import org.panda_lang.panda.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.design.architecture.wrapper.Container;
import org.panda_lang.panda.design.architecture.wrapper.StatementCell;
import org.panda_lang.panda.design.interpreter.parser.generation.CasualParserGenerationAssistant;
import org.panda_lang.panda.design.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.design.interpreter.parser.pipeline.DefaultPriorities;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.design.interpreter.parser.util.Components;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternAssistant;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.language.structure.general.argument.ArgumentParser;
import org.panda_lang.panda.language.structure.general.expression.Expression;
import org.panda_lang.panda.language.structure.general.expression.ExpressionParser;
import org.panda_lang.panda.language.structure.general.expression.ExpressionUtils;
import org.panda_lang.panda.language.structure.overall.imports.ImportRegistry;
import org.panda_lang.panda.language.syntax.PandaSyntax;

@ParserRegistration(target = DefaultPipelines.STATEMENT, parserClass = MethodInvokerParser.class, handlerClass = MethodInvokerParserHandler.class, priority = DefaultPriorities.STATEMENT_METHOD_INVOKER_PARSER)
public class MethodInvokerParser implements UnifiedParser {

    public static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+** . +** ( +* )")
            .lastIndexAlgorithm(true)
            .build();

    @Override
    public void parse(ParserInfo info) {
        CasualParserGenerationAssistant.delegateImmediately(info, new MethodInvokerDeclarationCasualParserCallback());
    }

    @LocalCallback
    private static class MethodInvokerDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, delegatedInfo, "instance", "method-name", "arguments");
            delegatedInfo.setComponent("redactor", redactor);

            Container container = delegatedInfo.getComponent("container");
            StatementCell cell = container.reserveCell();
            delegatedInfo.setComponent("cell", cell);

            nextLayer.delegate(new MethodInvokerCasualParserCallback(), delegatedInfo);
        }

    }

    @LocalCallback
    private static class MethodInvokerCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            AbyssRedactor redactor = delegatedInfo.getComponent("redactor");
            TokenizedSource instanceSource = redactor.get("instance");
            TokenizedSource methodSource = redactor.get("method-name");
            TokenizedSource argumentsSource = redactor.get("arguments");

            PandaScript script = delegatedInfo.getComponent(Components.SCRIPT);
            ImportRegistry registry = script.getImportRegistry();

            String surmiseClassName = instanceSource.asString();
            ClassPrototype prototype = registry.forClass(surmiseClassName);

            String methodName = methodSource.asString();
            Expression instance = null;

            if (prototype == null) {
                ExpressionParser expressionParser = new ExpressionParser();

                instance = expressionParser.parse(delegatedInfo, instanceSource);
                prototype = instance.getReturnType();
            }

            ArgumentParser argumentParser = new ArgumentParser();
            Expression[] arguments = argumentParser.parse(delegatedInfo, argumentsSource);

            ClassPrototype[] parameterTypes = ExpressionUtils.toTypes(arguments);
            PrototypeMethod prototypeMethod = prototype.getMethods().getMethod(methodName, parameterTypes);

            if (prototypeMethod == null) {
                PrototypeField field = prototype.getField(methodName);

                if (field == null) {
                    throw new PandaParserException("Method " + methodName + " not found in class " + prototype.getClassName() + " at line " + TokenUtils.getLine(instanceSource));
                }

                throw new PandaParserException("Not implemented");
            }

            MethodInvoker invoker = new MethodInvoker(prototypeMethod, instance, arguments);
            StatementCell cell = delegatedInfo.getComponent("cell");
            cell.setStatement(invoker);
        }

    }

}
