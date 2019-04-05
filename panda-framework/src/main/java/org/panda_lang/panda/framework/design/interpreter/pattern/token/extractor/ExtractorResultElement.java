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

package org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor;

import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;

public class ExtractorResultElement {

    private final Object element;

    public ExtractorResultElement(Object element) {
        this.element = element;
    }

    public boolean isExpression() {
        return element instanceof Expression;
    }

    public boolean isSnippet() {
        return element instanceof Snippet;
    }

    @SuppressWarnings("unchecked")
    public <T> T getElement() {
        return (T) element;
    }

}
