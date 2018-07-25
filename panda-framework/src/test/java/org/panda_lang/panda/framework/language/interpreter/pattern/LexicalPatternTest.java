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

package org.panda_lang.panda.framework.language.interpreter.pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.LexicalPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.extractor.LexicalExtractorResult;

public class LexicalPatternTest {

    @Test
    public void testLexicalPattern() {
        LexicalPattern<String> pattern = LexicalPattern.<String> builder()
                .compile("(send msg:[message] wildcard:* 3:to (console|terminalIdentifier:terminal[ ][screen *])|rand)")
                .build();

        LexicalExtractorResult<String> result = pattern.extract("send 'test' to terminal screen X11");

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isMatched());

        Assertions.assertAll(
                () -> Assertions.assertNotNull(result.getWildcards()),
                () -> Assertions.assertNotNull(result.getIdentifiers()),
                () -> Assertions.assertNotNull(result.getProcessedValues())

        );

        Assertions.assertAll(
                () -> Assertions.assertEquals("'test'", result.getWildcards().get(0)),
                () -> Assertions.assertEquals("X11", result.getWildcards().get(1))
        );
    }

}
