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

import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.framework.design.interpreter.pattern.custom.AbstractCustomPatternElement;
import org.panda_lang.framework.design.interpreter.pattern.custom.CustomPatternElement;

public final class ExpressionElement extends AbstractCustomPatternElement {

    private ExpressionElement(AbstractCustomPatternElementBuilder builder) {
        super(builder);
    }

    public static ExpressionElementBuilder create(String id) {
        return new ExpressionElementBuilder(id);
    }

    private static final class ExpressionElementBuilder extends AbstractCustomPatternElementBuilder<ExpressionTransaction, ExpressionElementBuilder> {

        private ExpressionElementBuilder(String id) {
            super(id);
        }

        public ExpressionElementBuilder parser(ExpressionParser parser, Context context) {
            super.custom(source -> parser.parseSilently(context, source.getAvailableSource()).orElse(null));
            return this;
        }

        @Override
        public CustomPatternElement build() {
            return new ExpressionElement(this);
        }

    }

}
