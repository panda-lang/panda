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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.invoker;

import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.GenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.PandaScript;
import org.panda_lang.panda.framework.language.architecture.prototype.method.MethodInvoker;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.argument.ArgumentParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.ExpressionUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.variable.parser.VarParser;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPatternUtils;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternAssistant;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;

@ParserRegistration(target = PandaPipelines.STATEMENT, priority = PandaPriorities.STATEMENT_METHOD_INVOKER_PARSER)
public class MethodInvokerParser implements UnifiedParser, ParserHandler {

    public static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+** . +** ( +* )")
            .lastIndexAlgorithm(true)
            .build();

    @Override
    public boolean handle(TokenReader reader) {
        return AbyssPatternUtils.match(MethodInvokerParser.PATTERN, reader) && !AbyssPatternUtils.match(VarParser.ASSIGNATION_PATTERN, reader);
    }

    @Override
    public boolean parse(ParserData data, GenerationLayer nextLayer) {
        AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, data, "instance", "method-name", "arguments");

        Container container = data.getComponent(PandaComponents.CONTAINER);
        StatementCell cell = container.reserveCell();

        nextLayer.delegate(new MethodInvokerCasualParserCallback(cell, redactor), data);
        return true;
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
        public void call(ParserData delegatedData, GenerationLayer nextLayer) {
            TokenizedSource instanceSource = redactor.get("instance");
            TokenizedSource methodSource = redactor.get("method-name");
            TokenizedSource argumentsSource = redactor.get("arguments");

            PandaScript script = delegatedData.getComponent(PandaComponents.PANDA_SCRIPT);
            ModuleLoader registry = script.getModuleLoader();

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
                PrototypeField field = prototype.getFields().getField(methodName);

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
