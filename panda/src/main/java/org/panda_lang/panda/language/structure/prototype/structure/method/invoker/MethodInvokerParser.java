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

package org.panda_lang.panda.language.structure.prototype.structure.method.invoker;

import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenHollowRedactor;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPattern;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternHollows;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternUtils;
import org.panda_lang.panda.core.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.core.interpreter.parser.pipeline.DefaultPriorities;
import org.panda_lang.panda.core.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.core.interpreter.parser.util.Components;
import org.panda_lang.panda.core.structure.PandaScript;
import org.panda_lang.panda.core.structure.wrapper.Container;
import org.panda_lang.panda.core.structure.wrapper.StatementCell;
import org.panda_lang.panda.framework.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationType;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.language.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.language.structure.general.argument.ArgumentParser;
import org.panda_lang.panda.language.structure.general.expression.Expression;
import org.panda_lang.panda.language.structure.general.expression.ExpressionParser;
import org.panda_lang.panda.language.structure.overall.imports.ImportRegistry;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.field.PrototypeField;
import org.panda_lang.panda.language.structure.prototype.structure.method.PrototypeMethod;

@ParserRegistration(target = DefaultPipelines.SCOPE, parserClass = MethodInvokerParser.class, handlerClass = MethodInvokerParserHandler.class, priority = DefaultPriorities.SCOPE_METHOD_INVOKER_PARSER)
public class MethodInvokerParser implements UnifiedParser {

    public static final TokenPattern PATTERN = TokenPattern.builder()
            //.lastIndexAlgorithm(true)
            .hollow()
            .unit(TokenType.SEPARATOR, ".")
            .simpleHollow()
            .unit(TokenType.SEPARATOR, "(")
            .hollow()
            .unit(TokenType.SEPARATOR, ")")
            .unit(TokenType.SEPARATOR, ";")
            .build();

    @Override
    public void parse(ParserInfo info) {
        CasualParserGeneration generation = info.getComponent(Components.GENERATION);

        generation.getLayer(CasualParserGenerationType.HIGHER)
                .delegateImmediately(new MethodInvokerDeclarationCasualParserCallback(), info.fork());
    }

    @LocalCallback
    private static class MethodInvokerDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            TokenPatternHollows hollows = TokenPatternUtils.extract(PATTERN, delegatedInfo);
            TokenHollowRedactor redactor = new TokenHollowRedactor(hollows);

            redactor.map("instance", "method-name", "arguments");
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
            TokenHollowRedactor redactor = delegatedInfo.getComponent("redactor");

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

            PrototypeMethod prototypeMethod = prototype.getMethods().getMethod(methodName);

            if (prototypeMethod == null) {
                PrototypeField field = prototype.getField(methodName);

                if (field == null) {
                    throw new PandaParserException("Method " + methodName + " not found in class ");
                }
            }

            MethodInvoker invoker = new MethodInvoker(prototypeMethod, instance, arguments);

            StatementCell cell = delegatedInfo.getComponent("cell");
            cell.setStatement(invoker);
        }

    }

}
