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

package org.panda_lang.panda.framework.design.interpreter.token;

import java.util.concurrent.atomic.AtomicInteger;

public class TokenType {

    private static final AtomicInteger ID_ASSIGNER = new AtomicInteger();

    public static final TokenType IDENTIFIER = new TokenType("IDENTIFIER");

    public static final TokenType LITERAL = new TokenType("LITERAL");

    public static final TokenType KEYWORD = new TokenType("KEYWORD");

    public static final TokenType SEPARATOR = new TokenType("SEPARATOR");

    public static final TokenType SEQUENCE = new TokenType("SEQUENCE");

    public static final TokenType OPERATOR = new TokenType("OPERATOR");

    public static final TokenType INDENTATION = new TokenType("INDENTATION");

    public static final TokenType UNKNOWN = new TokenType("UNKNOWN");

    private static final TokenType[] VALUES = new TokenType[8];

    static {
        VALUES[0] = IDENTIFIER;
        VALUES[1] = LITERAL;
        VALUES[2] = KEYWORD;
        VALUES[3] = SEPARATOR;
        VALUES[4] = SEQUENCE;
        VALUES[5] = OPERATOR;
        VALUES[6] = INDENTATION;
        VALUES[7] = UNKNOWN;
    }

    private final int id;
    private final String typeName;

    public TokenType(String typeName) {
        this.id = ID_ASSIGNER.getAndIncrement();
        this.typeName = typeName;
    }

    public int ordinal() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof TokenType)) {
            return false;
        }

        TokenType tokenType = (TokenType) o;
        return id == tokenType.id;
    }

    @Override
    public String toString() {
        return getTypeName();
    }

    public static TokenType[] values() {
        return VALUES;
    }

}
