/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.framework.language.resource.syntax.sequence;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.token.TokenType;
import org.panda_lang.framework.language.interpreter.token.EqualableToken;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;

import java.util.Optional;

public final class Sequence extends EqualableToken {

    private final String sequenceStart;
    private final String sequenceEnd;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<String> cachedName;

    public Sequence(@Nullable String name, char sequence) {
        this(name, Character.toString(sequence));
    }

    public Sequence(@Nullable String name, String sequence) {
        this(name, sequence, sequence);
    }

    public Sequence(@Nullable String name, String sequenceStart, String sequenceEnd) {
        this.cachedName = Optional.ofNullable(name);
        this.sequenceStart = sequenceStart;
        this.sequenceEnd = sequenceEnd;
    }

    public String getSequenceStart() {
        return sequenceStart;
    }

    public String getSequenceEnd() {
        return sequenceEnd;
    }

    @Override
    public String getValue() {
        return getSequenceStart() + getSequenceEnd();
    }

    @Override
    public Optional<String> getName() {
        return cachedName;
    }

    @Override
    public TokenType getType() {
        return TokenTypes.SEQUENCE;
    }

}
