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

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;

import java.util.Stack;

public class ProgressivePatternWorker {

    private final ProgressivePattern extractor;
    private final ProgressivePatternResult result;
    private final TokenReader source;

    private Stack<Separator> separators = new Stack<>();
    private PandaSnippet expression = new PandaSnippet();

    public ProgressivePatternWorker(ProgressivePattern extractor, ProgressivePatternResult result, TokenReader source) {
        this.extractor = extractor;
        this.result = result;
        this.source = source;
    }

    public ProgressivePatternResult extract() {
        for (TokenRepresentation representation : source) {
            verify(representation);
        }

        if (expression.size() != 0) {
            addExpression(expression);
        }

        result.succeed();
        return result;
    }

    private void verify(TokenRepresentation representation) {
        Token token = representation.getToken();

        if (!separators.isEmpty()) {
            expression.addToken(representation);

            if (!(token instanceof Separator)) {
                return;
            }

            Separator separator = (Separator) token;
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

    private boolean pullFragment(TokenRepresentation operatorRepresentation, int requiredSeparators) {
        if (this.separators.size() == requiredSeparators) {
            this.pullFragment(operatorRepresentation);
            return true;
        }

        return false;
    }

    private void pullFragment(TokenRepresentation operatorRepresentation) {
        if (this.expression.size() != 0) {
            this.addExpression(this.expression);
            this.expression = new PandaSnippet();
        }

        this.addOperator(operatorRepresentation);
    }

    private boolean contains(Token[] tokens, TokenRepresentation representation) {
        for (Token token : tokens) {
            if (representation.contentEquals(token)) {
                return true;
            }
        }

        return false;
    }

    private void addExpression(Snippet expressionSource) {
        ProgressivePatternElement expressionElement = new ProgressivePatternElement(expressionSource);
        this.result.addElement(expressionElement);
    }

    private void addOperator(TokenRepresentation operatorRepresentation) {
        ProgressivePatternElement operatorElement = new ProgressivePatternElement(operatorRepresentation);
        this.result.addElement(operatorElement);
    }

    private boolean isDivider(TokenRepresentation representation) {
        return contains(extractor.getDividers(), representation);
    }

    private boolean isSeparator(TokenRepresentation representation) {
        return contains(extractor.getSeparators(), representation);
    }

}
