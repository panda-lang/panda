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

package org.panda_lang.panda.utilities.redact.match;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.utilities.commons.pattern.text.TextHollowPattern;

public class HollowPatternTest {

    private static final String EXPRESSION = "instance.extractToken().method(parameter.extractToken())";

    @Test
    public void testHollowPatternWithCompiler() {
        TextHollowPattern hollowPattern = TextHollowPattern.builder()
                .compile("*.*(*)*")
                .build();

        Assertions.assertTrue(hollowPattern.match(EXPRESSION));
    }

    @Test
    public void testHollowPatternWithBuilder() {
        TextHollowPattern hollowPattern = TextHollowPattern.builder()
                .basis("set")
                .hollow()
                .basis("to")
                .hollow()
                .build();

        boolean matched = hollowPattern.match("set (variable1 to 14) to value of (set variable2 to 10)");

        System.out.println(matched);
        System.out.println(hollowPattern.getHollows());
    }

}
