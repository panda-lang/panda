/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.pattern;

import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.Token;
import org.panda_lang.framework.design.interpreter.token.TokenInfo;
import org.panda_lang.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.framework.language.resource.syntax.separator.Separator;

import java.util.Stack;

final class OperationPatternWorker {

    private final OperationPattern extractor;
    private final OperationPatternResult result;

    private final Stack<Separator> separators = new Stack<>();
    private PandaSnippet expression = new PandaSnippet();

    OperationPatternWorker(OperationPattern extractor, Snippet source) {
        this.extractor = extractor;
        this.result = new OperationPatternResult(source);
    }

    OperationPatternResult extract() {
        for (TokenInfo representation : result.getSource()) {
            verify(representation);
        }

        if (expression.size() != 0) {
            addExpression(expression);
        }

        result.succeed();
        return result;
    }

    private void verify(TokenInfo representation) {
        Token token = representation.getToken();

        if (!separators.isEmpty()) {
            expression.addToken(representation);

            if (!(token instanceof Separator)) {
                return;
            }

            Separator separator = representation.toToken();
            Separator openingSeparator = separators.peek();

            if (openingSeparator.hasOpposite() && openingSeparator.getOpposite().equals(token)) {
                separators.pop();
            }
            else if (separator.hasOpposite()) {
                separators.push(separator);
            }

            return;
        }

        if (isDivider(representation)) {
            pullFragment(representation);
            return;
        }

        if (!isSeparator(representation)) {
            expression.addToken(representation);
            return;
        }

        Separator separator = (Separator) token;
        expression.addToken(representation);

        if (separator.hasOpposite()) {
            separators.push(separator);
        }
    }

    private boolean pullFragment(TokenInfo operatorRepresentation, int requiredSeparators) {
        if (this.separators.size() == requiredSeparators) {
            this.pullFragment(operatorRepresentation);
            return true;
        }

        return false;
    }

    private void pullFragment(TokenInfo operatorRepresentation) {
        if (this.expression.size() != 0) {
            this.addExpression(this.expression);
            this.expression = new PandaSnippet();
        }

        this.addOperator(operatorRepresentation);
    }

    private boolean contains(Token[] tokens, TokenInfo representation) {
        for (Token token : tokens) {
            if (representation.contentEquals(token)) {
                return true;
            }
        }

        return false;
    }

    private void addExpression(Snippet expressionSource) {
        OperationPatternElement expressionElement = new OperationPatternElement(expressionSource);
        this.result.addElement(expressionElement);
    }

    private void addOperator(TokenInfo operatorRepresentation) {
        OperationPatternElement operatorElement = new OperationPatternElement(operatorRepresentation);
        this.result.addElement(operatorElement);
    }

    private boolean isDivider(TokenInfo representation) {
        return contains(extractor.getDividers(), representation);
    }

    private boolean isSeparator(TokenInfo representation) {
        return contains(extractor.getSeparators(), representation);
    }

}
