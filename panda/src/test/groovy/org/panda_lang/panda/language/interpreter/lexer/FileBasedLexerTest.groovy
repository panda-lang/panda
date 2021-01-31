/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.panda.language.interpreter.lexer

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test
import org.panda_lang.framework.interpreter.lexer.PandaLexer
import org.panda_lang.framework.interpreter.source.PandaSource
import org.panda_lang.framework.interpreter.source.Source
import org.panda_lang.framework.interpreter.token.Snippet
import org.panda_lang.panda.utils.PandaUtils

import static org.junit.jupiter.api.Assertions.assertEquals

@CompileStatic
final class FileBasedLexerTest {

    private static final Source SOURCE = new PandaSource(FileBasedLexerTest.class, "a('z').b.c('y').d('x');")

    @Test
    void testLexer() {
        Snippet snippet = PandaLexer.of(PandaUtils.defaultInstance().getLanguage().getSyntax())
                .build()
                .convert(SOURCE)

        assertEquals 17, snippet.size()
        assertEquals "a", snippet.getFirst().getValue()
        assertEquals ";", snippet.getLast().getValue()
    }

}
