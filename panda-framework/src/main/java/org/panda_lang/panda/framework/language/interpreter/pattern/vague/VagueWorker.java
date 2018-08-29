/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.pattern.vague;

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;

import java.util.Stack;

public class VagueWorker {

    private final VagueExtractor extractor;
    private final VagueResult result;
    private final TokenReader source;

    private Stack<Separator> separators = new Stack<>();
    private PandaTokenizedSource expression = new PandaTokenizedSource();

    public VagueWorker(VagueExtractor extractor, VagueResult result, TokenReader source) {
        this.extractor = extractor;
        this.result = result;
        this.source = source;
    }

    public VagueResult extract() {
        for (TokenRepresentation representation : this.source) {
            if (this.isDivider(representation)) {
                this.pullFragment(representation);
                continue;
            }

            if (this.isSeparator(representation)) {
                Token token = representation.getToken();

                if (!(token instanceof Separator)) {
                    throw new PandaParserException("Token is not separator");
                }

                Separator separator = (Separator) token;

                if (this.separators.size() > 0) {
                    Separator previousSeparator = this.separators.peek();
                    Separator opposite = previousSeparator.getOpposite();

                    if (separator.equals(opposite)) {
                        this.separators.pop();

                        if (this.pullFragment(representation, 0)) {
                            continue;
                        }
                    }
                }

                if (separator.hasOpposite()) {
                    this.separators.push(separator);

                    if (this.pullFragment(representation, 1)) {
                        continue;
                    }
                }
            }

            this.expression.addToken(representation);
        }

        if (this.expression.size() != 0) {
            this.addExpression(this.expression);
        }

        result.succeed();
        return result;
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
            this.expression = new PandaTokenizedSource();
        }

        this.addOperator(operatorRepresentation);
    }

    private boolean contains(Token[] tokens, TokenRepresentation representation) {
        for (Token token : tokens) {
            if (TokenUtils.equals(representation, token)) {
                return true;
            }
        }

        return false;
    }

    private void addExpression(TokenizedSource expressionSource) {
        VagueElement expressionElement = new VagueElement(expressionSource);
        this.result.addElement(expressionElement);
    }

    private void addOperator(TokenRepresentation operatorRepresentation) {
        VagueElement operatorElement = new VagueElement(operatorRepresentation);
        this.result.addElement(operatorElement);
    }

    private boolean isDivider(TokenRepresentation representation) {
        return contains(extractor.getDividers(), representation);
    }

    private boolean isSeparator(TokenRepresentation representation) {
        return contains(extractor.getSeparators(), representation);
    }

}
