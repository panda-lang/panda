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

package org.panda_lang.framework.language.interpreter.pattern.descriptive.extractor;

import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.architecture.expression.Expression;

import java.util.Objects;

public final class ExtractorResultElement {

    private final String name;
    private final Object value;

    public ExtractorResultElement(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExtractorResultElement that = (ExtractorResultElement) o;

        return getName().equals(that.getName()) && getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getValue());
    }

    public boolean isExpression() {
        return value instanceof Expression;
    }

    public boolean isSnippet() {
        return value instanceof Snippet;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName() + ": " + getValue();
    }

}
