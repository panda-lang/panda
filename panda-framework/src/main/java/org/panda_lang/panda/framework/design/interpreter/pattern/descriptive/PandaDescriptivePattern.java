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

package org.panda_lang.panda.framework.design.interpreter.pattern.descriptive;

import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.wildcard.reader.defaults.ExpressionWildcardReader;

public class PandaDescriptivePattern {

    private final DescriptivePatternBuilder builder = DescriptivePattern.builder();

    private PandaDescriptivePattern() { }

    public PandaDescriptivePattern compile(String pattern) {
        builder.compile(pattern);
        return this;
    }

    public DescriptivePattern build(Context context) {
        return builder.build()
                .addWildcardReader(new ExpressionWildcardReader(context.getComponent(UniversalComponents.EXPRESSION)));
    }

    public static PandaDescriptivePattern builder() {
        return new PandaDescriptivePattern();
    }

}
