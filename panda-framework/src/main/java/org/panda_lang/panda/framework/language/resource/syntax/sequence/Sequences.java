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

package org.panda_lang.panda.framework.language.resource.syntax.sequence;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;

/**
 * Default sequences
 */
public class Sequences {

    public static final Sequence STRING = new Sequence("String", '"');

    public static final Sequence RAW_STRING = new Sequence("String", "'");

    public static final Sequence LINE_ORIENTED_COMMENT = new Sequence("Comment", "//", System.lineSeparator());

    public static final Sequence BLOCK_ORIENTED_COMMENT = new Sequence("Comment", "/*", "*/");

    public static final Sequence DOCUMENTATION_ORIENTED_COMMENT = new Sequence("Documentation", "/**", "*/");

    private static final Sequence[] VALUES = new Sequence[5];

    static {
        VALUES[0] = STRING;
        VALUES[1] = RAW_STRING;
        VALUES[2] = LINE_ORIENTED_COMMENT;
        VALUES[3] = BLOCK_ORIENTED_COMMENT;
        VALUES[4] = DOCUMENTATION_ORIENTED_COMMENT;
    }

    public static Sequence[] values() {
        return VALUES;
    }

    public static @Nullable Sequence valueOf(Token token) {
        if (token.getType() != TokenType.SEQUENCE) {
            return null;
        }

        String value = token.getTokenValue();

        for (Sequence sequence : values()) {
            if (!sequence.getName().equals(token.getName())) {
                continue;
            }

            if (!sequence.getSequenceStart().equals(value) && !sequence.getSequenceEnd().equals(value)) {
                continue;
            }

            return sequence;
        }

        return null;
    }

}
