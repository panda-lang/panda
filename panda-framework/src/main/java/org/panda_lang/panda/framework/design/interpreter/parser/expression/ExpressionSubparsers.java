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

package org.panda_lang.panda.framework.design.interpreter.parser.expression;

import org.panda_lang.panda.language.interpreter.parser.expression.PandaExpressionParser;

import java.util.Collection;

/**
 * Collection of {@link org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparser}
 */
public interface ExpressionSubparsers {

    /**
     * Get collection of subparsers
     *
     * @return the collection of subparsers
     */
    Collection<? extends ExpressionSubparser> getSubparsers();

    /**
     * Create default {@link org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser} using the current subparsers
     *
     * @return a new instance of expression parser
     */
    default ExpressionParser toParser() {
        return new PandaExpressionParser(this);
    }

}
