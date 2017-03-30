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

package org.panda_lang.panda.language.structure.block;

import org.panda_lang.panda.framework.interpreter.lexer.token.TokenizedSource;
import org.panda_lang.panda.framework.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.interpreter.parser.generation.ParserGeneration;
import org.panda_lang.panda.framework.interpreter.parser.generation.ParserGenerationCallback;
import org.panda_lang.panda.framework.interpreter.parser.generation.ParserGenerationLayer;
import org.panda_lang.panda.framework.interpreter.parser.generation.ParserGenerationType;
import org.panda_lang.panda.framework.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenHollowRedactor;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPatternHollows;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPatternUtils;
import org.panda_lang.panda.implementation.interpreter.parser.defaults.ScopeParser;
import org.panda_lang.panda.implementation.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.DefaultPriorities;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.implementation.interpreter.parser.util.Components;
import org.panda_lang.panda.implementation.structure.wrapper.Scope;
import org.panda_lang.panda.language.PandaSyntax;

@ParserRegistration(target = DefaultPipelines.SCOPE, parserClass = BlockParser.class, handlerClass = BlockParserHandler.class, priority = DefaultPriorities.BLOCK_PARSER)
public class BlockParser implements UnifiedParser {

    protected static final TokenPattern PATTERN = TokenPattern.builder().compile(PandaSyntax.getInstance(), "+* { +* }").build();

    @Override
    public void parse(ParserInfo info) {
        ParserGeneration generation = info.getComponent(Components.GENERATION);

        generation.getLayer(ParserGenerationType.HIGHER)
                .delegateImmediately(new BlockDeclarationParserCallback(), info.fork());
    }

    @LocalCallback
    private static class BlockDeclarationParserCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            TokenPatternHollows hollows = TokenPatternUtils.extract(PATTERN, delegatedInfo);
            TokenHollowRedactor redactor = new TokenHollowRedactor(hollows);

            redactor.map("block-declaration", "block-body");
            delegatedInfo.setComponent("redactor", redactor);

            //

            nextLayer.delegate(new BlockBodyParserCallback(), delegatedInfo);
        }

    }

    @LocalCallback
    private static class BlockBodyParserCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            ScopeLinker linker = delegatedInfo.getComponent(Components.SCOPE_LINKER);
            Scope currentScope = linker.getCurrentScope();

            TokenHollowRedactor redactor = delegatedInfo.getComponent("redactor");
            TokenizedSource body = redactor.get("block-body");

            ScopeParser scopeParser = new ScopeParser(currentScope);
            scopeParser.parse(delegatedInfo, body);
        }

    }

}
