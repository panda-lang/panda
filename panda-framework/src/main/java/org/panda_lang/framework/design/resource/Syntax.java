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

package org.panda_lang.framework.design.resource;

import org.panda_lang.framework.language.resource.syntax.keyword.Keyword;
import org.panda_lang.framework.language.resource.syntax.literal.Literal;
import org.panda_lang.framework.language.resource.syntax.operator.Operator;
import org.panda_lang.framework.language.resource.syntax.separator.Separator;
import org.panda_lang.framework.language.resource.syntax.sequence.Sequence;

import java.util.List;

/**
 * Set of syntax rules to use by the language
 */
public interface Syntax {

    /**
     * TODO: replace special characters with allowed for names characters
     *
     * Characters that can't belong to unknown tokens
     *
     * @return array of characters
     */
    char[] getSpecialCharacters();

    /**
     * Get sequences that belongs to the syntax
     *
     * @return list of sequences
     *
     * @see org.panda_lang.framework.language.resource.syntax.sequence.Sequence
     */
    List<? extends Sequence> getSequences();

    /**
     * Get operators that belongs to the syntax
     *
     * @return list of separators
     *
     * @see org.panda_lang.framework.language.resource.syntax.separator.Separator
     */
    List<? extends Operator> getOperators();

    /**
     * Get separators that belongs to the syntax
     *
     * @return list of separators
     *
     * @see org.panda_lang.framework.language.resource.syntax.separator.Separator
     */
    List<? extends Separator> getSeparators();

    /**
     * Get literals that belongs to the syntax
     *
     * @return list of literals
     *
     * @see org.panda_lang.framework.language.resource.syntax.literal.Literal
     */
    List<? extends Literal> getLiterals();

    /**
     * Get keywords that belongs to the syntax
     *
     * @return list of keywords
     *
     * @see org.panda_lang.framework.language.resource.syntax.keyword.Keyword
     */
    List<? extends Keyword> getKeywords();

}
