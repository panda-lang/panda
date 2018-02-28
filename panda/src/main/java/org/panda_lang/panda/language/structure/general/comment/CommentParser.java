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

package org.panda_lang.panda.language.structure.general.comment;

import org.panda_lang.panda.design.interpreter.parser.generation.CasualParserGenerationAssistant;
import org.panda_lang.panda.design.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.design.interpreter.parser.util.Components;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.token.reader.TokenReader;

@ParserRegistration(target = DefaultPipelines.ALL, parserClass = CommentParser.class, handlerClass = CommentParserHandler.class)
public class CommentParser implements UnifiedParser {

    @Override
    public void parse(ParserInfo info) {
        CasualParserGenerationAssistant.delegateImmediately(info, new CommentCasualParserCallback());
    }

    @LocalCallback
    private static class CommentCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            SourceStream stream = delegatedInfo.getComponent(Components.SOURCE_STREAM);
            TokenReader reader = stream.toTokenReader();

            while (reader.hasNext()) {
                TokenRepresentation representation = reader.next();
                Token token = representation.getToken();

                if (token.getType() != TokenType.SEQUENCE || !token.getName().equals("Comment")) {
                    break;
                }

                reader.read();
            }

            stream.readDifference(reader);
        }

    }

}
