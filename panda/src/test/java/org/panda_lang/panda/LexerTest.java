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

import org.panda_lang.panda.framework.language.interpreter.lexer.Lexer;
import org.panda_lang.panda.framework.language.interpreter.token.reader.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.token.Token;
import org.panda_lang.panda.language.composition.PandaComposition;
import org.panda_lang.panda.language.PandaSyntax;
import org.panda_lang.panda.framework.implementation.lexer.PandaLexer;
import org.panda_lang.panda.framework.implementation.token.reader.PandaTokenReader;

import java.io.File;

public class LexerTest {

    private static final File SOURCE_FILE = new File("examples/hello_world.panda");

    public static void main(String[] args) {
        PandaFactory pandaFactory = new PandaFactory();
        Panda panda = pandaFactory.createPanda();

        PandaComposition pandaComposition = panda.getPandaComposition();
        PandaSyntax syntaxComposition = pandaComposition.getSyntax();

        Lexer lexer = new PandaLexer(syntaxComposition, "a('z').b.c('y').d('x');");
        TokenizedSource tokenizedSource = lexer.convert();
        TokenReader tokenReader = new PandaTokenReader(tokenizedSource);

        for (TokenRepresentation tokenRepresentation : tokenReader) {
            Token token = tokenRepresentation.getToken();

            System.out.println((tokenRepresentation.getLine() + 1) + "[" + tokenReader.iterator().getIndex() + "]" + ": " + token.getType().toString() + ": " + token.getTokenValue());
        }
    }

}
