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
import org.panda_lang.framework.resource.syntax.PandaSyntax

import static org.junit.jupiter.api.Assertions.assertEquals

@CompileStatic
final class SimpleLexerTest {

    private static final Source SOURCE = new PandaSource(null, SimpleLexerTest.class, "this.intValue()")

    @Test
    void testKeywordsInUnknown() {
        Snippet snippet = PandaLexer.of(new PandaSyntax())
                .build()
                .convert(SOURCE)

        assertEquals "this . intValue ( )", snippet.toString()
    }

}
