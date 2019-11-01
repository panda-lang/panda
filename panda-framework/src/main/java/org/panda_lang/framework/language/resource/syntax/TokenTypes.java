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

package org.panda_lang.framework.language.resource.syntax;

import org.panda_lang.framework.design.interpreter.token.TokenType;
import org.panda_lang.utilities.commons.ReflectionUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Basic collection of token types
 */
public final class TokenTypes {

    /**
     * Represents {@link org.panda_lang.framework.language.resource.syntax.separator.Separator}
     */
    public static final TokenType SECTION = new TokenType("SECTION");
    /**
     * Represents {@link org.panda_lang.framework.language.resource.syntax.auxiliary.Identifier}
     */
    public static final TokenType IDENTIFIER = new TokenType("IDENTIFIER");
    /**
     * Represents {@link org.panda_lang.framework.language.resource.syntax.literal.Literal}
     */
    public static final TokenType LITERAL = new TokenType("LITERAL");
    /**
     * Represents {@link org.panda_lang.framework.language.resource.syntax.keyword.Keyword}
     */
    public static final TokenType KEYWORD = new TokenType("KEYWORD");
    /**
     * Represents {@link org.panda_lang.framework.language.resource.syntax.separator.Separator}
     */
    public static final TokenType SEPARATOR = new TokenType("SEPARATOR");
    /**
     * Represents {@link org.panda_lang.framework.language.resource.syntax.sequence.Sequence}
     */
    public static final TokenType SEQUENCE = new TokenType("SEQUENCE");
    /**
     * Represents {@link org.panda_lang.framework.language.resource.syntax.operator.Operator}
     */
    public static final TokenType OPERATOR = new TokenType("OPERATOR");
    /**
     * Represents {@link org.panda_lang.framework.language.resource.syntax.operator.Operator}
     */
    public static final TokenType INDENTATION = new TokenType("INDENTATION");
    /**
     * Represents random {@link org.panda_lang.framework.language.interpreter.token.PandaToken}
     */
    public static final TokenType UNKNOWN = new TokenType("UNKNOWN");

    /**
     * Collected types
     */
    private static final Collection<TokenType> VALUES;

    static {
        VALUES = ReflectionUtils.getStaticFieldValues(TokenType.class, TokenType.class);
    }

    /**
     * Get all defined in the class types
     *
     * @return the collection of defined types
     */
    public static Collection<? extends TokenType> values() {
        return new ArrayList<>(VALUES);
    }

}
