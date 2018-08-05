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

import java.util.ArrayList;
import java.util.Collection;

/**
 * Set of rules for Divider
 *
 * @see Divider
 */
public class DividerRules {

    private final Collection<DividerSeparator> separators;
    private final Collection<DividerSequence> sequences;
    private boolean omissionsEnabled;

    public DividerRules() {
        this.separators = new ArrayList<>();
        this.sequences = new ArrayList<>();
    }

    public void enableOmissions() {
        omissionsEnabled = true;
    }

    public void addLineSeparator(DividerSeparator separator) {
        separators.add(separator);
    }

    public void addSequence(DividerSequence sequence) {
        sequences.add(sequence);
    }

    public boolean isOmissionsEnabled() {
        return omissionsEnabled;
    }

    public Collection<DividerSequence> getSequences() {
        return sequences;
    }

    public Collection<DividerSeparator> getSeparators() {
        return separators;
    }

}
