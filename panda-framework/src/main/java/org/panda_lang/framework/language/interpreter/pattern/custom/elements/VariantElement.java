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

package org.panda_lang.framework.language.interpreter.pattern.custom.elements;

import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.interpreter.token.Token;
import org.panda_lang.framework.language.interpreter.pattern.custom.Buildable;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPattern;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPatternElementBuilder;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomReader;
import org.panda_lang.framework.language.interpreter.pattern.custom.Result;
import org.panda_lang.framework.language.interpreter.token.TokenUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class VariantElement extends CustomPatternElementBuilder<Object, VariantElement> {

    public VariantElement(String id) {
        super(id);
    }

    public VariantElement content(Token... tokens) {
        return content(Arrays.stream(tokens)
                .map(Token::getValue)
                .toArray(String[]::new));
    }

    public VariantElement content(String... variants) {
        super.custom((data, source) -> {
            return TokenUtils.valueEquals(source.next(), variants) ? source.getCurrent().orElse(null) : null;
        });

        return this;
    }

    public VariantElement content(Buildable<?>... elements) {
        List<CustomReader<Result>> readers = Arrays.stream(elements)
                .map(CustomPattern::of)
                .map(SubPatternElement::createReader)
                .collect(Collectors.toList());

        super.custom((data, source) -> {
            PandaFrameworkException cachedException = null;

            for (CustomReader<Result> reader : readers) {
                try {
                    Result result = reader.read(data, source);

                    if (result != null) {
                        return result;
                    }
                } catch (PandaFrameworkException e) {
                    cachedException = e;
                }
            }

            if (cachedException != null) {
                throw cachedException;
            }

            return null;
        });

        return this;
    }

    public static VariantElement create(String id) {
        return new VariantElement(id);
    }

}
