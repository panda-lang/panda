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

package panda.interpreter.token;

import org.jetbrains.annotations.Nullable;

import panda.std.Option;

public class PandaToken extends EqualableToken {

    private final TokenType type;
    @SuppressWarnings("OptionUsedAsFieldOrParameterType")
    private final Option<String> name;
    private final String value;

    public PandaToken(TokenType type, String value) {
        this(type, type.getName(), value);
    }

    public PandaToken(TokenType type, @Nullable String name, String value) {
        this.type = type;
        this.value = value;
        this.name = Option.of(name);
    }

    @Override
    public Option<String> getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public TokenType getType() {
        return type;
    }

}
