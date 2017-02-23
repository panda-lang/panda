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

package org.panda_lang.panda.language.structure.group;

import org.panda_lang.framework.interpreter.lexer.token.TokenType;
import org.panda_lang.framework.interpreter.lexer.token.TokenizedSource;
import org.panda_lang.framework.interpreter.lexer.token.distributor.SourceStream;
import org.panda_lang.framework.interpreter.lexer.token.extractor.Extractor;
import org.panda_lang.framework.interpreter.lexer.token.reader.TokenReader;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.framework.interpreter.parser.generation.ParserGeneration;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationCallback;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationType;
import org.panda_lang.framework.interpreter.parser.util.Components;
import org.panda_lang.framework.structure.Script;
import org.panda_lang.framework.structure.Statement;
import org.panda_lang.panda.implementation.interpreter.lexer.token.extractor.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.implementation.interpreter.parser.ParserRegistration;
import org.panda_lang.panda.language.structure.prototype.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.parser.ClassPrototypeReference;

import java.util.List;

@ParserRegistration(parserClass = GroupParser.class, handlerClass = GroupParserHandler.class)
public class GroupParser implements UnifiedParser {

    private static final TokenPattern PATTERN = TokenPattern.builder()
            .unit(TokenType.KEYWORD, "group")
            .gap()
            .unit(TokenType.SEPARATOR, ";")
            .build();

    @Override
    public Statement parse(final ParserInfo parserInfo) {
        ParserGeneration generation = parserInfo.getComponent(Components.GENERATION);
        SourceStream source = parserInfo.getComponent(Components.SOURCE_STREAM);

        Extractor extractor = PATTERN.extractor();
        TokenReader reader = source.toTokenReader();
        List<TokenizedSource> gaps = extractor.extract(reader);

        if (gaps == null || gaps.size() != 1) {
            throw new PandaParserException("Cannot parse group at line " + (source.read().getLine() + 1));
        }

        String groupName = gaps.get(0).getToken(0).getTokenValue();
        source.readDifference(reader);

        GroupRegistry registry = GroupRegistry.getDefault();
        final Group group = registry.getOrCreate(groupName);

        generation.getLayer(ParserGenerationType.HIGHER)
                .delegateAfter(new ParserGenerationCallback() {
                    @Override
                    public void call(ParserInfo parserInfo) {
                        Script script = parserInfo.getComponent(Components.SCRIPT);
                        List<ClassPrototypeReference> prototypeReferences = script.select(ClassPrototypeReference.class);

                        for (ClassPrototypeReference prototypeReference : prototypeReferences) {
                            ClassPrototype classPrototype = prototypeReference.getClassPrototype();
                            group.add(classPrototype);
                        }
                    }
                });

        return new GroupStatement(group);
    }

}
