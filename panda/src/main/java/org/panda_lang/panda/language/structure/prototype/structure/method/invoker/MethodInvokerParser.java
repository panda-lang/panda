/*
 * Copyright (c) 2015-2017 Dzikoysk
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

import org.panda_lang.framework.interpreter.lexer.token.TokenType;
import org.panda_lang.framework.interpreter.lexer.token.TokenizedSource;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.framework.interpreter.parser.generation.ParserGeneration;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationCallback;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationLayer;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationType;
import org.panda_lang.framework.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.framework.interpreter.parser.util.Components;
import org.panda_lang.panda.implementation.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.implementation.structure.Script;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenHollowRedactor;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPatternHollows;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPatternUtils;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.implementation.structure.wrapper.Scope;
import org.panda_lang.panda.language.structure.group.Group;
import org.panda_lang.panda.language.structure.imports.Import;
import org.panda_lang.panda.language.structure.imports.ImportStatement;
import org.panda_lang.panda.language.structure.prototype.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.method.Method;

import java.awt.*;
import java.util.List;

@ParserRegistration(target = DefaultPipelines.SCOPE, parserClass = MethodInvokerParser.class, handlerClass = MethodInvokerParserHandler.class, priority = 1)
public class MethodInvokerParser implements UnifiedParser {

    protected static final TokenPattern PATTERN = TokenPattern.builder()
            .hollow()
            .unit(TokenType.SEPARATOR, ".")
            .hollow()
            .unit(TokenType.SEPARATOR, "(")
            .hollow()
            .unit(TokenType.SEPARATOR, ")")
            .unit(TokenType.SEPARATOR, ";")
            .build();

    @Override
    public void parse(ParserInfo parserInfo) {
        ParserGeneration generation = parserInfo.getComponent(Components.GENERATION);

        generation.getLayer(ParserGenerationType.HIGHER)
                .delegateImmediately(new MethodInvokerDeclarationParserCallback(), parserInfo.fork());
    }

    @LocalCallback
    private static class MethodInvokerDeclarationParserCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            TokenPatternHollows hollows = TokenPatternUtils.extract(PATTERN, delegatedInfo);
            TokenHollowRedactor redactor = new TokenHollowRedactor(hollows);

            redactor.map("instance", "method-name", "arguments");
            delegatedInfo.setComponent("redactor", redactor);
            nextLayer.delegate(new MethodInvokerParserCallback(), delegatedInfo);

            System.out.println(redactor.get("instance") + " | " + redactor.get("method-name"));
        }

    }

    @LocalCallback
    private static class MethodInvokerParserCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            TokenHollowRedactor redactor = delegatedInfo.getComponent("redactor");

            TokenizedSource instance = redactor.get("instance");
            TokenizedSource method = redactor.get("method-name");
            TokenizedSource arguments = redactor.get("arguments");

            Script script = delegatedInfo.getComponent(Components.SCRIPT);
            List<ImportStatement> importStatements = script.select(ImportStatement.class);

            // Temp, just a test

            String prototypeName = instance.getToken(0).getTokenValue();
            String methodName = method.getToken(0).getTokenValue();
            ClassPrototype prototype = null;

            for (ImportStatement importStatement : importStatements) {
                Import anImport = importStatement.getAssociatedImport();
                Group group = anImport.getGroup();
                prototype = group.get(prototypeName);

                if (prototype != null) {
                    break;
                }
            }

            //prototypeMethod.invoke(null);

            Method prototypeMethod = prototype.getMethods().get(methodName);
            MethodInvoker invoker = new MethodInvoker(prototypeMethod);
            ScopeLinker linker = delegatedInfo.getComponent(Components.LINKER);

            Scope currentScope = linker.getCurrentScope();
            currentScope.addStatement(invoker);
        }

    }

}
