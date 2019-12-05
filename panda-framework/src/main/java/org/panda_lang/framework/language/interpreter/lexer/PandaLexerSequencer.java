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

package org.panda_lang.framework.language.interpreter.lexer;

import org.panda_lang.framework.design.interpreter.token.Token;
import org.panda_lang.framework.language.resource.syntax.sequence.Sequence;
import org.panda_lang.framework.language.resource.syntax.sequence.SequenceToken;

import java.util.Stack;

final class PandaLexerSequencer {

    private final PandaLexerWorker worker;
    private final Stack<Sequence> sequenceStack;

    public PandaLexerSequencer(PandaLexerWorker worker) {
        this.worker = worker;
        this.sequenceStack = new Stack<>();
    }

    public boolean checkBefore(StringBuilder tokenBuilder, char c) {
        if (sequenceStack.isEmpty()) {
            return false;
        }

        String tokenPreview = worker.getBuilder().append(c).toString();
        Sequence sequence = sequenceStack.peek();

        if (!tokenPreview.endsWith(sequence.getSequenceEnd())) {
            return true;
        }

        int startIndex = sequence.getSequenceStart().length();
        int endIndex = tokenPreview.length() - sequence.getSequenceEnd().length();
        String sequenceValue = tokenPreview.substring(startIndex, endIndex);

        Token token = new SequenceToken(sequence, sequenceValue);
        worker.addLineToken(token);

        tokenBuilder.setLength(0);
        sequenceStack.pop();
        return true;
    }

    public boolean checkAfter(StringBuilder tokenBuilder) {
        String tokenPreview = tokenBuilder.toString();

        for (Sequence sequence : worker.getConfiguration().syntax.getSequences()) {
            if (!tokenPreview.endsWith(sequence.getSequenceStart())) {
                continue;
            }

            sequenceStack.push(sequence);
            return true;
        }

        return false;
    }

}
