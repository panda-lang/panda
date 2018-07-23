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

package org.panda_lang.panda.framework.language.interpreter.pattern.flexible.tree;

public class FlexibleTreeElement {

    private Object value;
    private boolean specified;
    private boolean expression;

    public FlexibleTreeElement(Object element) {
        this.value = element;
        this.specified = element instanceof String;
        this.expression = element instanceof Class;
    }

    public boolean isSpecified() {
        return specified;
    }

    public boolean isExpression() {
        return expression;
    }

    public Class<?> getExpression() {
        return (Class<?>) value;
    }

    public String getSpecified() {
        return (String) value;
    }

}
