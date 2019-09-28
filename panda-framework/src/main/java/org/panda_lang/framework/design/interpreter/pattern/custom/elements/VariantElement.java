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

package org.panda_lang.framework.design.interpreter.pattern.custom.elements;

import org.panda_lang.framework.design.interpreter.pattern.custom.AbstractCustomPatternElement;
import org.panda_lang.framework.design.interpreter.pattern.custom.CustomPatternElement;
import org.panda_lang.framework.design.interpreter.token.Token;
import org.panda_lang.framework.language.interpreter.token.TokenUtils;

public final class VariantElement extends AbstractCustomPatternElement {

    private VariantElement(VariantElementBuilder builder) {
        super(builder);
    }

    public static VariantElementBuilder create(String id) {
        return new VariantElementBuilder(id);
    }

    public static final class VariantElementBuilder extends AbstractCustomPatternElementBuilder<Token, VariantElementBuilder> {

        public VariantElementBuilder(String id) {
            super(id);
        }

        public VariantElementBuilder content(String... variants) {
            super.custom((source, current) -> TokenUtils.valueEquals(current, variants) ? current : null);
            return this;
        }

        @Override
        public CustomPatternElement build() {
            return new VariantElement(this);
        }

    }

}
