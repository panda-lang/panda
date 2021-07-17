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

package org.panda_lang.framework.resource.syntax.operator;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.interpreter.token.TokenType;
import org.panda_lang.framework.interpreter.token.EqualableToken;
import org.panda_lang.framework.resource.syntax.TokenTypes;
import panda.utilities.StringUtils;

public final class Operator extends EqualableToken {

    private final String family;
    private final String operator;
    private final boolean conjunction;

    public Operator(@Nullable String family, String operator, boolean conjunction) {
        this.family = family;
        this.operator = operator;
        this.conjunction = conjunction;
    }

    public Operator(@Nullable String family, String operator) {
        this(family, operator, true);
    }

    public boolean isConjunction() {
        return conjunction;
    }

    public boolean hasFamily() {
        return !StringUtils.isEmpty(family);
    }

    public String getFamily() {
        return family;
    }

    @Override
    public String getValue() {
        return operator;
    }

    @Override
    public TokenType getType() {
        return TokenTypes.OPERATOR;
    }

}
