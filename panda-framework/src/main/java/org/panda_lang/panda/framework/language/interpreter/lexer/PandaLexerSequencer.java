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

package org.panda_lang.panda.framework.language.interpreter.lexer;

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.language.interpreter.token.PandaToken;
import org.panda_lang.panda.framework.language.resource.syntax.sequence.Sequence;

import java.util.Collection;
import java.util.Stack;

public class PandaLexerSequencer {

    private final PandaLexer lexer;
    private final Collection<Sequence> sequences;
    private final Stack<Sequence> sequenceStack;

    public PandaLexerSequencer(PandaLexer lexer, Collection<Sequence> sequences) {
        this.lexer = lexer;
        this.sequences = sequences;
        this.sequenceStack = new Stack<>();
    }

    public boolean checkBefore(StringBuilder tokenBuilder, char c) {
        if (sequenceStack.size() > 0) {
            String tokenPreview = lexer.getTokenBuilder().append(c).toString();
            Sequence sequence = sequenceStack.peek();

            if (!tokenPreview.endsWith(sequence.getSequenceEnd())) {
                return true;
            }

            int startIndex = sequence.getSequenceStart().length();
            int endIndex = tokenPreview.length() - sequence.getSequenceEnd().length();
            String sequenceValue = tokenPreview.substring(startIndex, endIndex);

            Token token = new PandaToken(sequence.getType(), sequence.getName(), sequenceValue);
            lexer.getTokenizedLine().add(token);

            tokenBuilder.setLength(0);
            sequenceStack.pop();
            return true;
        }

        return false;
    }

    public boolean checkAfter(StringBuilder tokenBuilder) {
        String tokenPreview = tokenBuilder.toString();

        for (Sequence sequence : sequences) {
            if (!tokenPreview.startsWith(sequence.getSequenceStart())) {
                continue;
            }

            sequenceStack.push(sequence);
            return true;
        }

        return false;
    }

}
