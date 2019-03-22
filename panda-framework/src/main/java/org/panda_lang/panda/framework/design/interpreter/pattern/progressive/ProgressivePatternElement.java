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

package org.panda_lang.panda.framework.design.interpreter.pattern.progressive;

import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;

public class ProgressivePatternElement {

    private TokenRepresentation operator;
    private Snippet expression;

    public ProgressivePatternElement(TokenRepresentation operator) {
        this.operator = operator;
    }

    public ProgressivePatternElement(Snippet expression) {
        this.expression = expression;
    }

    public boolean isExpression() {
        return expression != null;
    }

    public boolean isOperator() {
        return operator != null;
    }

    public Snippet getExpression() {
        return expression;
    }

    public TokenRepresentation getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return (isOperator() ? operator.toString() : expression.toString());
    }

}
