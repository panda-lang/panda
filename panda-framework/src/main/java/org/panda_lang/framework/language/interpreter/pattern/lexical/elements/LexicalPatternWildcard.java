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

package org.panda_lang.framework.language.interpreter.pattern.lexical.elements;

import org.panda_lang.utilities.commons.StringUtils;

public final class LexicalPatternWildcard extends DefaultLexicalPatternElement {

    private final String context;
    private String name;
    private String condition;

    public LexicalPatternWildcard(String condition) {
        this.context = condition;
        this.initialize();
    }

    public LexicalPatternWildcard() {
        this(StringUtils.EMPTY);
    }

    private void initialize() {
        if (StringUtils.isEmpty(context)) {
            return;
        }

        if (!context.contains(":")) {
            this.name = context;
            return;
        }

        String[] elements = StringUtils.splitFirst(context, ":");
        this.name = elements[0];
        this.condition = elements[1];
    }

    public boolean hasCondition() {
        return condition != null;
    }

    public boolean isHole() {
        return name.startsWith("*");
    }

    public String getCondition() {
        return condition;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return context;
    }

    @Override
    public String toString() {
        return getIdentifier() + ":*=" + getData();
    }

}
