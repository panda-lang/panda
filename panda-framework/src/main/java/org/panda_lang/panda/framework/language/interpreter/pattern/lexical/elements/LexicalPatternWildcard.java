/*
 * Copyright (c) 2016-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements;

public class LexicalPatternWildcard extends DefaultLexicalPatternElement {

    private final String originalDetails;
    private String name;
    private String details;

    public LexicalPatternWildcard(String details) {
        this.originalDetails = details;
        this.details = originalDetails;
        this.name = originalDetails;
    }

    public LexicalPatternWildcard() {
        this(null);
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public String getName() {
        return name;
    }

    public String getOriginalDetails() {
        return originalDetails;
    }

}
