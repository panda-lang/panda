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

package org.panda_lang.panda;

import org.panda_lang.panda.core.interpreter.lexer.extractor.prepared.PreparedExtractor;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPattern;
import org.panda_lang.panda.framework.implementation.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.framework.implementation.interpreter.token.reader.PandaTokenReader;
import org.panda_lang.panda.framework.language.interpreter.lexer.Lexer;
import org.panda_lang.panda.framework.language.interpreter.lexer.Syntax;
import org.panda_lang.panda.framework.language.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.language.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.token.reader.TokenReader;
import org.panda_lang.panda.language.composition.PandaComposition;

import java.util.List;

public class ExtractorTest {

    //private static final String SOURCE = "a('z').b.c(new Clazz { public void x(String m) { System.out.println(m); } }).d('x');";
    private static final String SOURCE = "class A {} class B {}";

    public static void main(String[] args) {
        PandaFactory pandaFactory = new PandaFactory();
        Panda panda = pandaFactory.createPanda();

        PandaComposition pandaComposition = panda.getPandaComposition();
        Syntax syntaxComposition = pandaComposition.getSyntax();

        Lexer lexer = new PandaLexer(syntaxComposition, SOURCE);
        TokenizedSource tokenizedSource = lexer.convert();
        TokenReader tokenReader = new PandaTokenReader(tokenizedSource);

        TokenPattern pattern = TokenPattern.builder()
                .keepOpposites(true)
                .unit(TokenType.KEYWORD, "class")
                .hollow()
                .unit(TokenType.SEPARATOR, "{")
                .hollow()
                .unit(TokenType.SEPARATOR, "}")
                .build();

        PreparedExtractor extractor = new PreparedExtractor(pattern);
        List<TokenizedSource> gaps = extractor.extract(tokenReader);

        if (gaps == null) {
            System.out.println("Cannot extract gaps for PATTERN '" + pattern.toString() + "' and source '" + SOURCE + "'");
            return;
        }

        for (TokenizedSource gap : gaps) {
            System.out.println("--- Gap:");

            for (TokenRepresentation tokenRepresentation : gap.getTokensRepresentations()) {
                System.out.println("  : " + tokenRepresentation.toString());
            }
        }

        System.out.println("Size: " + gaps.size());
    }

}
