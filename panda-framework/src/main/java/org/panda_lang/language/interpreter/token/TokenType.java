/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.interpreter.token;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents token type
 */
public final class TokenType {

    private static final AtomicInteger ID_ASSIGNER = new AtomicInteger();

    private final int id;
    private final String name;

    public TokenType(String name) {
        this.id = ID_ASSIGNER.getAndIncrement();
        this.name = name;
    }

    /**
     * Get name
     *
     * @return the name
     */
    public String getName() {
        return name;
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
        return getName();
    }

}
