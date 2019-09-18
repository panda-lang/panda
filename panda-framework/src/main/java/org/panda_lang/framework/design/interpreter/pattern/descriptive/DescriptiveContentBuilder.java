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

package org.panda_lang.framework.design.interpreter.pattern.descriptive;

import org.panda_lang.utilities.commons.text.ContentJoiner;

public class DescriptiveContentBuilder {

    private final StringBuilder contentBuilder = new StringBuilder();

    private DescriptiveContentBuilder() { }

    public DescriptiveContentBuilder element(String identifier, String content) {
        contentBuilder.append(identifier).append(":");
        return element(content);
    }

    public DescriptiveContentBuilder element(String content) {
        contentBuilder.append(content);
        return this;
    }

    public DescriptiveContentBuilder identifier(String identifier) {
        contentBuilder.append(identifier).append(":");
        return this;
    }

    public DescriptiveContentBuilder variant(Object... elements) {
        contentBuilder.append("(").append(ContentJoiner.on("|").join(elements)).append(")");
        return this;
    }

    public DescriptiveContentBuilder optional(String identifier, String element) {
        return identifier(identifier).optional(element);
    }

    public DescriptiveContentBuilder optional(String element) {
        contentBuilder.append("[").append(element).append("]");
        return this;
    }

    public String build() {
        return contentBuilder.toString();
    }

    public static DescriptiveContentBuilder create() {
        return new DescriptiveContentBuilder();
    }

}
