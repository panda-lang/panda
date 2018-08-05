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

package org.panda_lang.panda.utilities.commons.redact.divider;

public class DividerSequence {

    private final String tokenName;
    private final String sequenceStart;
    private final String sequenceEnd;

    public DividerSequence(String tokenName, char sequence) {
        this(tokenName, Character.toString(sequence));
    }

    public DividerSequence(String tokenName, String sequence) {
        this(tokenName, sequence, sequence);
    }

    public DividerSequence(String tokenName, String sequenceStart, String sequenceEnd) {
        this.tokenName = tokenName;
        this.sequenceStart = sequenceStart;
        this.sequenceEnd = sequenceEnd;
    }

    public String getSequenceStart() {
        return sequenceStart;
    }

    public String getSequenceEnd() {
        return sequenceEnd;
    }

    public String getTokenValue() {
        return getSequenceStart() + getSequenceEnd();
    }

    public String getName() {
        return tokenName;
    }

}
