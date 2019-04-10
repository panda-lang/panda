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

package org.panda_lang.panda.framework.design.resource.parsers.expression.fixed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ExpressionParserSettings {

    public static final ExpressionParserSettings DEFAULT = ExpressionParserSettings.create();
    public static final ExpressionParserSettings COMBINED = ExpressionParserSettings.create().withCombinedExpressions();

    private Collection<String> selectedSubparsers;
    private Boolean selectedMode;
    private boolean combined;

    private ExpressionParserSettings() { }

    public ExpressionParserSettings includeSelected() {
        this.selectedMode = Boolean.FALSE;
        return this;
    }

    public ExpressionParserSettings excludeSelected() {
        this.selectedMode = Boolean.TRUE;
        return this;
    }

    public ExpressionParserSettings withCombinedExpressions() {
        this.combined = true;
        return this;
    }

    public ExpressionParserSettings withSelectedSubparsers(String... subparsers) {
        if (this.selectedSubparsers == null) {
            this.selectedSubparsers = new ArrayList<>(subparsers.length);
        }

        Collections.addAll(this.selectedSubparsers, subparsers);
        return this;
    }

    public ExpressionParserSettings withSelectedSubparsers(Collection<String> toExclude) {
        if (selectedSubparsers == null) {
            this.selectedSubparsers = toExclude;
        }
        else {
            this.selectedSubparsers.addAll(toExclude);
        }

        return this;
    }

    public boolean hasSelected() {
        return selectedSubparsers != null;
    }

    public boolean isCombined() {
        return combined;
    }

    public Boolean getSelectedMode() {
        return selectedMode;
    }

    public Collection<? extends String> getSelectedSubparsers() {
        return selectedSubparsers;
    }

    public static ExpressionParserSettings create() {
        return new ExpressionParserSettings();
    }

}
