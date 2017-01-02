/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.framework.tool.divider;

import org.panda_lang.framework.interpreter.token.suggestion.Separator;
import org.panda_lang.framework.interpreter.token.suggestion.Sequence;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Set of rules for Divider
 *
 * @see Divider
 */
public class DividerRules {

    private final Collection<Separator> separators;
    private final Collection<Sequence> sequences;
    private boolean omissionsEnabled;

    public DividerRules() {
        this.separators = new ArrayList<>();
        this.sequences = new ArrayList<>();
    }

    public void enableOmissions() {
        omissionsEnabled = true;
    }

    public void addLineSeparator(Separator separator) {
        separators.add(separator);
    }

    public void addSequence(Sequence sequence) {
        sequences.add(sequence);
    }

    public boolean isOmissionsEnabled() {
        return omissionsEnabled;
    }

    public Collection<Sequence> getSequences() {
        return sequences;
    }

    public Collection<Separator> getSeparators() {
        return separators;
    }

}
