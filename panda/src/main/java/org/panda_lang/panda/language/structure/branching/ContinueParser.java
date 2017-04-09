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

package org.panda_lang.panda.language.structure.branching;

import org.panda_lang.panda.core.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.core.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.core.interpreter.parser.util.Components;
import org.panda_lang.panda.core.structure.wrapper.Container;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.ParserGeneration;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.ParserGenerationCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.ParserGenerationLayer;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.ParserGenerationType;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.language.structure.branching.statements.Continue;

@ParserRegistration(target = DefaultPipelines.SCOPE, parserClass = ContinueParser.class, handlerClass = ContinueParserHandler.class)
public class ContinueParser implements UnifiedParser {

    @Override
    public void parse(ParserInfo info) {
        ParserGeneration generation = info.getComponent(Components.GENERATION);

        generation.getLayer(ParserGenerationType.HIGHER)
                .delegateImmediately(new ContinueDeclarationParserCallback(), info.fork());
    }

    @LocalCallback
    private static class ContinueDeclarationParserCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            SourceStream stream = delegatedInfo.getComponent(Components.SOURCE_STREAM);
            Container container = delegatedInfo.getComponent("container");

            stream.read();
            stream.read();

            Continue continueStatement = new Continue();
            container.addStatement(continueStatement);
        }

    }

}
